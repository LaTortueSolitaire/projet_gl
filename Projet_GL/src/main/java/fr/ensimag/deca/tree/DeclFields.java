package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.context.FieldDefinition;

/**
 * Declaration of class fields
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class DeclFields extends AbstractFields {


    final private AbstractIdentifier name;
    final private AbstractInitialization initialization;

    public DeclFields(AbstractIdentifier name, AbstractInitialization initialization) {
        Validate.notNull(name);
        this.name = name;
        this.initialization = initialization;

    }

    public AbstractIdentifier getNameIdent(){
        return this.name;
    }

    @Override
    protected void verifyFieldsMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type t, Visibility v)
            throws ContextualError{
    		  currentClass.incNumberOfFields();
    		  FieldDefinition def = new FieldDefinition(t, this.getLocation(), v, currentClass, currentClass.getNumberOfFields());
    		  ExpDefinition superIdDef = currentClass.getSuperClass().getMembers().get(this.name.getName());
    		  if (superIdDef != null && !superIdDef.isField()) {
    			  throw new ContextualError("Field already declared as method in super class", this.getLocation());
    		  }
    		  try {
            	  currentClass.getMembers().declare(this.name.getName(), def);
              } catch(EnvironmentExp.DoubleDefException e) {
            	  ExpDefinition ddef = currentClass.getMembers().get(this.name.getName());
     			 if (ddef.isField()) {
     			 	throw new ContextualError("Field already declared in current class", this.getLocation());
     			 }
     			 else if (ddef.isMethod()) {
     				 throw new ContextualError("Identifier already declared as method in current class", this.getLocation());
     			 }
              }
              this.name.verifyExpr(compiler, currentClass.getMembers(), currentClass);
            }

    @Override
    protected void verifyFieldsBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type t)
            throws ContextualError{

    	this.initialization.verifyInitialization(compiler, t, localEnv, currentClass);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
                name.iter(f);
                //TODO
            }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
          name.prettyPrint(s, prefix, false);
          initialization.prettyPrint(s, prefix, true);
            }

    @Override
    public void decompile(IndentPrintStream s) {
      name.decompile(s);
      initialization.decompile(s);
      s.println(";");
      }


    /**
      *  Generate code for the current field in a class
      *
      * @param compiler  (contains the "env_types" attribute)
      *
    */
    @Override
    /* déclaration pour un seul champ*/
    protected void codeGenField(DecacCompiler compiler) {
      compiler.getLinkedList().getLast().addComment("Initialisation de " + this.name.getName().getName());
    	if (this.initialization instanceof NoInitialization) {
    		this.initialization.codeGenInit(compiler, this.name.getType());
    	}
    	else {
    		this.initialization.codeGenInit(compiler);
    	}
      compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant(); //car l'initialisation n'a pas fait augmenter le compteur
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.R1));
    }
}
