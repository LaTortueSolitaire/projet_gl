package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.util.ArrayList;


/**
 * Declaration of a method
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class DeclMethod extends AbstractDeclMethod {


	final private ListDeclVar methodDeclVar;
    final private ListInst methodBody;


    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam listParam, ListDeclVar methodDeclVar, ListInst methodBody) {
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(listParam);
        this.type = type;
        this.methodName = methodName;
        this.listParam = listParam;
        this.methodDeclVar = methodDeclVar;
        this.methodBody = methodBody;
    }

    /**
     * Verify the members of new declared method
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
    	Type returnType = this.type.getType();
    	this.listParam.verifyListDeclParamBody(compiler, methodEnv, currentClass);
    	this.methodDeclVar.verifyListDeclVariable(compiler, methodEnv, currentClass);
    	this.methodBody.verifyListInst(compiler, methodEnv, currentClass, returnType);

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
          listParam.prettyPrint(s, prefix, false);
          methodDeclVar.prettyPrint(s, prefix, false);
          methodBody.prettyPrint(s, prefix, true);
            }

    @Override
    public void decompile(IndentPrintStream s) {
      type.decompile(s);
      s.print(" ");
      methodName.decompile(s);
      s.print("(");
      listParam.decompile(s);
      s.println("){");
      methodDeclVar.decompile(s);
      methodBody.decompile(s);
      s.print("}");
      }

		/**
	    *  Generate code for the body of the current method
	    *
	    * @param compiler  (contains the "env_types" attribute)
	    *
	   */
    protected void codeGenDeclMethod(DecacCompiler compiler){
			compiler.getCompilerOptions().getRegisterManager().setRegDebut();
			compiler.getCompilerOptions().getRegisterManager().setVarLocale();
			compiler.incrementNombreMethod();
      compiler.getLinkedList().getLast().addComment("Code de la méthode " + this.methodName.getName().getName());
      Label code = this.methodName.getMethodDefinition().getLabel();
      compiler.getLinkedList().getLast().addLabel(code);
			IMAProgram lastOne = new IMAProgram();
			compiler.getLinkedList().addLast(lastOne);
    //  compiler.resetCompteurLB(); //on remet à 2
      compiler.setCompteurParam(this.listParam.getList().size());
			compiler.getLinkedList().getLast().addComment("Début des déclarations de variables");
      this.methodDeclVar.codeGenListDeclVar(compiler, Register.LB, true);//ici on doit passé LB et compteurLB
			compiler.getLinkedList().getLast().addComment("Début de la liste des instructions");
      this.methodBody.codeGenListInstMethod(compiler);

			if(!this.type.getDefinition().getType().isVoid()){
					compiler.addInstruction(new WSTR("Error : Method "+this.methodName.getName().getName()+" expects a non-void returned expression"));
					compiler.addInstruction(new WNL());
					compiler.addInstruction(new ERROR());
			}

			Label finMethod = new Label("fin_method."+ compiler.getNombreMethod());
			compiler.getLinkedList().getLast().addLabel(finMethod);

			// Label de fin de method a mettre
			int v = 0;

			int i;
			// int j = compiler.getCompilerOptions().getRegisterManager().getRegFinBloc();
			int w  = 15;
			for(i = 2; i <= 15; i++){
					compiler.addInstruction(new POP(GPRegister.getR(w)));
					compiler.getLinkedList().getLast().addFirst(new PUSH(GPRegister.getR(w)));
					w--;
					v++;
			}
			//compiler.getLinkedList().getLast().addFirst(new ADDSP(compiler.getCompilerOptions().getRegisterManager().getVarLocale()));
			Label stack = new Label("stack_overflow_error");
			if(!compiler.getCompilerOptions().getNoCheck()){
				compiler.getLinkedList().getLast().addFirst(new BOV(stack));
			}
			compiler.getLinkedList().getLast().addFirst(new TSTO(v + compiler.getCompilerOptions().getRegisterManager().getVarLocale() +( compiler.getCompilerOptions().getRegisterManager().getRegFinBloc()-compiler.getCompilerOptions().getRegisterManager().getRegDebutBloc())+ this.listParam.getList().size()));
			// compiler.getLinkedList().getLast().addFirst(new TSTO(0));
			compiler.addInstruction(new RTS());
		}

}
