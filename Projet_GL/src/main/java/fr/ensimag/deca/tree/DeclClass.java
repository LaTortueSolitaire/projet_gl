package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.DecacInternalError;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl17
 * @date 01/01/2018
 */
public class DeclClass extends AbstractDeclClass {

   private AbstractIdentifier name;
    private AbstractIdentifier nameExtends;
    private ListDeclMethod method;
    private ListFieldSet fields;


    public DeclClass(AbstractIdentifier name, AbstractIdentifier nameExtends, ListDeclMethod method, ListFieldSet fields) {
        Validate.notNull(name);
        this.name = name;
        this.nameExtends= nameExtends;
        this.method = method;
        this.fields = fields;
    }

    public ListDeclMethod getMethod() {
    	return this.method;
    }

    public ListFieldSet getFields() {
    	return this.fields;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        if (nameExtends.getName().getName() != "Object"){
          s.print(" extends ");
          nameExtends.decompile(s);
        }
        s.println(" {");
        s.println();
        fields.decompile(s);
        s.println();
        method.decompile(s);
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
    	Type extendType = this.nameExtends.verifyType(compiler);
    	if (!extendType.isClass()) {
    		throw new ContextualError("Class cannot inherit from litteral type", this.getLocation());
    	}
    	ClassType typ = new ClassType(this.name.getName(), this.getLocation(), (ClassDefinition) compiler.getEnvType().get(nameExtends.getName()));
    	this.name.setDefinition(typ.getDefinition());
    	try {
    		compiler.getEnvType().declare(this.name.getName(), this.name.getClassDefinition());
    	} catch (EnvironmentType.DoubleDefException e) {
    		throw new ContextualError("Class already defined", this.getLocation());
    	}
    	this.name.verifyType(compiler);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
    	int numberOfFields = this.nameExtends.getClassDefinition().getNumberOfFields();
    	this.name.getClassDefinition().setNumberOfFields(numberOfFields);
    	int numberOfMethods	 = this.nameExtends.getClassDefinition().getNumberOfMethods();
    	this.name.getClassDefinition().setNumberOfMethods(numberOfMethods);

    	this.getFields().verifyListFieldSetMembers(compiler, this.nameExtends.getClassDefinition().getMembers(), this.name.getClassDefinition());
    	this.getMethod().verifyListMethodMembers(compiler, this.nameExtends.getClassDefinition().getMembers(), this.name.getClassDefinition());

//// TEST POUR VERIFIER LA BONNE MAJ D'INDEX ET DE LABEL
//       	for (AbstractDeclMethod absMeth : this.method.getList()) {
//       		DeclMethod meth = (DeclMethod) absMeth;
//       		MethodDefinition methDef = (MethodDefinition) this.name.getClassDefinition().getMembers().get(meth.getMethodName().getName());
//       		System.out.println(this.name.getName().getName()+" : "+methDef.getLabel().getName() + ", index : " + methDef.getIndex());
//       	}
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
    	try {
    		this.getMethod().verifyListMethodBody(compiler, name.getClassDefinition().getMembers(), name.getClassDefinition());
    		this.getFields().verifyListFieldSetBody(compiler, name.getClassDefinition().getMembers(), name.getClassDefinition());
    	}
    	catch (DecacInternalError e){
    		throw new ContextualError("Lilali", this.getLocation());
    	}
    }


    @Override
    protected void iterChildren(TreeFunction f) {
      //  throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        nameExtends.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, true);
    }

