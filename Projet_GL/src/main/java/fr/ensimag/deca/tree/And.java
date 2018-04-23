package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.*;

import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
/**
 *	Logical "and" operation ("&&")
 * @author gl17
 * @date 01/01/2018
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    private static int nbAnd = 0;


    /**
     *  Generate code for a comparison that has a And in his expression.
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
      this.getRightOperand().codeGenCompare(compiler, deb, end);
    }

    /**
     *  Generate code for a comparison that has a And in his expression.
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
      this.getLeftOperand().codeGenCompareMethod(compiler, deb, end);
      this.getRightOperand().codeGenCompareMethod(compiler, deb, end);
    }



    /**
    *  Generate code for a And expression in a declaration for example, in a main program.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param init is used to know if we are in an initialization case.
    *
    *
    */
    /*utilisé pour la déclaration de booléen*/
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
          Label f = new Label("False_And." + nbAnd );
          Label t = new Label("True_And." + nbAnd);
          nbAnd++;

          this.getLeftOperand().codeGenExpr(compiler, init);
          compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new BNE(f));
          this.getRightOperand().codeGenExpr(compiler, init);
          compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new BNE(f));
          compiler.addInstruction(new LOAD(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
          compiler.addInstruction(new BRA(t));//pour éviter de load 0
          compiler.getLinkedList().getLast().addLabel(f); //on store false
          compiler.addInstruction(new LOAD(0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
          compiler.getLinkedList().getLast().addLabel(t);

    }

    /**
    *  Generate code for a And expression in a declaration for example, in a method.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param init is used to know if we are in an initialization case.
    *
    *
    */
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
          Label f = new Label("False_And." + nbAnd );
          Label t = new Label("True_And." + nbAnd);
          nbAnd++;

          this.getLeftOperand().codeGenExprMethod(compiler, init);
          compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new BNE(f));
          this.getRightOperand().codeGenExprMethod(compiler, init);
          compiler.addInstruction(new CMP(new ImmediateInteger(1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.addInstruction(new BNE(f));
          compiler.addInstruction(new LOAD(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
          compiler.addInstruction(new BRA(t));//pour éviter de load 0
          compiler.getLinkedList().getLast().addLabel(f); //on store false
          compiler.addInstruction(new LOAD(0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()) ));
          compiler.getLinkedList().getLast().addLabel(t);

    }

}
