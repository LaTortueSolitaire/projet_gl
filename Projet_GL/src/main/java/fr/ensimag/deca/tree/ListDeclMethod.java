package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;

/**
 *	Declaration of methods inside a class
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    /**
     * Implements non-terminal "list_decl_method" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     * @throws ContextualError if contextual syntax isn't respected
     */
	protected void verifyListMethodMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
   	 for (AbstractDeclMethod method : getList()) {
   		 method.verifyMethodMembers(compiler, localEnv, currentClass);
   	 }
   }

	/**
     * Implements non-terminal "list_decl_method" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     * @throws ContextualError if contextual syntax isn't respected
     */
	protected void verifyListMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
             throws ContextualError {
    	 for (AbstractDeclMethod method : getList()) {
    		 method.verifyMethodBody(compiler, localEnv, currentClass);
    	 }
    }

    @Override
    public void decompile(IndentPrintStream s) {
      for (AbstractDeclMethod m : getList()) {
          m.decompile(s);
          s.println();
      	}
      }

/**
	*  Generate code for a list of method in a class.
	*
	* @param compiler  (contains the "env_types" attribute)
	*
	*/
		protected void codeGenListDeclMethod(DecacCompiler compiler){
			for (AbstractDeclMethod m : getList()) {
					IMAProgram lastOne = new IMAProgram();
					compiler.getLinkedList().addLast(lastOne);
					m.codeGenDeclMethod(compiler);
				}
		}
}
