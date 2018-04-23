package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
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
import fr.ensimag.deca.context.VariableDefinition;

/**
 * Declaration of a variable
 * @author gl17
 * @date 01/01/2018
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }


    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
              Type t = this.type.verifyType(compiler);
              if (t.isVoid()) {
            	  throw new ContextualError("Variable cannot be declared with void type", this.getLocation());
              }
              this.initialization.verifyInitialization(compiler, t, localEnv, currentClass);
              try {
                localEnv.declare(this.varName.getName(), new VariableDefinition(t, getLocation()));
                this.varName.verifyExpr(compiler, localEnv, currentClass);

              } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError("Variable is already declared", this.getLocation());
              }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }


    /**
     *  Generate code for the declaration of the current variable
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param register
     *          is Register.LB if the function is used to generate code in a method
     *          is Register.GB if the function is used to generate code in a main program
     * @param boolean
     *         true if the function is used to generate code in a method
     */
    @Override
    public void codeGenDeclVar(DecacCompiler compiler, Register register, boolean method){
      Type t = varName.getType();
      int compteur;
      if (method){
        compteur = compiler.getCompteurLB();
      }
      else{
        compteur = compiler.getCompteur(); //GB
      }
      this.varName.getExpDefinition().setOperand(new RegisterOffset(compteur,register));
      if (this.initialization instanceof NoInitialization){
        this.initialization.codeGenInit(compiler, t);
      }
      else{
        if (!method){
          this.initialization.codeGenInit(compiler);
        }
        else{
          this.initialization.codeGenInitMethod(compiler);
        }
      }
      compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), this.varName.getExpDefinition().getOperand()));
      if (method){
        compiler.incrementCompteurLB();
        compiler.getCompilerOptions().getRegisterManager().incrementVarLocale();

      }
      else{
        compiler.incrementCompteur();
        compiler.getCompilerOptions().getRegisterManager().incrementVarGlobale();
      }
    }
}
