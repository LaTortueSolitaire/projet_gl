package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 *	Exact binary comparison operations (==, !=)
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type lType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type rType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
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
    	Type t = new BooleanType(compiler.getSymbolTable().create("boolean"));
    	this.setType(t);
    	return t;
    }


}
