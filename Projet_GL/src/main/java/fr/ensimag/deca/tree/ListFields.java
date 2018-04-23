package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *	Declaration of fields inside a class
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class ListFields extends TreeList<AbstractFields> {

    /**
     * Implements non-terminal "list_decl_field" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     * @param t
     *          corresponds to field type
     * @param v
     * 			corresponds to the field visibility
     * @throws ContextualError if contextual syntax isn't respected
     */
     protected void verifyListFieldsMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type t, Visibility v)
             throws ContextualError
 {
         for (AbstractFields df : this.getList()) {
       	  df.verifyFieldsMembers(compiler, localEnv, currentClass, t, v);
         }
    }

     /**
      * Implements non-terminal "list_decl_field" of [SyntaxeContextuelle] in pass 2
      * @param compiler contains "env_types" attribute
      * @param localEnv corresponds to "env_exp" attribute
      * @param currentClass
      *          corresponds to "class" attribute (null in the main bloc).
      * @param t
      *          corresponds to field type
      * @throws ContextualError if contextual syntax isn't respected
      */
     protected void verifyListFieldsBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type t)
             throws ContextualError{
    	 for (AbstractFields df : this.getList()) {
          	  df.verifyFieldsBody(compiler, localEnv, currentClass, t);
    	 }
     }

    @Override
    public void decompile(IndentPrintStream s) {
      for (AbstractFields f : getList()) {
          f.decompile(s);
      }

      }

  /**
    *  Generate code for all the fields contains in a class
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    */
    // @Override
    protected void codeGenListField(DecacCompiler compiler) {
    	int i = 1;
    	for (AbstractFields f : getList()) {
            f.codeGenField(compiler);
          //  GPRegister R1 = GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant());
            compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1),new RegisterOffset(f.getNameIdent().getFieldDefinition().getIndex(),GPRegister.R1)));
            i = i + 1;
            compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    	}
    }
}
