package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
/**

/**
 *	Exact comparison operator for non-equality ("!=")
 * @author gl17
 * @date 01/01/2018
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }

    /**
     *  Generate code for a NotEquals expression in a declaration for example, in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *
     *
     */
    @Override
    //pour une déclaration
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
      this.getLeftOperand().codeGenExpr(compiler, false); //met la valeur dans le registre courant et incrémente
      this.getRightOperand().codeGenExpr(compiler, false);//met la valeur dans le deuxième registre et incrémente
      compiler.addInstruction(new CMP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -2)));
      Label loadFalse = new Label("load_false." + compiler.getiLabel());
      Label loadTrue = new Label("load_true." + compiler.getiLabel());
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      compiler.addInstruction(new BEQ(loadFalse));
      compiler.addInstruction(new LOAD(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.addInstruction(new BRA(loadTrue));
      compiler.getLinkedList().getLast().addLabel(loadFalse);
      compiler.addInstruction(new LOAD(0,  GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.getLinkedList().getLast().addLabel(loadTrue);
      compiler.incrementiLabel();
    }

    /**
     *  Generate code for a NotEquals expression in a declaration for example, in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *
     *
     */
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      this.codeGenExpr(compiler, init); //rajouté en faisant la doc
    }


    /**
     *  Generate code for a comparison that has a NotEquals in his expression.
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
      this.getLeftOperand().codeGenCompare(compiler, deb, end);
      if (this.getLeftOperand() instanceof AbstractOpCmp){
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
      this.getRightOperand().codeGenCompare(compiler, deb, end);
      compiler.addInstruction(new CMP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -2)));
      compiler.addInstruction(new BEQ(end));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    }

    /**
     *  Generate code for a comparison that has a NotEquals in his expression.
     *      The comparison is in a method.
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
      this.getLeftOperand().codeGenCompareMethod(compiler, deb, end);
      if (this.getLeftOperand() instanceof AbstractBinaryExpr){
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
      this.getRightOperand().codeGenCompareMethod(compiler, deb, end);
      compiler.addInstruction(new CMP(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant() -2)));
      compiler.addInstruction(new BEQ(end));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    }

}
