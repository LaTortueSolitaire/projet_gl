package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *	Binary comparison operations
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type lType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type rType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (lType.sameType(rType)) {
    		if (!rType.isFloat() && !rType.isInt() && (this instanceof AbstractOpIneq)){
    			throw new ContextualError("Unsupported types for this operation: Operands must be of type float or int", getLocation());
    		}
    	}
    	else {
    		if (rType.isFloat() && lType.isInt()) {
    			ConvFloat newLOp = new ConvFloat(this.getLeftOperand());
    			this.setLeftOperand(newLOp);
    			this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    		}
    		else if (rType.isInt() && lType.isFloat()) {
    			ConvFloat newROp = new ConvFloat(this.getRightOperand());
    			this.setRightOperand(newROp);
    			this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    		}
    		else {
    			throw new ContextualError("Unsupported types for this operation: Operands must either be of same type or be of type float or int", getLocation());
    		}
    	}
    	Type t = new BooleanType(compiler.getSymbolTable().create("boolean"));
    	this.setType(t);
    	return t;
    }

    protected abstract void codeGenCompare(DecacCompiler compiler, Label deb, Label fin);
    protected abstract void codeGenCompareMethod(DecacCompiler compiler, Label end, Label fin);

}
