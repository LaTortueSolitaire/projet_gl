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
 *	Boolean binary operations (&&, ||)
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type lType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type rType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

    	if (!lType.isBoolean() || !rType.isBoolean()) {
    		throw new ContextualError("Unsupported types for this operation: Operands must be of type boolean", this.getLocation());
    	}
    	this.setType(rType);
    	return rType;
    }


    protected abstract void codeGenCompare(DecacCompiler compiler, Label deb, Label end);
    protected abstract void codeGenCompareMethod(DecacCompiler compiler, Label end, Label fin);


}
