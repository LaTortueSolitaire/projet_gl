package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;

/**
 *	"This" identifier to manipulate the object inside the body of its methods
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class This extends AbstractExpr {


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (currentClass == null) {
        	throw new ContextualError("Cannot use \"this\" identifier outside of a method", this.getLocation());
        }
        this.setType(currentClass.getType());
        return (Type) currentClass.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
      s.print("this");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "This";
    }

    protected void codeGenCompareMethod(DecacCompiler compiler,Label deb,  Label end){
        this.codeGenExprMethod(compiler, false);
    }

    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
        if(!init) {
            compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      	}
    }
}
