package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }


    protected abstract String getOperatorName();

    @Override
    public void decompile(IndentPrintStream s) {
        if(this.getOperatorName().equals("!") || this.getOperatorName().equals("-")){
            if(this.getOperatorName().equals("!")){
                s.print("!");
            }
            else{
                s.print("-");
            }
            operand.decompile(s);
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }
    
    protected  abstract void codeGenExpr(DecacCompiler compiler, boolean init);

}
