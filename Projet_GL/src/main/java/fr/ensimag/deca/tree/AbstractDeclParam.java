package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Instruction
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractDeclParam extends Tree {


    protected abstract Type verifyParamMembers(DecacCompiler compiler) throws ContextualError;

    protected abstract void verifyParamBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, int index) throws ContextualError;

    /**
     * Generate assembly code for the instruction.
     *
     * @param compiler
     */



}
