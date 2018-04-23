package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;



/**
 * Unary operator "-"
 * @author gl17
 * @date 01/01/2018
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type opType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(opType.isInt() || opType.isFloat())) {
        	throw new ContextualError("Unsupported type for this operation: Operand must be of type int or float", this.getLocation());
        }
        this.setType(opType);
        return opType;
    }

    /**
     *  Generate code for an UnaryMinus in an expression in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we need to decrement current register because it has been increment before.
     *
     */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init) {
    	this.getOperand().codeGenExpr(compiler, false); //false pour quand on fait un print d'opération binaire
    	compiler.addInstruction(new OPP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
      if (init){
        compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      }
    }

    /**
     *  Generate code for an UnaryMinus in an expression in method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      this.getOperand().codeGenExprMethod(compiler, true);
      compiler.addInstruction(new OPP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      if (!init){
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant(); //car par exemple dans le cas d'un assign il faut incrémenter
      }
    }

    /**
     *  Generate code for a comparison that has an UnuaryMinus in his expression.
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
    	this.codeGenExpr(compiler, false);
    }


    /**
     *  Generate code for a comparison that has an UnuaryMinus in his expression.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param deb is the label we have to jump if the result of the comparison is true
     *
     * @param end is the label we have to jump if the result of the comparison is false
     *
     */
   @Override
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end){
      this.codeGenExprMethod(compiler, false);
    }

    /**
     *  Generate code for an UnaryMinus that needs to be convert into a minus float.
     *      The conversion is happening in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprConv(DecacCompiler compiler, boolean init){
      this.getOperand().codeGenExprConv(compiler, true);
      compiler.addInstruction(new OPP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      if (!init){
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant(); //car par exemple dans le cas d'un assign il faut incrémenter
      }
    }

    /**
     *  Generate code for an UnaryMinus that needs to be convert into a minus float.
     *      The conversion is happening in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprConvMethod(DecacCompiler compiler, boolean init){
      this.codeGenExprConv(compiler, init);
    }


    /**
     *  Generate code to display a UnuaryMinus in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex){
      this.codeGenExpr(compiler, false);
      compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.R1));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      Type type = this.getType();
      if (type.isInt()){
        compiler.addInstruction(new WINT());
      }
      else if (type.isFloat()){
        if (!printHex){
          compiler.addInstruction(new WFLOAT());
        }
        else{
          compiler.addInstruction(new WFLOATX());
        }
      }

    }


    /**
     *  Generate code to display a UnuaryMinus in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    protected void codeGenPrintMethod(DecacCompiler compiler, boolean printHex){
      this.codeGenExprMethod(compiler, false); //le résultat de l'opération se trouve dans le registre courant
      compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.R1));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      Type type = this.getType();
      if (type.isInt()){
        compiler.addInstruction(new WINT());
      }
      else if (type.isFloat()){
        if (!printHex){
          compiler.addInstruction(new WFLOAT());
        }
        else{
          compiler.addInstruction(new WFLOATX());
        }
      }

    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
