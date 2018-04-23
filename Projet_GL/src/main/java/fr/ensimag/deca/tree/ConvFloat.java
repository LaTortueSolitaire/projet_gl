package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        Type newFType = new FloatType(compiler.getSymbolTable().create("float"));
        this.setType(newFType);
        return newFType;
    }


  /**
    *  Generate code for a ConvFloat expression in a main program.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param init is used to know if we are in an initialization case.
    *           If true, we don't need to increment the current register.
    *
    */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init) {
    	if (this.getOperand().getType().isInt() || this.getOperand() instanceof AbstractIdentifier) {
    		this.getOperand().codeGenExprConv(compiler, init);
    	}
      else {
    		this.getOperand().codeGenExpr(compiler, false);
    	}
    }

    /**
      *  Generate code for a ConvFloat expression in a main program.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */

   @Override
   protected void codeGenExprMethod(DecacCompiler compiler, boolean init) {
     if (this.getOperand().getType().isInt() || this.getOperand() instanceof AbstractIdentifier) {
       if (this.getOperand() instanceof AbstractOpArith){
         this.getOperand().codeGenExprConvMethod(compiler, false);
         if (init){
                //System.err.println("heyhey je décrémente");
                compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
              }
        }
      else{
        this.getOperand().codeGenExprConvMethod(compiler, init);
        }
      }

    else {
      this.getOperand().codeGenExprMethod(compiler, false);
    }

}

    /**
    *  Generate code for a comparison that need a ConvFloat in his expression.
    *  The comparison is in the main program.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param deb is the label we have to jump if the result of the comparison is true
    *
    * @param end is the label we have to jump if the result of the comparison is false
    *
    */
    protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end) {
    	this.getOperand().codeGenExprConv(compiler, false); // Pas sur que ce soit false qu'il faut
    }

    /**
    *  Generate code for a comparison that need a ConvFloat in his expression.
    *  The comparison is in the method.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param deb is the label we have to jump if the result of the comparison is true
    *
    * @param end is the label we have to jump if the result of the comparison is false
    *
    */
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end) {
      this.getOperand().codeGenExprConv(compiler, false);
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
