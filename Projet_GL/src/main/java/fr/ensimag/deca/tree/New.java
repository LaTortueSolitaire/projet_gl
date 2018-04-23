package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.context.*;

/**
 * Construction of a new class instance
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class New extends AbstractExpr {


    final private AbstractIdentifier id;


    public New(AbstractIdentifier id) {
        this.id = id;

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type idType = id.verifyType(compiler);
    	if (!idType.isClass()) {
    		throw new ContextualError("Cannot declare a new instance with litteral type", this.getLocation());
    	}
    	this.setType(idType);
    	return idType;
    }




    @Override
    public void decompile(IndentPrintStream s) {
      s.print("new ");
      id.decompile(s);
      s.print("()");

    }

    /**
     *  Generate code for a "new" expression in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *      Actually, the function is always called with init = true.
     *
     */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
      int numberField = this.id.getClassDefinition().getNumberOfFields();
      compiler.addInstruction(new NEW(numberField+1, GPRegister.getR(2)));
      Label heap = new Label("heap_overflow_error");
      if(!compiler.getCompilerOptions().getNoCheck()){
        compiler.addInstruction(new BOV(heap));
      }
      compiler.addInstruction(new LEA(this.id.getClassDefinition().getOperand(), Register.R0));
      compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, GPRegister.getR(2))));
      compiler.addInstruction(new PUSH(GPRegister.getR(2)));
      if (!init){
      compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }//avant été commenté
      Label initLabel = new Label("init." + this.id.getName().getName());
      compiler.addInstruction(new BSR(initLabel));
      compiler.addInstruction(new POP(GPRegister.getR(2)));
    }

    /**
     *  Generate code for a "new" expression in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *      Actually, the function is always called with init = true.
     *
     */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      int numberField = this.id.getClassDefinition().getNumberOfFields();
      compiler.addInstruction(new NEW(numberField+1, GPRegister.getR(2)));
      Label heap = new Label("heap_overflow_error");
      if(!compiler.getCompilerOptions().getNoCheck()){
        compiler.addInstruction(new BOV(heap));
      }
      compiler.addInstruction(new LEA(this.id.getClassDefinition().getOperand(), Register.R0));
      compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, GPRegister.getR(2))));
      compiler.addInstruction(new PUSH(GPRegister.getR(2)));
      if (!init){
      compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }//avant été commenté
      Label initLabel = new Label("init." + this.id.getName().getName());
      compiler.addInstruction(new BSR(initLabel));
      compiler.addInstruction(new POP(GPRegister.getR(2)));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
      id.iterChildren(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      id.prettyPrint(s, prefix, true);
    }

}
