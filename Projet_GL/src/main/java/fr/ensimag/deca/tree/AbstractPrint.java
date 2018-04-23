package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl17
 * @date 01/01/2018
 */
  public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();

    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        for (AbstractExpr argument : getArguments().getList()){
          Type argumentType = argument.verifyExpr(compiler, localEnv, currentClass);
          if (!(argumentType.isFloat() || argumentType.isInt() || argumentType.isString())){
            throw new ContextualError("Wrong type of argument: Argument must be of type int, float or string", getLocation());
          }
        }
    }

    /**
     *  Generate code for a Print instruction in a main program.
     *    The different arguments are printed at the same time.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     *
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, printHex);
        }
    }


    /**
     *  Generate code for a Print instruction in a method.
     *    The different arguments are printed at the same time.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     *
     */
    protected void codeGenInstMethod(DecacCompiler compiler){
      for (AbstractExpr a : getArguments().getList()) {
          a.codeGenPrintMethod(compiler, printHex);
      }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if(this.getSuffix().equals("")){
            if(printHex==true){
              s.print("printx(");
            }
            else{
              s.print("print(");
            }
        }
        else if(this.getSuffix().equals("ln")){
            if(printHex==true){
                s.print("printlnx(");
            }
            else{
                s.print("println(");
            }
        }
        arguments.decompile(s);
        s.println(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }



}
