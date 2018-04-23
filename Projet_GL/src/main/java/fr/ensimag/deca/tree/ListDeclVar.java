package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import java.util.*;

/**
 * List of declarations (e.g. int x; float y,z).
 *
 * @author gl17
 * @date 01/01/2018
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {



    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclVar i : getList()){
          i.decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to
     *      the "env_exp_r" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     */
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
              for (AbstractDeclVar dc : this.getList()) {
                dc.verifyDeclVar(compiler, localEnv, currentClass);
              }
    }

    /**
     *  Generate code for the declaration of every variable in a program or method
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param register
     *          is Register.LB if the function is used to generate code in a method
     *          is Register.GB if the function is used to generate code in a main program
     * @param boolean
     *         true if the function is used to generate code in a method
     */
    public void codeGenListDeclVar(DecacCompiler compiler, Register register, boolean method) {
        for (AbstractDeclVar i : getList()) {
            i.codeGenDeclVar(compiler, register, method);
        }
    }



}
