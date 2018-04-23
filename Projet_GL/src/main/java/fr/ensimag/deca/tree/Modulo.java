package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *	Modulo operartion ("%")
 * @author gl17
 * @date 01/01/2018
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type lType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!lType.isInt() || !rType.isInt()) {
          throw new ContextualError("Unsupported types for this operation: Operands must be of type int", this.getLocation());
        }
        this.setType(rType);
        return rType;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    /**
     *  Generate code for a Modulo instruction in a program or in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param method is used to know if the plus is in a method or in a main program.
     *
     *
     */

    protected void codeGenMnemo(DecacCompiler compiler, boolean method){
      Label bov = new Label("division_or_modulo_per_zero");
        if(this.getRightOperand() instanceof AbstractOpArith){

            compiler.addInstruction(new REM(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));

        }
        else if (this.getLeftOperand() instanceof AbstractOpArith){
              compiler.addInstruction(new REM(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));

        }
        else{
              compiler.addInstruction(new REM(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-2)));
          //On gère le cas du %0
          if (this.getRightOperand() instanceof AbstractLValue){ //lorsque la variable par laquelle on fait le modulo vaut 0
            if (method){
              this.compareToZeroMethod((AbstractIdentifier)this.getRightOperand(), true, compiler, bov);
            }
            else{
          	   this.compareToZero((AbstractIdentifier)this.getRightOperand(), true, compiler, bov);
            }
          }
          else{ //lorsque l'entier vaut 0
            IntLiteral divider;
          if (!(this.getRightOperand() instanceof DotMethod)){
              divider = (IntLiteral) this.getRightOperand();
              if(!compiler.getCompilerOptions().getNoCheck()){
                  if (divider.getValue() == 0){
                      compiler.addInstruction(new BOV(bov));
                  }
              }
          }
          else{
            this.getRightOperand().codeGenInst(compiler); //je charge dans R0 la valeur de retour
            compiler.addInstruction(new CMP(0, Register.R0)); //je compare la valeur de R0 à 0
            compiler.addInstruction(new BOV(bov));
          }
        }
      }
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    }

}
