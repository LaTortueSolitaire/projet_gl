package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
/**
 * Variable declaration
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractDeclVar extends Tree {

    /**
     * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to
     *      the synthetized attribute
     * @param currentClass
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

      public abstract void codeGenDeclVar(DecacCompiler compiler,  Register register, boolean method);
      //protected abstract void codeGenDeclVarMethod(DecacCompiler compiler);
}
