package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl17
 * @date 01/01/2018
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    /**
     *  Implements non-terminal "program"
     *    of [SyntaxeContextuelle] in pass 1,2,3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @throws ContextualError if contextual syntax isn't respected
     */

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
    	try {
          compiler.getEnvType().declare(compiler.getSymbolTable().create("int"), new TypeDefinition(new IntType(compiler.getSymbolTable().create("int")), Location.BUILTIN));
          compiler.getEnvType().declare(compiler.getSymbolTable().create("boolean"), new TypeDefinition(new BooleanType(compiler.getSymbolTable().create("boolean")), Location.BUILTIN));
          compiler.getEnvType().declare(compiler.getSymbolTable().create("float"), new TypeDefinition(new FloatType(compiler.getSymbolTable().create("float")), Location.BUILTIN));
          compiler.getEnvType().declare(compiler.getSymbolTable().create("string"), new TypeDefinition(new StringType(compiler.getSymbolTable().create("string")), Location.BUILTIN));
          compiler.getEnvType().declare(compiler.getSymbolTable().create("void"), new TypeDefinition(new VoidType(compiler.getSymbolTable().create("void")), Location.BUILTIN));
          ClassDefinition Object = new ClassDefinition(new ClassType(compiler.getSymbolTable().create("Object"), Location.BUILTIN, null), Location.BUILTIN, null);

          Object.setOperand(new RegisterOffset(1, Register.GB));
          int indexEqualObject = Object.incNumberOfMethods();

          Type typeRetourner = compiler.getEnvType().get(compiler.getSymbolTable().create("boolean")).getType();
          Signature sign = new Signature();
          sign.add(Object.getType());
          MethodDefinition defDeEquals = new MethodDefinition(typeRetourner, Location.BUILTIN, sign, indexEqualObject);
          Label object = new Label("code.Object.equals");
          defDeEquals.setLabel(object);
          // Object.setDefinition(defDeEquals);
          Object.getMembers().declare(compiler.getSymbolTable().create("equals"), defDeEquals);
          compiler.getEnvType().declare(compiler.getSymbolTable().create("Object"), Object);
            // compiler.getEnvType().declare(compiler.getSymbolTable().create("object"), new ClassDefinition(new ClassType(compiler.getSymbolTable().create("object"), Location.BUILTIN, null), Location.BUILTIN, null));


        } catch (EnvironmentType.DoubleDefException e) {
          throw new ContextualError("Double definition of predefined type", this.getLocation());
        } catch (EnvironmentExp.DoubleDefException e) {
          throw new ContextualError("Double definition of predefined method", this.getLocation());
        }

        //Passe 1
        this.getClasses().verifyListClass(compiler);
        //Passe 2
        this.getClasses().verifyListClassMembers(compiler);
        //Passe 3
        this.getClasses().verifyListClassBody(compiler);
        this.main.verifyMain(compiler);
    }
    
    /**
     *  Generate code for the entire program
     *
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
        IMAProgram firstOne = new IMAProgram();
        compiler.getLinkedList().add(firstOne);
        compiler.getLinkedList().getLast().addComment("Premiere passe, construction table des methodes");
        this.classes.codeGenPreListDeclClass(compiler);
        compiler.getLinkedList().getLast().addComment("");



        compiler.getLinkedList().getLast().addComment("");
        compiler.getLinkedList().getLast().addComment("");
        compiler.getLinkedList().getLast().addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        compiler.getLinkedList().getLast().addComment("End of Main Programm");



        compiler.getLinkedList().getLast().addComment("");
        compiler.getLinkedList().getLast().addComment("");
        compiler.getLinkedList().getLast().addComment("Messages d'erreurs");

        Label io = new Label("io_error");
        compiler.getLinkedList().getLast().addLabel(io);
        compiler.addInstruction(new WSTR("Error: Input/Output error"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        Label stack = new Label("stack_overflow_error");

        if(!compiler.getCompilerOptions().getNoCheck()){

            compiler.getLinkedList().getLast().addComment("Les debordements");


            compiler.getLinkedList().getLast().addLabel(stack);
            compiler.addInstruction(new WSTR("Error: Stack overflow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());


            Label heap = new Label("heap_overflow_error");
            compiler.getLinkedList().getLast().addLabel(heap);
            compiler.addInstruction(new WSTR("Error: Heap overflow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            Label overflow = new Label("overflow_error");
            compiler.getLinkedList().getLast().addLabel(overflow);
            compiler.addInstruction(new WSTR("Error: Overflow during arithmetical operation"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            Label zeroDivision = new Label("division_or_modulo_per_zero");
            compiler.getLinkedList().getLast().addLabel(zeroDivision);
            compiler.addInstruction(new WSTR("Error : Division or modulo by zero"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            Label dereferencement = new Label("dereferencement.null");
            compiler.getLinkedList().getLast().addLabel(dereferencement);
            compiler.addInstruction(new WSTR("Error : Dereferencing null object"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

        }

        int varG = compiler.getCompilerOptions().getRegisterManager().getVarGlobale();
        int varTempo = compiler.getCompilerOptions().getRegisterManager().getMaxTempo()+2;


        compiler.getLinkedList().getLast().addFirst(new ADDSP(varG));
        if(!compiler.getCompilerOptions().getNoCheck()){
          compiler.getLinkedList().getLast().addFirst(new BOV(stack));
        }
        compiler.getLinkedList().getLast().addFirst(new TSTO(varG + varTempo));
        // compiler.getLinkedList().getLast().addFirst(new TSTO(0));

        IMAProgram avantDeniere = new IMAProgram();
        compiler.getLinkedList().addLast(avantDeniere);
        compiler.getLinkedList().getLast().addComment("");
        compiler.getLinkedList().getLast().addComment("");
        compiler.getLinkedList().getLast().addComment("Deuxieme passe, initialisation des champs");
        this.classes.codeGenListDeclClass(compiler);

        IMAProgram lastOne = new IMAProgram();
        compiler.getLinkedList().addLast(lastOne);
        compiler.getLinkedList().getLast().addComment("");
        compiler.getLinkedList().getLast().addComment("Deuxième passe, génération de code des méthodes");
        this.classes.codeGenListDeclClassMethod(compiler);


    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
