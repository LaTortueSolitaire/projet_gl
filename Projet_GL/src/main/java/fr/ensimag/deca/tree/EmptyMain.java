package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Empty main Deca program
 *
 * @author gl17
 * @date 01/01/2018
 */
public class EmptyMain extends AbstractMain {
    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
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

    }
    /**
     * Contains no real information => nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // no main program => nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}
