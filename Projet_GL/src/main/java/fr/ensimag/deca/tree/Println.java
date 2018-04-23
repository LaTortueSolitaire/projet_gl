package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WNL;

/**
 * Print instruction with carriage return
 * @author gl17
 * @date 01/01/2018
 */
public class Println extends AbstractPrint {

    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printlnx)
     */
    public Println(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    /**
     *  Generate code for a Println instruction in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     *
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
        compiler.addInstruction(new WNL());
    }

    /**
     *  Generate code for a Println instruction in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     *
     */
    @Override
    protected void codeGenInstMethod(DecacCompiler compiler) {
        super.codeGenInstMethod(compiler);
        compiler.addInstruction(new WNL());
    }

    @Override
    String getSuffix() {
        return "ln";
    }
}
