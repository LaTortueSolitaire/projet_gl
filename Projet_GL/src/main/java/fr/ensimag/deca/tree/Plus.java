package fr.ensimag.deca.tree;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;


/**
 * Operator plus ("+")
 * @author gl17
 * @date 01/01/2018
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "+";
    }

    /**
     *  Generate code for a Plus instruction in a program or in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param method is used to know if the plus is in a method or in a main program.
     *      This boolean is not useful in this case.
     *
     */
    protected void codeGenMnemo(DecacCompiler compiler, boolean method){
      if (this.getLeftOperand() instanceof IntLiteral && this.getRightOperand() instanceof IntLiteral && this.getLeftOperand().getType().isInt() && this.getRightOperand().getType().isInt() && (long) ((IntLiteral) this.getLeftOperand()).getValue() + (long) ((IntLiteral) this.getRightOperand()).getValue() != ((IntLiteral) this.getLeftOperand()).getValue() + ((IntLiteral) this.getRightOperand()).getValue()) {
    	  int res = ((IntLiteral) this.getLeftOperand()).getValue() + ((IntLiteral) this.getRightOperand()).getValue();
    	  compiler.addInstruction(new LOAD(res, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
    	  compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      } else {
          compiler.addInstruction(new ADD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
          compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
          if(!compiler.getCompilerOptions().getNoCheck()){
            compiler.addInstruction(new BOV(this.overflow));
          }
      }
    }
}
