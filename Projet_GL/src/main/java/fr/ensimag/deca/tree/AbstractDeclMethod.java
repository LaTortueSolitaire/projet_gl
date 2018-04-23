package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import java.util.ArrayList;

/**
 * Class declaration.
 *
 * @author gl17 Anais
 * @date 01/01/2018
 */
public abstract class AbstractDeclMethod extends Tree {

	 protected AbstractIdentifier type;
	 protected AbstractIdentifier methodName;
	 protected ListDeclParam listParam;

	 public AbstractIdentifier getMethodName() {
		return methodName;
	 }
	 
	 /**
	     * Verify the members of new declared method
	     *
	     * implements non-terminal "decl_method"
	     *    of [SyntaxeContextuelle] in pass 2
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
	 protected void verifyMethodMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
          throws ContextualError{
		 //On récupère la MethodDefinition de la classe parente
		 ExpDefinition superIdDef = currentClass.getSuperClass().getMembers().get(this.methodName.getName());
		 if (superIdDef != null && !superIdDef.isMethod()) {
			 throw new ContextualError("Method already declared as field in super class", this.getLocation());
		 }
		 MethodDefinition parentMethodDefinition = (MethodDefinition) currentClass.getSuperClass().getMembers().get(this.methodName.getName());
		 Type methodType = this.type.verifyType(compiler);
		 Signature methodSig = this.listParam.verifyListDeclParamMembers(compiler);
		 //Création de l'index de la méthode,
		 int methodIndex;

		 if (parentMethodDefinition != null) {
			 methodIndex = parentMethodDefinition.getIndex();
			 if(!(methodSig.equals(parentMethodDefinition.getSignature()))){
				 throw new ContextualError("Overriden method must have the same signature as parent method", this.getLocation());
			 }
			 else if (!AbstractExpr.subtype(compiler, methodType, parentMethodDefinition.getType())){
				 throw new ContextualError("Returned type of overriden method must be a subtype of the parent method return type", this.getLocation());
			 }
		 }
		 else {
			 methodIndex = currentClass.incNumberOfMethods();
		 }
		 
		 MethodDefinition methodDefinition = new MethodDefinition(methodType, this.getLocation(), methodSig, methodIndex);

		 String methodLabelStr = "code." + currentClass.getType().getName().getName() + "." + this.methodName.getName();
		 Label methodLabel = new Label(methodLabelStr);
		 methodDefinition.setLabel(methodLabel);
		 try {
			 currentClass.getMembers().declare(this.methodName.getName(), methodDefinition);
		 }
		 catch(EnvironmentExp.DoubleDefException e) {
			 ExpDefinition def = currentClass.getMembers().get(this.methodName.getName());
			 if (def.isField()) {
			 	throw new ContextualError("Identifier already declared as field in current class", this.getLocation());
			 }
			 else if (def.isMethod()) {
				 throw new ContextualError("Method already in current class", this.getLocation());
			 }
		 }
  	this.methodName.verifyExpr(compiler, currentClass.getMembers(), currentClass);
}



  protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
          throws ContextualError;


  protected abstract void codeGenDeclMethod(DecacCompiler compiler);
}
