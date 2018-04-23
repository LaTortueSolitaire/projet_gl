package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tree.Cast;
import fr.ensimag.deca.tree.ConvFloat;

/**
 *	Return instruction in a method
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class Return extends AbstractInst {
    private AbstractExpr returned;


    public AbstractExpr getReturn() {
        return returned;
    }


    public Return(AbstractExpr returned) {
        Validate.notNull(returned);
        this.returned = returned;
    }

    /**
     *  Generate code for the instruction return.
     *    The value that must be returned is always load in the register R0.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    protected void codeGenInstMethod(DecacCompiler compiler) {
      Label finMethod = new Label("fin_method."+compiler.getNombreMethod());
        this.returned.codeGenExprMethod(compiler, true);
        compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.R0));
        compiler.addInstruction(new BRA(finMethod));

    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

    		this.returned = this.returned.verifyRValue(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        returned.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returned.iter(f);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      returned.prettyPrint(s, prefix, true);
    }

}
