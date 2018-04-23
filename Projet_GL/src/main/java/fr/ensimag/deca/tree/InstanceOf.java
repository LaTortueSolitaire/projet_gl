package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Instanceof operation
 *
 * @author gl17
 * @date 01/01/2018
 */
public class InstanceOf extends AbstractExpr {


    final private AbstractExpr expression;
    final private AbstractIdentifier ident;

    public InstanceOf(AbstractExpr expression, AbstractIdentifier ident) {
        this.expression = expression;
        this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type expType = this.expression.verifyExpr(compiler, localEnv, currentClass);
        Type idType = this.ident.verifyType(compiler);
        if (!idType.isClass()) {
        	throw new ContextualError("Unsupported type for this operation : Cannot use instanceof on a litteral", this.getLocation());        	
        }
        Type booleanType = new BooleanType(compiler.getSymbolTable().create("boolean"));
    	this.setType(booleanType);
        return booleanType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
      expression.decompile(s);
      s.print(" instanceof ");
      ident.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
      expression.iterChildren(f);
      ident.iterChildren(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      expression.prettyPrint(s, prefix, false);
      ident.prettyPrint(s, prefix, true);
    }

}
