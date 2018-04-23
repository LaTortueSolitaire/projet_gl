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
 * Arithmetic binary operations (+, -, /, ...)
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type typeDroit = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type typeGauche = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

    	if ((!typeDroit.isFloat() && !typeDroit.isInt()) || (!typeGauche.isFloat() && !typeGauche.isInt())) {
    		throw new ContextualError("Unsupported types for this operation: Operands must be of type float or int", this.getLocation());
    	}
    	if (typeDroit.isInt() && typeGauche.isFloat()) {
    		this.setRightOperand(new ConvFloat(this.getRightOperand()));
    		this.setType(typeGauche);
    		return this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	}
    	if (typeDroit.isFloat() && typeGauche.isInt()) {
    		this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
    		this.setType(typeDroit);
    		return this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	}
    	this.setType(typeDroit);
    	return typeDroit;
    }
    protected abstract void codeGenMnemo(DecacCompiler compiler, boolean method);
    protected Label overflow = new Label("overflow_error");

    /**
     *  Generate code to display the value of an Arithmetic operation in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex){
      this.codeGenExpr(compiler, false); //le résultat de l'opération se trouve dans le registre courant
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
     *  Generate code to display the value of an Arithmetic operation in a method.
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



    /**
      *  Generate code when there is an Arithmetic operation in an expression, in a main program.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we need to decrement the current register.
      *
      */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
      boolean verif_2 = compiler.getCompilerOptions().getRegisterManager().verify(2);

      if(verif_2){
          this.getLeftOperand().codeGenExpr(compiler, false);
          this.getRightOperand().codeGenExpr(compiler, false);
          this.codeGenMnemo(compiler, false);
      }
      else {
          compiler.addInstruction(new PUSH(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
          this.getLeftOperand().codeGenExpr(compiler, false);
          this.getRightOperand().codeGenExpr(compiler, false);
          this.codeGenMnemo(compiler, false);
          compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new POP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();

          }

      if (init) {
         compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      }
    }

    /**
      *  Generate code when there is an Arithmetic operation in an expression, in a method
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we need to decrement the current register.
      *
      */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      boolean verif_2 = compiler.getCompilerOptions().getRegisterManager().verify(2);

      if(verif_2){
          this.getLeftOperand().codeGenExprMethod(compiler, false);
          int toPOP = compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -1;
          if (this.getRightOperand() instanceof DotMethod || this.getLeftOperand() instanceof DotMethod){ //pour gérer le cas ou la right ou left opérande est un appel de méthode donc appelle R2
            compiler.addInstruction(new PUSH(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -1)));
          }
          this.getRightOperand().codeGenExprMethod(compiler, false);
          if (this.getRightOperand() instanceof DotMethod || this.getLeftOperand() instanceof DotMethod){
            compiler.addInstruction(new POP(GPRegister.getR(toPOP)));
          }
          this.codeGenMnemo(compiler, true);
      }
      else {
          compiler.addInstruction(new PUSH(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
          this.getLeftOperand().codeGenExprMethod(compiler, false);
          this.getRightOperand().codeGenExprMethod(compiler, false);
          this.codeGenMnemo(compiler, true);
          compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new POP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();

          }

      if (init) {
         compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      }

    }

    /**
     *  Generate code for an arithmetic operation which the result needs to be convert into a float.
     *      The conversion is happening in the main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprConv(DecacCompiler compiler, boolean init){
      boolean verif_2 = compiler.getCompilerOptions().getRegisterManager().verify(2);

      if(verif_2){
          this.getLeftOperand().codeGenExprConv(compiler, false);
          this.getRightOperand().codeGenExprConv(compiler, false);
          this.codeGenMnemo(compiler, false);
      }
      else {
          compiler.addInstruction(new PUSH(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
          this.getLeftOperand().codeGenExpr(compiler, false);
          this.getRightOperand().codeGenExpr(compiler, false);
          this.codeGenMnemo(compiler, false);
          compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new POP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();

          }
      if (init) {
         compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      }
      compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    }

    /**
     *  Generate code for an arithmetic operation which the result needs to be convert into a float.
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
      boolean verif_2 = compiler.getCompilerOptions().getRegisterManager().verify(2);

      if(verif_2){
          this.getLeftOperand().codeGenExprMethod(compiler, false);/// Modifier pour erreur3
          this.getRightOperand().codeGenExprMethod(compiler, false);
          this.codeGenMnemo(compiler, false);
      }
      else {
          compiler.addInstruction(new PUSH(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
          this.getLeftOperand().codeGenExprMethod(compiler, false);
          this.getRightOperand().codeGenExprMethod(compiler, false);
          this.codeGenMnemo(compiler, false);
          compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new POP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() - 1)));
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();

          }
      if (init) {
         compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      }
      compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
    }



    /**
     *  Generate code to compare the result of an arithmetic operation to zero.
     *      The operation is in the main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    /*Utilisé dans le cas de la division par zéro ou d'un modulo par zéro*/
    protected void compareToZero(AbstractIdentifier divider, boolean typeDivider, DecacCompiler compiler, Label bov){
        divider.codeGenExpr(compiler, false);
        if (typeDivider){ //y est un entier
          compiler.addInstruction(new CMP(0,GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1) ));
        }
        else{
          compiler.addInstruction(new CMP(new ImmediateFloat((float)0.0),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1) ));
        }
        if(!compiler.getCompilerOptions().getNoCheck()){
            compiler.addInstruction(new BOV(bov));
        }
        compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();

      }

      /**
       *  Generate code to compare the result of an arithmetic operation to zero.
       *      The operation is in a method.
       *
       * @param compiler  (contains the "env_types" attribute)
       *
       * @param init is used to know if we are in an initialization case.
       *     If true, we don't need to increment the current register.
       *
       */
      protected void compareToZeroMethod(AbstractIdentifier divider, boolean typeDivider, DecacCompiler compiler, Label bov){
          divider.codeGenExprMethod(compiler, false);
          if (typeDivider){ //y est un entier
            compiler.addInstruction(new CMP(0,GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1) ));
          }
          else{
            compiler.addInstruction(new CMP(new ImmediateFloat((float)0.0),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1) ));
          }
          if(!compiler.getCompilerOptions().getNoCheck()){
              compiler.addInstruction(new BOV(bov));
          }
          compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();

        }

  /**
    *  Generate code for a comparison that has an arithmetic operation in his expression.
    *  The comparison is in the main program.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param deb is the label we have to jump if the result of the comparison is true
    *
    * @param end is the label we have to jump if the result of the comparison is false
    *
    */
   /* dans le cas d'une opération arithmétique dans une condition*/
    protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end) {
    	this.codeGenExpr(compiler, false);
    }

    /**
      *  Generate code for a comparison that has an arithmetic operation in his expression.
      *  The comparison is in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param deb is the label we have to jump if the result of the comparison is true
      *
      * @param end is the label we have to jump if the result of the comparison is false
      *
      */
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end) {
    	this.codeGenExprMethod(compiler, false);
    }

}
