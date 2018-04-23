package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.deca.context.Type;
/**
 * Class declaration.
 *
 * @author gl17 Anais
 * @date 01/01/2018
 */
public abstract class AbstractFields extends Tree {
	
/**
 * Verify the field members of a class for contextual error
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
 * @param t
 * 			Type of the field  
 * @param v
 * 			Visibility of the field
 * @throws ContextualError if contextual syntax isn't respected
 */ 

  protected abstract void verifyFieldsMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type t, Visibility v)
          throws ContextualError;
  
/**
 * Verify the field body of a class for contextual error
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
 * @param t
 * 			 Type of the field
 * @throws ContextualError if contextual syntax isn't respected
 */
  
  protected abstract void verifyFieldsBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type t)
          throws ContextualError;

  protected abstract void codeGenField(DecacCompiler compiler);

  public abstract AbstractIdentifier getNameIdent();


}
