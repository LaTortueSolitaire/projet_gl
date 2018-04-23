package fr.ensimag.deca.tree;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;


/**
 * read...() statement.
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    protected abstract void codeGenExpr(DecacCompiler compiler, boolean init);
    protected abstract void codeGenExprMethod(DecacCompiler compiler, boolean init);
}