    /**
     *  Generate the label table of the class
     *    and then the code for the method table of the class
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    protected void codeGenPreDeclClass(DecacCompiler compiler) {
    	this.genTabEtiquette();
    	this.codeGenPremierePasse(compiler); //construction de la table des méthodes
    	// this.codeGenDeuxiemePasse(compiler);
    }

    /**
     *  Generate code for the initialization of field
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
    	   this.codeGenInitField(compiler);
    }


    /**
     *  Generate code for the method table
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    protected void codeGenPremierePasse(DecacCompiler compiler) {
    	compiler.getLinkedList().getLast().addComment("Code de la table des méthodes de " + this.name.getName().getName());
      if (this.nameExtends.getName().toString().equals("Object")){ //la classe est une classe mère
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), Register.R0));
      }
      else{ //la classe a une super classe différentes de Object
        int numberMethodParentClass = this.nameExtends.getClassDefinition().getNumberOfMethods();
        compiler.addInstruction(new LEA(new RegisterOffset(compiler.getCompteur() - numberMethodParentClass -1, Register.GB), Register.R0));
      }
    	compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getCompteur(), Register.GB)));
    	this.name.getClassDefinition().setOperand(new RegisterOffset(compiler.getCompteur(),Register.GB));
    	compiler.incrementCompteur();
      compiler.getCompilerOptions().getRegisterManager().incrementVarGlobale();
    	//on ajoute les méthodes de la classe parente
      for(int i = 0; i < this.name.getClassDefinition().getTableEtiquette().size(); i++){
        compiler.addInstruction(new LOAD(new LabelOperand(this.name.getClassDefinition().getLabelTable(i)), GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(compiler.getCompteur(), Register.GB)));
        compiler.incrementCompteur();
        compiler.getCompilerOptions().getRegisterManager().incrementVarGlobale();
      }
    }

    /**
     *  Generate code for the initialization of fields
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    protected void codeGenInitField(DecacCompiler compiler) {
    	Label init = new Label("init." + this.name.getName().getName());
    	compiler.getLinkedList().getLast().addComment("Initialisation des champs de " + this.name.getName().getName());
    	compiler.getLinkedList().getLast().addLabel(init);
      IMAProgram lastOne = new IMAProgram();
      compiler.getLinkedList().addLast(lastOne);
      compiler.getCompilerOptions().getRegisterManager().setRegDebut();
			compiler.getCompilerOptions().getRegisterManager().setVarLocale();
    	if (!this.nameExtends.getName().toString().equals("Object")){
    		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
    		compiler.addInstruction(new PUSH(Register.R0)); //sauvegarde du registre
    		Label classMere = new Label("init." + this.nameExtends.getName().getName()); //initialisation des attributs de la classe mère
    		compiler.addInstruction(new BSR(classMere));
    		compiler.addInstruction(new SUBSP(1)); //on redescend la pile
    		}
    	this.fields.codeGenListFieldSet(compiler); //et on initialise les attributs non hérités de la classe


      int v = 0;
      int i;
			int j = compiler.getCompilerOptions().getRegisterManager().getRegFinBloc();
			int w  = j;
			for(i = compiler.getCompilerOptions().getRegisterManager().getRegDebutBloc(); i <= j; i++){
					compiler.addInstruction(new POP(GPRegister.getR(i)));
					compiler.getLinkedList().getLast().addFirst(new PUSH(GPRegister.getR(w)));
					w--;
					v++;
			}
			// compiler.getLinkedList().getLast().addFirst(new ADDSP(compiler.getCompilerOptions().getRegisterManager().getVarLocale()));
			Label stack = new Label("stack_overflow_error");
      if(!compiler.getCompilerOptions().getNoCheck()){
        compiler.getLinkedList().getLast().addFirst(new BOV(stack));
      }
			compiler.getLinkedList().getLast().addFirst(new TSTO(v + compiler.getCompilerOptions().getRegisterManager().getVarLocale() +( compiler.getCompilerOptions().getRegisterManager().getRegFinBloc()-compiler.getCompilerOptions().getRegisterManager().getRegDebutBloc())));
      // compiler.getLinkedList().getLast().addFirst(new TSTO(0));
      compiler.addInstruction(new RTS());
    }


    /**
     *  Generate the label table of the current class
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    @Override
    /* retourne un arrayList contenant toute les étiquettes des méthodes*/
    protected void genTabEtiquette() {
      boolean labelAdd = false;
      int compteur = 1;
    	Label object = new Label("code.Object.equals");
      this.name.getClassDefinition().ajoutLabel(object, 0);
      ArrayList<Label> tableEtiquetteSuperClass = this.nameExtends.getClassDefinition().getTableEtiquette();
      for(int i = 1; i < tableEtiquetteSuperClass.size(); i++){
        this.name.getClassDefinition().ajoutLabel(tableEtiquetteSuperClass.get(i), i);
        compteur++;
      }
      for (AbstractDeclMethod m : this.method.getList()){
        labelAdd = false;
        Label method = m.getMethodName().getMethodDefinition().getLabel();
        String methodS = method.getName();
        String[] methodName = methodS.split("\\.");
        for (int i = 1; i< tableEtiquetteSuperClass.size(); i++){
          String s =  tableEtiquetteSuperClass.get(i).getName();
          String[] methodSuperClass = s.split("\\.");
          if (methodName[2].equals(methodSuperClass[2])){
              this.name.getClassDefinition().getTableEtiquette().set(i, method);
              labelAdd = true;
          }
        }
        if(!labelAdd){
          this.name.getClassDefinition().ajoutLabel(method, compteur);
          compteur++;
        }
      }

    }

    /**
     *  Generate code for all the method of the class
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    protected void codeGenDeclClassMethod(DecacCompiler compiler){
      this.method.codeGenListDeclMethod(compiler);
    }


}
