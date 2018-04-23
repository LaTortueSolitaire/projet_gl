package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

/**
 *	Declaration of method parameters
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {


	/**
     * Implements non-terminal "list_decl_param" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains "env_types" attribute
     * @throws ContextualError if contextual syntax isn't respected
     */
	 public Signature verifyListDeclParamMembers(DecacCompiler compiler) throws ContextualError {
	    	Signature sig = new Signature();
	    	for (AbstractDeclParam param : getList()) {
	    		sig.add(param.verifyParamMembers(compiler));
	    	}
	    	return sig;
	    }

	/**
	 * Implements non-terminal "list_decl_param" of [SyntaxeContextuelle] in pass 3
	 * @param compiler contains "env_types" attribute
	 * @param localEnv corresponds to "env_exp" attribute
	 * @param currentClass
	 *          corresponds to "class" attribute (null in the main bloc).
	 * @throws ContextualError if contextual syntax isn't respected
	 */

	public void verifyListDeclParamBody(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass)
            throws ContextualError {
		int index = 1;
    	for (AbstractDeclParam param : getList()) {
    		param.verifyParamBody(compiler, localEnv, currentClass, index);
    		index++;
    	}
    }

    /*public void codeGenListParam(DecacCompiler compiler) {
    }*/

    @Override
    public void decompile(IndentPrintStream s) {
      int virgule = 0;
      for (AbstractDeclParam p : getList()) {
        if (virgule > 0){
          s.print(",");
        }
          p.decompile(s);
          virgule = virgule +1;
      }

    }
}
