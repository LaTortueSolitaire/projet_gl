package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;


import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
/**
 * Class declaration.
 *
 * @author gl17 Anais
 * @date 01/01/2018
 */
public abstract class AbstractFieldSet extends Tree {
	
/**
 * Verify the fields set members of a class for contextual error
 *
 * partially implements non-terminal "decl_field"
 *    of [SyntaxeContextuelle] in pass 2
 *
 * @param compiler  (contains the "env_types" attribute)
 * @param localEnv
 *            Environment in which the expression should be checked
 *            (corresponds to the "env_exp" attribute)
 * @param currentClass
 *            Definition of the class containing the expression
 *            (corresponds to the "class" attribute)
 *             is null in the main bloc.
 * @throws ContextualError if contextual syntax isn't respected
 */

  protected abstract void verifyFieldSetMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
          throws ContextualError;
  
/**
 * Verify the fields set members of a class for contextual error
 *
 * partially implements non-terminal "decl_field"
 *    of [SyntaxeContextuelle] in pass 3
 *
 * @param compiler  (contains the "env_types" attribute)
 * @param localEnv
 *            Environment in which the expression should be checked
 *            (corresponds to the "env_exp" attribute)
 * @param currentClass
 *            Definition of the class containing the expression
 *            (corresponds to the "class" attribute)
 *             is null in the main bloc.
 * @throws ContextualError if contextual syntax isn't respected
 */
  
  protected abstract void verifyFieldSetBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
          throws ContextualError;

  protected abstract void codeGenFieldSet(DecacCompiler compiler);
}
