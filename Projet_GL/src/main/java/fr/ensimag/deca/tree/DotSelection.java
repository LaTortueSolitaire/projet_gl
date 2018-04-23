package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
/**
 *	Field call expression ($object.$field)
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class DotSelection extends AbstractLValue {

    final private AbstractExpr object;
    final private AbstractIdentifier fieldIdent;

    public DotSelection(AbstractExpr object, AbstractIdentifier fieldIdent) {
        Validate.notNull(object);
        Validate.notNull(fieldIdent);
        this.object = object;
        this.fieldIdent = fieldIdent;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type objType = this.object.verifyExpr(compiler, localEnv, currentClass);
    	ClassType objClassType = objType.asClassType("Unsupported type for this selection: Left operand must be a class instance", this.getLocation());

    	Type identType = this.fieldIdent.verifyExpr(compiler, objClassType.getDefinition().getMembers(), currentClass);

    	ClassDefinition classDefinition = objClassType.getDefinition();
    	ExpDefinition identDefinition =  classDefinition.getMembers().get(fieldIdent.getName());

    	if (!identDefinition.isField()) {
    		throw new ContextualError("Unsupported operand for this selection: Right operand must be a field", this.getLocation());
    	}

    	FieldDefinition fieldDefinition = (FieldDefinition) identDefinition;
    	if (fieldDefinition.getVisibility() == Visibility.PROTECTED && (
    			currentClass == null || !AbstractExpr.subtype(compiler, objClassType, currentClass.getType())
    			)) {
    		throw new ContextualError("Forbidden access to protected field", this.getLocation());
    	}
    	this.setType(identType);
    	return identType;
    }

    @Override
    String prettyPrintNode() {
        return "Selection Attribut" ;
    }

    @Override
    public void decompile(IndentPrintStream s) {
      object.decompile(s);
      s.print(".");
      fieldIdent.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //TODOfieldIdent;
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      object.prettyPrint(s, prefix, false);
      fieldIdent.prettyPrint(s, prefix, true);
    }
    @Override
    public FieldDefinition getFieldDefinition(){
        return this.fieldIdent.getFieldDefinition(); //A MODIFIER !!! j'ai juste mis ça pour que ça compile
    }

    @Override
    public Definition getDefinition(){
      return this.fieldIdent.getDefinition(); //A MODIFIER !!! j'ai just emis ça pour que ça compile
    }

    /**
     *  Generate code to display the value of a DotSelection in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex){
        this.object.codeGenExpr(compiler, true);
        int indexAttribut = this.fieldIdent.getFieldDefinition().getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(indexAttribut, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())), GPRegister.R1));
        Type type = this.fieldIdent.getDefinition().getType();
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
     *  Generate code to display the value of a DotSelection in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param printHex is used to know if the value must be printed in hexadecimal.
     *       If true, the value is displayed in hexadecimal.
     *
     */
    @Override
    protected void codeGenPrintMethod(DecacCompiler compiler, boolean printHex){
      this.object.codeGenExprMethod(compiler, true);
      int indexAttribut = this.fieldIdent.getFieldDefinition().getIndex();
      compiler.addInstruction(new LOAD(new RegisterOffset(indexAttribut, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())), GPRegister.R1));
      Type type = this.fieldIdent.getDefinition().getType();
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
      *  Generate code when there is a DotSelection in an expression, in a main program.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */
    @Override
    protected void codeGenExpr(DecacCompiler compiler, boolean init){
      int indexAttribut = this.fieldIdent.getFieldDefinition().getIndex();
      //On veut load dans le registre courant la valeur de identType
      int rCourant = compiler.getCompilerOptions().getRegisterManager().getRegisterCourant();
      compiler.addInstruction(new LOAD(new RegisterOffset(indexAttribut, GPRegister.R1), GPRegister.getR(rCourant)));
      if (!init){
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
    }

    /**
      *  Generate code when there is a DotSelection in an expression, in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      * @param init is used to know if we are in an initialization case.
      *           If true, we don't need to increment the current register.
      *
      */
    @Override
    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
      this.fieldIdent.codeGenExprMethod(compiler, init);
    }

    /**
     *  Generate code for a DotSelection that needs to be convert into a float.
     *      The conversion is happening in a main program.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprConv(DecacCompiler compiler, boolean init){
      this.codeGenExpr(compiler, true); //met this.... dans le registre courant et incrément
      compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()))) ;
      if(!init){
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
    }

    /**
     *  Generate code for a DotSelection that needs to be convert into a float.
     *      The conversion is happening in a method.
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     * @param init is used to know if we are in an initialization case.
     *     If true, we don't need to increment the current register.
     *
     */
    @Override
    protected void codeGenExprConvMethod(DecacCompiler compiler, boolean init){ //rajouté pendant la doc
      this.codeGenExprMethod(compiler, true);
      compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()))) ;
      if(!init){
          compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
      }
    }

    /**
     *  Generate code for a comparison that has a DotSelection in his expression.
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
    protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end){
        this.object.codeGenExpr(compiler, true);
        int index = this.fieldIdent.getFieldDefinition().getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(index, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
        Type type = this.fieldIdent.getFieldDefinition().getType();
        if (type.isBoolean()){
          compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
          compiler.addInstruction(new BNE(end));
        }
    }

    /**
     *  Generate code for a comparison that has a DotSelection in his expression.
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
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end){
        this.object.codeGenExprMethod(compiler, true);
        int index = this.fieldIdent.getFieldDefinition().getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(index, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
        compiler.getCompilerOptions().getRegisterManager().incrementCompteurCourant();
        Type type = this.fieldIdent.getFieldDefinition().getType();
        if (type.isBoolean()){
          compiler.addInstruction(new CMP(1, GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
          compiler.addInstruction(new BNE(end));
        }
    }

    /**
      *  Generate code to store the value of a DotSelection in a method.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      *
      */
    @Override
    protected void codeGenInstMethod(DecacCompiler compiler){
      int indexAttribut = this.fieldIdent.getFieldDefinition().getIndex();
      compiler.addInstruction(new STORE(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), new RegisterOffset(indexAttribut, GPRegister.R0)));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
    }
}
