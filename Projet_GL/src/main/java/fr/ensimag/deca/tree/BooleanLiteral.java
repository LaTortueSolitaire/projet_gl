package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;
/**
 *	Boolean expression
 * @author gl17
 * @date 01/01/2018
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type booleanType = new BooleanType(compiler.getSymbolTable().create("boolean"));
        this.setType(booleanType);
        return booleanType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }


    /**
      *  Generate code for a boolean in an expression, in a main program.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */
    protected void codeGenExpr(DecacCompiler compiler, boolean init) {
          int bool = (this.getValue())?1:0;
          compiler.addInstruction(new LOAD(new ImmediateInteger(bool), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          if (!init) {
        	  compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
          }
    }

    /**
      *  Generate code for a boolean in an expression, in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init) {
          this.codeGenExpr(compiler, init);
    }


    /**
     *  Generate code for a comparison that has a boolean in his expression.
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
    /*peut être à mettre dans UnuaryExpr car identique pour Int, Float, Boolean*/
    protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end) {
  	  this.codeGenExpr(compiler,false); //avant à false;
  	  compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
  	  compiler.addInstruction(new BNE(end));
    }

    /**
     *  Generate code for a comparison that has a boolean in his expression.
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
    /*peut être à mettre dans UnuaryExpr car identique pour Int, Float, Boolean*/
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end) {
      this.codeGenExprMethod(compiler,false);
      compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
  	  compiler.addInstruction(new BNE(end));
    }
}
