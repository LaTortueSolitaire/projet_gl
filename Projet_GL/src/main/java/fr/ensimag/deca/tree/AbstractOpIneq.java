package fr.ensimag.deca.tree;


/**
 *	Inequality binary operations (<, <=, >, >=)
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


}
