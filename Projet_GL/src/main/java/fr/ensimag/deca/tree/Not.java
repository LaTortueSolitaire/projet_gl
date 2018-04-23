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
 *	Logical "not" unary operation (!$boolean)
 * @author gl17
 * @date 01/01/2018
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type opType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!opType.isBoolean()) {
        	throw new ContextualError("Unsupported type for this operation: Operand must be of type boolean", this.getLocation());
        }
        this.setType(opType);
        return opType;
    }


    /**
     *  Generate code for a Not expression, declaration or assignation in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *
     *
     */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init) {
    	this.getOperand().codeGenExpr(compiler, init);
    	compiler.addInstruction(new CMP(0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    	compiler.addInstruction(new SEQ(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    }

    /**
     *  Generate code for a Not expression, declaration or assignation in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *
     *
     */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init) {
    	this.getOperand().codeGenExprMethod(compiler, init);
    	compiler.addInstruction(new CMP(0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    	compiler.addInstruction(new SEQ(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    }

    /**
     *  Generate code for a comparison that has a Not in his expression.
     *        The comparison is in the main program.
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
    	this.codeGenExpr(compiler, true);
    	compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    	compiler.addInstruction(new BNE(end)); //si c'est false on va à la fin
    }

    /**
     *  Generate code for a comparison that has a Not in his expression.
     *        The comparison is in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param deb is the label we have to jump if the result of the comparison is true
     *
     * @param end is the label we have to jump if the result of the comparison is false
     *
     */
    @Override
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end) {
    	this.codeGenExprMethod(compiler, true);
    	compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    	compiler.addInstruction(new BNE(end)); //si c'est false on va à la fin
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
