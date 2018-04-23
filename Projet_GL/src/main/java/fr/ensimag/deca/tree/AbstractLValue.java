package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.*;
/**
 * Left-hand side value of an assignment.
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractLValue extends AbstractExpr {
  public abstract FieldDefinition getFieldDefinition();
  public abstract Definition getDefinition();

}
