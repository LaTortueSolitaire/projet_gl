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

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl17
 * @date 01/01/2018
 */
public class NoInitialization extends AbstractInitialization {

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

    }


    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // nothing
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
     *  Generate code for the initialization of a variable.
     *    The value of this one is zero, null if it's an object from a class.
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param t is the type of the variable that have no initialization
     *
     */
    @Override
    protected void codeGenInit(DecacCompiler compiler, Type t){
      if(t.isInt() || t.isBoolean()){
          compiler.addInstruction(new LOAD(0, GPRegister.getR(2)));
      }
      else if( t.isFloat()){
          compiler.addInstruction(new LOAD(new ImmediateFloat((float)0.0), GPRegister.getR(2)));
      }
      else{
          compiler.addInstruction(new LOAD(new NullOperand(), GPRegister.getR(2)));
      }

    }


}
