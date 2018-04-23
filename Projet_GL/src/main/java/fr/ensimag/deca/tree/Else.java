package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 * Else statement.
 *
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class Else extends AbstractInst {

    private final ListInst thenBranch;
//    private final AbstractExpr conditionElseIf;


    public Else(ListInst thenBranch){
        Validate.notNull(thenBranch);
        this.thenBranch = thenBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
      thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

  /*  @Override
    protected void codeGenInst(DecacCompiler compiler) {
        thenBranch.codeGenListInst(compiler);
    }

    @Override
    protected void codeGenInstMethod(DecacCompiler compiler) {
        thenBranch.codeGenListInstMethod(compiler);
    }*/

    @Override
    public void decompile(IndentPrintStream s) {
        thenBranch.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        thenBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        thenBranch.prettyPrint(s, prefix, false);
    }
}
