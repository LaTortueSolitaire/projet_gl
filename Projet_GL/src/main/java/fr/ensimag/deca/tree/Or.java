package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *	Logical "or" operation ("||")
 * @author gl17
 * @date 01/01/2018
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }
    /* pour effectuer plusieurs or à la suite */
    private static int nbOr = 0;

    public int getNbOr(){
      return this.nbOr;
    }

    public void incrementNbOr(){
      this.nbOr++;
    }



    /**
     *  Generate code for an Or expression in a declaration for example, in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *
     *
     */
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
        this.getLeftOperand().codeGenExpr(compiler, init);
        Label f = new Label("False_Or." + this.getNbOr());
        Label t = new Label("True_Or." + this.getNbOr());
        this.incrementNbOr();
        compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
        compiler.addInstruction(new BEQ(t));
        this.getRightOperand().codeGenExpr(compiler, init);
        compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
        compiler.addInstruction(new BEQ(t));
        compiler.addInstruction(new LOAD(0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
        compiler.addInstruction(new BRA(f));
        compiler.getLinkedList().getLast().addLabel(t);
        compiler.addInstruction(new LOAD(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
        compiler.getLinkedList().getLast().addLabel(f);

  }

  /**
   *  Generate code for an Or expression in a declaration for example, in a method.
   *
   * @param compiler  (contains the "env_types" attribute)
   *
   * @param init is used to know if we are in an initialization case.
   *
   *
   */
  protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      this.getLeftOperand().codeGenExprMethod(compiler, init);
      Label f = new Label("False_Or." + this.getNbOr() );
      Label t = new Label("True_Or." + this.getNbOr());
      this.incrementNbOr();
      compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.addInstruction(new BEQ(t));
      this.getRightOperand().codeGenExprMethod(compiler, init);
      compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.addInstruction(new BEQ(t));
      compiler.addInstruction(new LOAD(0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
      compiler.addInstruction(new BRA(f));
      compiler.getLinkedList().getLast().addLabel(t);
      compiler.addInstruction(new LOAD(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
      compiler.getLinkedList().getLast().addLabel(f);

}

/**
 *  Generate code for a comparison that has an Or in his expression.
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
    protected void codeGenCompare(DecacCompiler compiler,Label deb,  Label end){
      Label eTrans = new Label("Transition"+ this.getNbOr());
      this.incrementNbOr();
      this.getLeftOperand().codeGenCompare(compiler, deb, eTrans);
      if (this.getLeftOperand() instanceof AbstractOpCmp){///rajouter
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
      compiler.addInstruction(new BRA(deb));
      compiler.getLinkedList().getLast().addLabel(eTrans);
      this.getRightOperand().codeGenCompare(compiler, deb, end);

    }

    /**
     *  Generate code for a comparison that has an Or in his expression.
     *  The comparison is in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param deb is the label we have to jump if the result of the comparison is true
     *
     * @param end is the label we have to jump if the result of the comparison is false
     *
     */
    @Override
    protected void codeGenCompareMethod(DecacCompiler compiler,Label deb,  Label end){
      Label eTrans = new Label("Transition" + this.getNbOr());
      this.incrementNbOr();
      this.getLeftOperand().codeGenCompareMethod(compiler, deb, eTrans);
      /*if (this.getLeftOperand() instanceof AbstractOpCmp){//rajouter
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }*/
      compiler.addInstruction(new BRA(deb));
      compiler.getLinkedList().getLast().addLabel(eTrans);
      this.getRightOperand().codeGenCompareMethod(compiler, deb, end);
    }

}
