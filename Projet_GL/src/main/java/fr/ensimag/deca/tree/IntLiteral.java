package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;

/**
 * Integer literal
 *
 * @author gl17
 * @date 01/01/2018
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
      Type intType = new IntType(compiler.getSymbolTable().create("int"));
      this.setType(intType);
      return intType;
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    protected ImmediateInteger getDval(){
      return new ImmediateInteger(this.getValue());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
     *  Generate code to display an integer in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
      compiler.addInstruction(new WSTR(new ImmediateString(Integer.toString(value))));
    }

    /**
     *  Generate code to display an integer in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    protected void codeGenPrintMethod(DecacCompiler compiler, boolean printHex){
      this.codeGenPrint(compiler, printHex);
    }


  /**
    *  Generate code for an integer in an expression, in a main program.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param init is used to know if we are in an initialization case.
    *           If true, we don't need to increment the current register.
    *
    */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init) {
        compiler.addInstruction(new LOAD(new ImmediateInteger(this.value), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    	if(!init) {
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
    	}
    }
    /**
      *  Generate code for an integer in an expression, in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init) {
        this.codeGenExpr(compiler, init);
    }

    /**
     *  Generate code for a comparison that has an integer in his expression.
     *  The comparison is in the main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param deb is the label we have to jump if the result of the comparison is true
     *
     * @param end is the label we have to jump if the result of the comparison is false
     *
     */
    @Override
    protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end) {
  	  this.codeGenExpr(compiler,false);
    }

    /**
     *  Generate code for a comparison that has an integer in his expression.
     *  The comparison is in the method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param deb is the label we have to jump if the result of the comparison is true
     *
     * @param end is the label we have to jump if the result of the comparison is false
     *
     */
    @Override
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end) {
  	  this.codeGenExprMethod(compiler,false);
    }

    /**
     *  Generate code for an integer that needs to be convert into a float.
     *      The conversion is happening in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprConv(DecacCompiler compiler, boolean init) {
    	compiler.addInstruction(new LOAD(new ImmediateFloat(this.value), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      if(!init){
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
    }

    /**
     *  Generate code for an integer that needs to be convert into a float.
     *      The conversion is happening in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprConvMethod(DecacCompiler compiler, boolean init) {
      this.codeGenExprConv(compiler, init);
    }

}
