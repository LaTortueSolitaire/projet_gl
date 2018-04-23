package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.RegisterManager;


/**
 *	Method call expression ($object.$method(*$args))
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class DotMethod extends AbstractExpr {

    final private AbstractExpr object;
    final private AbstractIdentifier methodIdent;
    final private ListExpr parameters;

    public DotMethod(AbstractExpr object, AbstractIdentifier methodIdent, ListExpr parameters) {
        Validate.notNull(object);
        Validate.notNull(methodIdent);
        Validate.notNull(parameters);
        this.object = object;
        this.methodIdent = methodIdent;
        this.parameters = parameters;
    }
    public DotMethod(Location loc, AbstractIdentifier method, ListExpr params) {
          Validate.notNull(method);
          Validate.notNull(params);

          this.object = new This();
          object.setLocation(loc);

          this.methodIdent = method;
          this.parameters = params;
  }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type exprType = this.object.verifyExpr(compiler, localEnv, currentClass);

    	if (!exprType.isClass()){
    		throw new ContextualError("Unsupported type for this selection: Left operand must be a class instance", this.getLocation());
    	}
    	ClassDefinition classDefinition = (ClassDefinition) compiler.getEnvType().get(compiler.getSymbolTable().create(exprType.getName().getName()));
      ClassType classType = classDefinition.getType();
    	ExpDefinition expDef = classDefinition.getMembers().get(this.methodIdent.getName());
    	if (expDef == null || !expDef.isMethod()) {
    		throw new ContextualError("Method '" + this.methodIdent.getName() + "' is unknown to class " + classType.getName(), this.getLocation());
    	}
    	MethodDefinition methodDef = (MethodDefinition) expDef;
    	Type methodIdentType = this.methodIdent.verifyExpr(compiler, classDefinition.getMembers(), currentClass);
    	this.parameters.verrifyAsParams(compiler, localEnv, currentClass, methodDef.getSignature(), this.getLocation());

    	this.setType(methodIdentType);
    	return methodIdentType;
    }

    @Override
    String prettyPrintNode() {
        return "MethodCall (" + object.prettyPrintNode() + "," + methodIdent.prettyPrintNode() + ")" ;
    }

    @Override
    public void decompile(IndentPrintStream s) {
      object.decompile(s);
      s.print(".");
      methodIdent.decompile(s);
      s.print("(");
      parameters.decompile(s);
      s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //TODO
    }

    /**
      *  Generate code to call a method in a main program.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      *
      */
    @Override
    protected void codeGenInst(DecacCompiler compiler){
      int numberParam =  this.parameters.getList().size();
      Label dereferencement = new Label("dereferencement.null");
    /*  if (this.object instanceof This){
        compiler.addInstruction(new LOAD(,Register.R0));
      }*/
      compiler.addInstruction(new ADDSP(numberParam+1));
      compiler.getLinkedList().getLast().addComment("empile objet");
      if (!(this.object instanceof This)){
        this.object.codeGenExpr(compiler, true);
      }
      else{
        //le this est implicite, on met l'adresse de l'objet (-2LB) dans un registre
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      }
      compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), new RegisterOffset(0, Register.SP)));

      compiler.getLinkedList().getLast().addComment("empile paramaters");
      int i = 1;
      for (AbstractExpr a : this.parameters.getList()){
        a.codeGenExprMethod(compiler, true);
        compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), new RegisterOffset(-i, Register.SP)));
        i = i +1;
      }
      compiler.getLinkedList().getLast().addComment("appel de méthode");

      compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), GPRegister.getR(2)));
      compiler.addInstruction(new CMP(new NullOperand(), GPRegister.getR(2)));
      compiler.addInstruction(new BEQ(dereferencement));
      compiler.addInstruction(new LOAD(new RegisterOffset(0, GPRegister.getR(2)), GPRegister.getR(2)));
      compiler.addInstruction(new BSR(new RegisterOffset(this.methodIdent.getMethodDefinition().getIndex(), GPRegister.getR(2))));
      compiler.addInstruction(new SUBSP(numberParam +1));


    }

    /**
      *  Generate code to call a method in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      *
      */
    @Override
    protected void codeGenInstMethod(DecacCompiler compiler){
      int numberParam =  this.parameters.getList().size();
      Label dereferencement = new Label("dereferencement.null");
    /*  if (this.object instanceof This){
        compiler.addInstruction(new LOAD(,Register.R0));
      }*/
      compiler.addInstruction(new ADDSP(numberParam+1));
      compiler.getLinkedList().getLast().addComment("empile objet");
      if (!(this.object instanceof This)){
        this.object.codeGenExprMethod(compiler, true);
      }
      else{
        //le this est implicite, on met l'adresse de l'objet (-2LB) dans un registre
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      }
      compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), new RegisterOffset(0, Register.SP)));

      compiler.getLinkedList().getLast().addComment("empile paramaters");
      int i = 1;
      for (AbstractExpr a : this.parameters.getList()){
        a.codeGenExprMethod(compiler, true);
        compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), new RegisterOffset(-i, Register.SP)));
        i = i +1;
      }
      compiler.getLinkedList().getLast().addComment("appel de méthode");

      compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), GPRegister.getR(2)));
      compiler.addInstruction(new CMP(new NullOperand(), GPRegister.getR(2)));
      compiler.addInstruction(new BEQ(dereferencement));
      compiler.addInstruction(new LOAD(new RegisterOffset(0, GPRegister.getR(2)), GPRegister.getR(2)));
      compiler.addInstruction(new BSR(new RegisterOffset(this.methodIdent.getMethodDefinition().getIndex(), GPRegister.getR(2))));
      compiler.addInstruction(new SUBSP(numberParam +1));
    }

    /**
     *  Generate code for a comparison that has a DotMethod in his expression.
     *  The comparison is in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param deb is the label we have to jump if the result of the comparison is true
     *
     * @param end is the label we have to jump if the result of the comparison is false
     *
     */
    /*utile pour la comparaison entre le retour d'une méthode et autre chose*/
    @Override
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end){
      this.codeGenExprMethod(compiler, false); //on veut qu'il incrémente car l'autre comparaison aura besoin d'un autre registregit
    }

    /**
     *  Generate code for a comparison that has a DotMethod in his expression.
     *  The comparison is in the main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param deb is the label we have to jump if the result of the comparison is true
     *
     * @param end is the label we have to jump if the result of the comparison is false
     *
     */
    /*utile pour la comparaison entre le retour d'une méthode et autre chose*/
    @Override
    protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end){
      this.codeGenExpr(compiler, false); //rajouter pendant la doc
    }


    /**
      *  Generate code when there is a DotMethod in an expression, in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      //avant chaque appel de méthode je push le dernier registre
      this.codeGenInst(compiler); //appel de méthode
      //car le résultat d'un return est stocké dans R0
      compiler.addInstruction(new LOAD(Register.R0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      if (!init){
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
    }

    /**
      *  Generate code when there is a DotMethod in an expression, in the main program.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */
    /*Pour un assign */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
      this.codeGenInst(compiler); //appel de méthode
    //  compiler.addInstruction(new BRA(nameMethod));
      //car le résultat d'un return est stocké dans R0
      compiler.addInstruction(new LOAD(Register.R0, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      if (!init){
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      object.prettyPrint(s, prefix, false);
      methodIdent.prettyPrint(s, prefix, false);
      parameters.prettyPrint(s, prefix, true);
    }

    /**
     *  Generate code to display the value returned by a function in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    /*pour afficher le retour d'une methode*/
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex){
      this.codeGenExpr(compiler, false); //le résultat de l'opération se trouve dans le registre courant
      compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.R1));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      Type type = this.methodIdent.getMethodDefinition().getType();
      if (type.isInt()){
          compiler.addInstruction(new WINT());
      }
      else if (type.isFloat()){
        if (!printHex){
          compiler.addInstruction(new WFLOAT());
        }
        else{
          compiler.addInstruction(new WFLOATX());
        }
      }
    }

    /**
     *  Generate code to display the value returned by a function in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    /*pour afficher le retour d'une methode*/
    protected void codeGenPrintMethod(DecacCompiler compiler, boolean printHex){ //rajouter pendant la doc
      this.codeGenExprMethod(compiler, false);
      compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.R1));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      Type type = this.methodIdent.getMethodDefinition().getType();
      if (type.isInt()){
          compiler.addInstruction(new WINT());
      }
      else if (type.isFloat()){
        if (!printHex){
          compiler.addInstruction(new WFLOAT());
        }
        else{
          compiler.addInstruction(new WFLOATX());
        }
      }
    }

}
