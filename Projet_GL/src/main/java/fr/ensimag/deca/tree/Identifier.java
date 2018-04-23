package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.context.TypeDefinition;
import java.util.AbstractMap;

/**
 * Deca Identifier
 *
 * @author gl17
 * @date 01/01/2018
 */
public class Identifier extends AbstractIdentifier {

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ParamDefinition getParamDefinition() {
        try {
            return (ParamDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Param identifier, you can't call getParamDefinition on it");
        }
    }


    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        ExpDefinition exp = localEnv.get(this.getName());
        if (exp == null) {
            throw new ContextualError("Undefined identifier : " + this.name.getName(), this.getLocation());
        }
        this.setType(exp.getType());
        this.setDefinition(exp);
        return exp.getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        TypeDefinition t = compiler.getEnvType().get(this.getName());
        if (t == null) {
          throw new ContextualError("Undefined type : " + this.name.getName(), this.getLocation());
        }
        this.setType(t.getType());
        this.setDefinition(t);
        return t.getType();
    }


    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }


/**
  *  Generate code to display the value of an identifier in a main program.
  *
  * @param compiler  (contains the "env_types" attribute)
  *
  * @param printHex is used to know if the value must be printed in hexadecimal.
  *       If true, the value is displayed in hexadecimal.
  *
  */
  @Override
  protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
      compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), GPRegister.R1));
    Type type = this.definition.getType();
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
    *  Generate code to display the value of an identifier in a method.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param printHex is used to know if the value must be printed in hexadecimal.
    *       If true, the value is displayed in hexadecimal.
    *
    */
  @Override
  protected void codeGenPrintMethod(DecacCompiler compiler, boolean printHex){
    Definition def = this.getDefinition();
    if (def.isField()){
      int index = this.getFieldDefinition().getIndex();
      compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));// Pour afficher un champs dan le print dans une methode
      compiler.addInstruction(new LOAD(new RegisterOffset(index, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())), GPRegister.R1));
    }
    else if (def.isParam()){
      int index = this.getParamDefinition().getIndex();
      compiler.addInstruction(new LOAD(new RegisterOffset(-index-2,Register.LB ), GPRegister.R1));
    }
    else{ //c'est une variable locale
      compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), GPRegister.R1));
    }
    Type type = this.definition.getType();
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
    *  Generate code to store the value of an identifier in a main program.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    *
    */
  @Override
  protected void codeGenInst(DecacCompiler compiler){
    compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), this.getExpDefinition().getOperand()));
  }

  /**
    *  Generate code to store the value of an identifier in a method.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    *
    */
  @Override
  protected void codeGenInstMethod(DecacCompiler compiler){
    compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    Definition def = this.getDefinition();
    if(def.isField()){
        int index = this.getFieldDefinition().getIndex();
        compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), new RegisterOffset(index, GPRegister.getR(0))));
    }
    else if (def.isParam()){
      int index = this.getParamDefinition().getIndex();
      compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), new RegisterOffset(-index-2, Register.LB)));

    }
    else { //variable locale
        compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), this.getExpDefinition().getOperand()));
    }
  }

  /**
   *  Generate code for a comparison that has an indentifier in his expression.
   *  The comparison is in the main program.
   *
   * @param compiler  (contains the "env_types" attribute)
   *
   * @param deb is the label we have to jump if the result of the comparison is true
   *
   * @param end is the label we have to jump if the result of the comparison is false
   *
   */
  @Override
  protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end) {
    Type typer = this.definition.getType();
	  if (typer.isBoolean()){ //pour gérer le cas d'un b booléen
		  compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
  	  compiler.addInstruction(new BNE(end));
	  }
	  else {
		  this.codeGenExpr(compiler, false);
	  }
  }


  /**
   *  Generate code for a comparison that has an indentifier in his expression.
   *  The comparison is in a method.
   *
   * @param compiler  (contains the "env_types" attribute)
   *
   * @param deb is the label we have to jump if the result of the comparison is true
   *
   * @param end is the label we have to jump if the result of the comparison is false
   *
   */
  @Override
  protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end) {
    Type typer = this.definition.getType();
    Definition def = this.getDefinition();
    if(def.isField()){
      int index = this.getFieldDefinition().getIndex();
      compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.addInstruction(new LOAD(new RegisterOffset(index, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())),  GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      if (typer.isBoolean()){ //pour gérer le cas d'un b booléen
          compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
          compiler.addInstruction(new BNE(end));
      }
    }
    else if (def.isParam()){
        int index = this.getParamDefinition().getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(-index -2, Register.LB), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
        if (typer.isBoolean()){ //pour gérer le cas d'un b booléen
            int index2 = this.getParamDefinition().getIndex();
            compiler.addInstruction(new LOAD(new RegisterOffset(-index2 -2, Register.LB), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
            compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
            compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
            compiler.addInstruction(new BNE(end));
        }
    }
    else{
        if (typer.isBoolean()){ //pour gérer le cas d'un b booléen
		      compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
          compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
  	      compiler.addInstruction(new BNE(end));
	      }
	      else {
		         this.codeGenExprMethod(compiler, false);
	      }
    }
  }

  /**
    *  Generate code for an identifier in an expression, in a main program.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param init is used to know if we are in an initialization case.
    *           If true, we don't need to increment the current register.
    *
    */
  @Override
  protected void codeGenExpr(DecacCompiler compiler, boolean init){
    compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    if (!init) {
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
  	}
  }

  /**
    *  Generate code for an identifier in an expression, in a method.
    *
    * @param compiler  (contains the "env_types" attribute)
    *
    * @param init is used to know if we are in an initialization case.
    *           If true, we don't need to increment the current register.
    *
    */
  @Override
  protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
    Definition def = this.getDefinition();
    if (def.isField()){ //l'identifier est un attribut
      int index = this.getFieldDefinition().getIndex();
      compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
      compiler.addInstruction(new LOAD(new RegisterOffset(index, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())),  GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    }
    else if(def.isParam()){
      int index = this.getParamDefinition().getIndex();
      compiler.addInstruction(new LOAD(new RegisterOffset(-index-2, Register.LB),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    }
    else { //l'identifier est une variable locale à la méthode
      compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
    }
    if (!init){
      compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
    }
  }

  /**
   *  Generate code for an identifier that needs to be convert into a float.
   *      The conversion is happening in a main program.
   *
   * @param compiler  (contains the "env_types" attribute)
   *
   * @param init is used to know if we are in an initialization case.
   *     If true, we don't need to increment the current register.
   *
   */
  protected void codeGenExprConv(DecacCompiler compiler, boolean init) { //pour gérer la conversion d'un identifier en flottant
      compiler.addInstruction(new FLOAT(this.getExpDefinition().getOperand(), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()))) ;
      if(!init){
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
  }

  /**
   *  Generate code for an identifier that needs to be convert into a float.
   *      The conversion is happening in a method.
   *
   * @param compiler  (contains the "env_types" attribute)
   *
   * @param init is used to know if we are in an initialization case.
   *     If true, we don't need to increment the current register.
   *
   */
  protected void codeGenExprConvMethod(DecacCompiler compiler, boolean init ) {
    Definition def = this.getDefinition();
    if (def.isField()){ //l'identifier est un attribut
      int index = this.getFieldDefinition().getIndex(); //pour gérer la conversion d'un identifier en flottant
      compiler.addInstruction(new FLOAT(new RegisterOffset(index, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()))) ;
  }
  else if(def.isParam()){
    int index = this.getParamDefinition().getIndex();
    compiler.addInstruction(new FLOAT(new RegisterOffset(-index-2, Register.LB),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
  }
  else { //l'identifier est une variable locale à la méthode
    compiler.addInstruction(new FLOAT(this.getExpDefinition().getOperand(),GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
  }
  if(!init){
      compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
  }}

}
