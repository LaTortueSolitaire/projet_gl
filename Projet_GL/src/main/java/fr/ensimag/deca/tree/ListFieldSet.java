package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *	Declaration of fields type and visibility inside a class
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class ListFieldSet extends TreeList<AbstractFieldSet> {

    /**
     * Partially implements non-terminal "list_decl_field" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     * @throws ContextualError if contextual syntax isn't respected
     */
     protected void verifyListFieldSetMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
             throws ContextualError{
    	 for (AbstractFieldSet fs : this.getList()) {
    		 fs.verifyFieldSetMembers(compiler, localEnv, currentClass);
    	 }
    }

     /**
      * Partially implements non-terminal "list_decl_field" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
      * @param localEnv corresponds to "env_exp" attribute
      * @param currentClass
      *          corresponds to "class" attribute (null in the main bloc).
      * @throws ContextualError if contextual syntax isn't respected
      */
     protected void verifyListFieldSetBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
             throws ContextualError{
    	 for (AbstractFieldSet fs : this.getList()) {
    		 fs.verifyFieldSetBody(compiler, localEnv, currentClass);
    	 }
     }

    @Override
    public void decompile(IndentPrintStream s) {
      for (AbstractFieldSet f : getList()) {
          f.decompile(s);
        //  s.println();
      }

      }


    /**
    *  Generate code for the initialization of fields of a class
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    */
    public void codeGenListFieldSet(DecacCompiler compiler) {
    	for (AbstractFieldSet f : getList()) {
    		f.codeGenFieldSet(compiler);
    	}
    }

}
