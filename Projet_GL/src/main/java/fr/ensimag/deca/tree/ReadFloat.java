package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;


/**
 *	Interactive input for a float entry
 * @author gl17
 * @date 01/01/2018
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	this.setType(new FloatType(compiler.getSymbolTable().create("float")));
    	return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    /**
     *  Generate code for a ReadInt expression in main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
        Label bov = new Label("io_error");
        compiler.addInstruction(new WSTR("Enter a float  "));
        compiler.addInstruction(new RFLOAT());
        compiler.addInstruction(new BOV(bov));
        compiler.addInstruction(new LOAD(GPRegister.R1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
        if(!init) {
        	compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
        }
    }

    /**
     *  Generate code for a ReadFloat expression in method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      this.codeGenExpr(compiler, init);
    }

}
