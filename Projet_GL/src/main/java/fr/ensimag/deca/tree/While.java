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
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *	While instruction
 * @author gl17
 * @date 01/01/2018
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;
    private static int numberOfWhile = 0;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public int getNumberOfWhile(){
      return this.numberOfWhile;
    }

    public void incrementWhile(){
      this.numberOfWhile ++;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    /**
     *  Generate code for the instruction while in a main program
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label eFin = new Label("E_Fin." + this.getNumberOfWhile());
         Label eCond = new Label("E_Cond." + this.getNumberOfWhile());
         Label eDeb = new Label("E_Debut." + this.getNumberOfWhile());
         compiler.addInstruction(new BRA(eCond));
         compiler.getLinkedList().getLast().addLabel(eDeb);
         this.body.codeGenListInst(compiler);
         compiler.addInstruction(new BRA(eCond));
         compiler.getLinkedList().getLast().addLabel(eCond);
         this.condition.codeGenCompare(compiler, eDeb, eFin);
         compiler.addInstruction(new BRA(eDeb));
         compiler.getLinkedList().getLast().addLabel(eFin);
         this.incrementWhile();
    }

    /**
     *  Generate code for the instruction while in a method
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    protected void codeGenInstMethod(DecacCompiler compiler) {
        Label eFin = new Label("E_Fin_Method." + this.getNumberOfWhile());
         Label eCond = new Label("E_Cond_Method." + this.getNumberOfWhile());
         Label eDeb = new Label("E_Debut_Method." + this.getNumberOfWhile());
         compiler.addInstruction(new BRA(eCond));
         compiler.getLinkedList().getLast().addLabel(eDeb);
         this.body.codeGenListInstMethod(compiler);
         compiler.addInstruction(new BRA(eCond));
         compiler.getLinkedList().getLast().addLabel(eCond);
         this.condition.codeGenCompareMethod(compiler, eDeb, eFin);
         compiler.addInstruction(new BRA(eDeb));
         compiler.getLinkedList().getLast().addLabel(eFin);
         this.incrementWhile();
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
      condition.verifyCondition(compiler, localEnv, currentClass);
      body.verifyListInst(compiler, localEnv, currentClass, returnType);

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
