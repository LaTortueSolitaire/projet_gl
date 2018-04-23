package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 * Divide operation ("/")
 * @author gl17
 * @date 01/01/2018
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }



    /**
     *  Generate code for a Divide instruction in a program or in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param method is used to know if the plus is in a method or in a main program.
     *
     *
     */
    protected void codeGenMnemo(DecacCompiler compiler, boolean method){
      //division entre entiers
      Label bov = new Label("division_or_modulo_per_zero");
      if (this.getRightOperand() instanceof AbstractBinaryExpr){
        if (!this.getRightOperand().getType().isFloat() && !this.getLeftOperand().getType().isFloat()){
          compiler.addInstruction(new CMP(0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
          compiler.addInstruction(new BEQ(bov));
          compiler.addInstruction(new QUO(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
        }
        else{
          compiler.addInstruction(new CMP(new ImmediateFloat((float) 0.0), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
          compiler.addInstruction(new BEQ(bov));
          compiler.addInstruction(new DIV(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
        }
      }

      else if (this.getLeftOperand() instanceof AbstractBinaryExpr){
        if (!this.getRightOperand().getType().isFloat() && !this.getLeftOperand().getType().isFloat()){
          compiler.addInstruction(new QUO(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
        }
        else{
          compiler.addInstruction(new DIV(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
        }
      }

      else if (!this.getRightOperand().getType().isFloat() && !this.getLeftOperand().getType().isFloat()){
        compiler.addInstruction(new QUO(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));

    	  //On gère le cas de la division par zéro
        if (this.getRightOperand() instanceof AbstractLValue){ //lorsque la variable par laquelle on divise vaut 0
          if (method){
            this.compareToZeroMethod((AbstractIdentifier)this.getRightOperand(), true, compiler, bov);
          }
          else{
        	   this.compareToZero((AbstractIdentifier)this.getRightOperand(), true, compiler, bov);
          }
        }
        else if (this.getRightOperand() instanceof DotMethod){
          }
        else{ //lorsque l'entier vaut 0
          IntLiteral divider = (IntLiteral) this.getRightOperand();
          if(!compiler.getCompilerOptions().getNoCheck()){
              if (divider.getValue() == 0){
                  compiler.addInstruction(new BOV(bov));
              }
          }
        }
      }
      //division entre flottants
      else if (this.getRightOperand().getType().isFloat() && this.getLeftOperand().getType().isFloat()){
          compiler.addInstruction(new DIV(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
          if (this.getRightOperand() instanceof AbstractLValue){ //lorsque la variable par laquelle on divise vaut 0.0
            if (method){
              this.compareToZeroMethod((AbstractIdentifier)this.getRightOperand(), false, compiler, bov);
            }
            else{
               this.compareToZero((AbstractIdentifier)this.getRightOperand(), false, compiler, bov);
            }
          }
          else if (!(this.getRightOperand() instanceof DotMethod) && !(this.getRightOperand() instanceof ConvFloat)){
                FloatLiteral divider;
        	      divider = (FloatLiteral) this.getRightOperand();
                if(!compiler.getCompilerOptions().getNoCheck()){
                    if (divider.getValue() == 0){
                        compiler.addInstruction(new BOV(bov));
                    }
                }
            }
          else if (this.getRightOperand() instanceof DotMethod){
            }
          else if (this.getRightOperand() instanceof ConvFloat){
              Type t = this.getRightOperand().getType();
              if (t.isFloat()){
                ConvFloat conv = (ConvFloat) this.getRightOperand();
                IntLiteral divider = (IntLiteral) conv.getOperand();
                if (divider.getValue() == 0){
                  compiler.addInstruction(new BOV(bov));
                }
              }
            }

          }


      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    }
}
