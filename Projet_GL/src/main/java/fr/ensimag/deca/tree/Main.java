package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VoidType;


/**
 * 
 * @author gl17
 * @date 01/01/2018
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        EnvironmentExp envVide = new EnvironmentExp(null);
        VoidType returnVoid = new VoidType(compiler.getSymbolTable().create("void"));
        this.declVariables.verifyListDeclVariable(compiler, envVide, null);
        this.insts.verifyListInst(compiler, envVide, null, returnVoid);
    }

    /**
     *  Generate code for the main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     *
     */
    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        compiler.getLinkedList().getLast().addComment("Beginning of main instructions:");
        declVariables.codeGenListDeclVar(compiler, Register.GB, false);  //ici on doit passer GB et compteurGB
        insts.codeGenListInst(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
