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
 * If statement
 *
 * @author gl17
 * @date 01/01/2018
 */
public class If extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;

    public If(AbstractExpr condition, ListInst thenBranch){
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
      condition.verifyCondition(compiler, localEnv, currentClass);
      thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    /*@Override
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
        condition.iter(f);
        thenBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
    }
}
