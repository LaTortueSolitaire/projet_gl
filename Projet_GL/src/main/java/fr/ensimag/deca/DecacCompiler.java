package fr.ensimag.deca;

import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

import java.util.concurrent.Callable;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.context.EnvironmentType;

import java.util.*;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction) instead of compiler.getProgram().addInstruction()).
 *
 * @author gl17
 * @date 01/01/2018
 */
public class DecacCompiler implements Callable<Boolean>{
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public void setOpBinary(){}
    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;

        this.symbolTable = new SymbolTable();
        this.envType = new EnvironmentType(null);
        this.listImap = new LinkedList<IMAProgram>();

    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        this.getLinkedList().getLast().addInstruction(instruction);
        if (instruction instanceof LOAD){
            this.getCompilerOptions().getRegisterManager().checkMax();
            this.getCompilerOptions().getRegisterManager().checkRegFin();
        }
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        this.getLinkedList().getLast().addInstruction(instruction, comment);
        if (instruction instanceof LOAD){
            this.getCompilerOptions().getRegisterManager().checkMax();
            this.getCompilerOptions().getRegisterManager().checkRegFin();
        }
    }

    public void addFirst(Instruction instruction){
        program.addFirst(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    private final CompilerOptions compilerOptions;
    private final File source;
    private int compteur = 3;
    private int compteurLB = 1;
    private int nombreMethod = 0;
    private int compteurParam = -3;


    public void incrementNombreMethod(){
        this.nombreMethod++;
    }

    public int getNombreMethod(){
        return this.nombreMethod;
    }

    public void incrementCompteur(){
      this.compteur++;
    }

    public int getCompteur(){
        return this.compteur;
    }

    public void incrementCompteurLB(){
      this.compteurLB++;
    }

    public void incrementCompteurParam(){
      this.compteurParam++;
    }

    public int getCompteurParam(){
      return this.compteurParam;
    }

    public int getCompteurLB(){
      return this.compteurLB;
    }

    public void resetCompteurLB(){
      this.compteurLB = 1;
    }

    public void setCompteurParam(int nbParamtres){
      this.compteurParam -= nbParamtres;
    }

    /*utile pour déclarer plusieurs booléens à la suite, nécessaire pour les étiquettes*/
    private int iLabel = 0;

    public int getiLabel(){
      return this.iLabel;
    }

    public void incrementiLabel(){
      this.iLabel++;
    }



    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
    private LinkedList<IMAProgram> listImap;

    public LinkedList<IMAProgram> getLinkedList(){
      return this.listImap;
    }


    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = null;
        // A FAIRE: calculer le nom du fichier .ass à partir du nom du
        // A FAIRE: fichier .deca.
        int lenSourceFile = sourceFile.length();
        destFile = sourceFile.substring(0, lenSourceFile-5) + ".ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        // a besoin des decompile pour faire les assertions
        // assert(prog.checkAllLocations());


        if(!compilerOptions.getParse()){ //l'option -p n'est pas activée
          prog.verifyProgram(this);
          // assert(prog.checkAllDecorations());

          if(!compilerOptions.getVerif()){ //l'option -v n'est pas activée

            prog.codeGenProgram(this);

            Iterator<IMAProgram> iterator = this.listImap.iterator();
            while(iterator.hasNext()){
              IMAProgram imap = iterator.next();
              program.append(imap);
            }

            LOG.debug("Generated assembly code:" + nl + program.display());
            LOG.info("Output file assembly file is: " + destName);

            FileOutputStream fstream = null;
            try {
                fstream = new FileOutputStream(destName);
            } catch (FileNotFoundException e) {
                throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
            }

            LOG.info("Writing assembler file ...");

            program.display(new PrintStream(fstream));
            LOG.info("Compilation of " + sourceName + " successful.");
            // return false;
          }
          else{ //l'option -v est activée
            return false;
          }
        }
        else{
          prog.decompile(out);
        }
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(new ANTLRFileStream(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    private SymbolTable symbolTable;

    public SymbolTable getSymbolTable() {
      return this.symbolTable;
    }

    private EnvironmentType envType;

    public EnvironmentType getEnvType() {
      return this.envType;
    }

    @Override
    public Boolean call() throws Exception {
        return compile();
      }
}
