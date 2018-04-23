package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Main block of a Deca program.
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractMain extends Tree {

    protected abstract void codeGenMain(DecacCompiler compiler);


/**
 * Implements non-terminal "main" of [SyntaxeContextuelle] in pass 3 
 *
 * @param compiler  (contains the "env_types" attribute)
 * @throws ContextualError if contextual syntax isn't respected
 */

    protected abstract void verifyMain(DecacCompiler compiler) throws ContextualError;
}
