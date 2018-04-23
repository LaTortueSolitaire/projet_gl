package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.tree.*;
/**
 * Variable initialization : Variable declaration with value assignment
 * @author gl17
 * @date 01/01/2018
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

    	this.setExpression(this.expression.verifyRValue(compiler, localEnv, currentClass, t));

    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        expression.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }

    /**
     *  Generate code for the initialization of a variable in a main program
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    protected void codeGenInit(DecacCompiler compiler){
      this.expression.codeGenExpr(compiler, true);
    }

    /**
     *  Generate code for the initialization of a variable in a method
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    protected void codeGenInitMethod(DecacCompiler compiler){
      this.expression.codeGenExprMethod(compiler, true);
    }


}
