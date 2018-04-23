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
 * Full if/else if/else statement.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private ListInst elseBranch;
    private static int nbr_if = 0;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public void addElseIfBranch(AbstractInst elseif){
      this.elseBranch.add(elseif);
    }

    public void addElseBranch(ListInst elseB){
      for(AbstractInst i : elseB.getList()){
        this.elseBranch.add(i);
      }
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
      condition.verifyCondition(compiler, localEnv, currentClass);
      thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
      elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    /**
     *  Generate code for the instruction IfThenElse in a main program
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
      Label eFinIf = new Label("E_Fin_If." + nbr_if);
      Label eCondIf = new Label("E_Cond_If." + nbr_if);
      Label eSinonIf = new Label("E_Sinon_If." + nbr_if);
      Label eDebutIf = new Label("E_Debut_If." + nbr_if);

      nbr_if++;

      compiler.addInstruction(new BRA(eCondIf));

      compiler.getLinkedList().getLast().addLabel(eDebutIf);
      thenBranch.codeGenListInst(compiler);
      compiler.addInstruction(new BRA(eFinIf));

      compiler.getLinkedList().getLast().addLabel(eSinonIf);
      elseBranch.codeGenListInst(compiler);
      compiler.addInstruction(new BRA(eFinIf));

      compiler.getLinkedList().getLast().addLabel(eCondIf);
      this.condition.codeGenCompare(compiler, eDebutIf, eSinonIf);
      compiler.addInstruction(new BRA(eDebutIf));

      compiler.getLinkedList().getLast().addLabel(eFinIf);
    }

    /**
     *  Generate code for the instruction IfThenElse in a method
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    protected void codeGenInstMethod(DecacCompiler compiler) {
      Label eFinIf = new Label("E_Fin_If_Method." + nbr_if);
      Label eCondIf = new Label("E_Cond_If_Method." + nbr_if);
      Label eSinonIf = new Label("E_Sinon_If_Method." + nbr_if);
      Label eDebutIf = new Label("E_Debut_If_Method." + nbr_if);

      nbr_if++;

      compiler.addInstruction(new BRA(eCondIf));

      compiler.getLinkedList().getLast().addLabel(eDebutIf);
      thenBranch.codeGenListInstMethod(compiler);
      compiler.addInstruction(new BRA(eFinIf));

      compiler.getLinkedList().getLast().addLabel(eSinonIf);
      elseBranch.codeGenListInstMethod(compiler);
      compiler.addInstruction(new BRA(eFinIf));

      compiler.getLinkedList().getLast().addLabel(eCondIf);
      this.condition.codeGenCompareMethod(compiler, eDebutIf, eSinonIf);
      compiler.addInstruction(new BRA(eDebutIf));

      compiler.getLinkedList().getLast().addLabel(eFinIf);
    }

    @Override
    public void decompile(IndentPrintStream s) {
	     Else branche_else = new Else(elseBranch);
       If branche_if = new If(condition, thenBranch);
       s.print("if (");
       condition.decompile(s);
       s.println(") {");
       s.indent();
       branche_if.decompile(s);
       s.unindent();
       s.println("}");
       s.println("else{");
       s.indent();
       branche_else.decompile(s);
       s.unindent();
       s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
