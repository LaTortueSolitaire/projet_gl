package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Declaration of method with assembly langage body
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class DeclAsmMethod extends AbstractDeclMethod{

    final private String stringLiteral;

    public DeclAsmMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam listParam, String string) {
        Validate.notNull(string);
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(listParam);
        this.type = type;
        this.methodName = methodName;
        this.listParam = listParam;
        this.stringLiteral = string;


    }

    /**
     * Verify the members of new asm declared method
     *
     * implements non-terminal "decl_method"
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @throws ContextualError if contextual syntax isn't respected
     */
    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError{
    	EnvironmentExp methodEnv = new EnvironmentExp(localEnv);
    	this.listParam.verifyListDeclParamBody(compiler, methodEnv, currentClass);

    }


    @Override
    protected void iterChildren(TreeFunction f) {
                type.iter(f);
                //TODO
            }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      type.prettyPrint(s, prefix, false);
      methodName.prettyPrint(s, prefix, false);
      listParam.prettyPrint(s, prefix, true);
     }

    @Override
    public void decompile(IndentPrintStream s) {
      type.decompile(s);
      s.print(" ");
      methodName.decompile(s);
      s.print("(");
      listParam.decompile(s);
      s.print(") asm(");
      s.print(stringLiteral);
      s.println(");");
      }

      /**
	     *  Generate code for the body of the current asm method
	     *
	     * @param compiler  (contains the "env_types" attribute)
	     *
	     */
     protected void codeGenDeclMethod(DecacCompiler compiler){
       compiler.getLinkedList().getLast().addComment("Code de la méthode asm " + this.methodName.getName().getName());
       Label code = this.methodName.getMethodDefinition().getLabel();
       compiler.getLinkedList().getLast().addLabel(code);
       compiler.setCompteurParam(this.listParam.getList().size());
       String asm = this.stringLiteral.substring(1, this.stringLiteral.length() -1);
       compiler.getLinkedList().getLast().add(new InlinePortion(asm));
     }
}
