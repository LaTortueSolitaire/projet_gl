package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type typeGauche = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

    	this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, typeGauche));
    	this.setType(typeGauche);
    	return typeGauche;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    /**
      *  Generate code to assign a value to another one in a main program.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      *
      */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
      this.getRightOperand().codeGenExpr(compiler, false); //on met dans R2 la bonne valeur à assigner
      this.getLeftOperand().codeGenInst(compiler); //je store
    }

    /**
      *  Generate code to assign a value to another one in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      *
      */
    @Override
    protected void codeGenInstMethod(DecacCompiler compiler){
      Definition def = this.getLeftOperand().getDefinition();
      if (def.isField()){
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.getR(0)));
      }
      int toPOP = compiler.getCompilerOptions().getRegisterManager().getRegisterCourant();
      if (this.getRightOperand() instanceof DotMethod){
        compiler.addInstruction(new PUSH(GPRegister.getR(toPOP)));
      }
      this.getRightOperand().codeGenExprMethod(compiler, false);
      this.getLeftOperand().codeGenInstMethod(compiler);
      if (this.getRightOperand() instanceof DotMethod){
        compiler.addInstruction(new POP(GPRegister.getR(toPOP)));
      }
    }
}
