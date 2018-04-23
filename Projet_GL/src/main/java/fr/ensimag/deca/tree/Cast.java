package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;

/**
*	Implicit and explicit cast expression
* @author gl17
* @date 13/01/2018
*/

public class Cast extends AbstractExpr {

	private AbstractExpr exp;
	private AbstractIdentifier id;

	public Cast(AbstractExpr exp, AbstractIdentifier id) {
		this.exp = exp;
		this.id = id;
	}

	public AbstractExpr getExp() {
		return exp;
	}

	public AbstractIdentifier getId() {
		return id;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type idType = this.getId().verifyType(compiler);
		Type expType = this.getExp().verifyExpr(compiler, localEnv, currentClass);
		if (!correctCast(compiler, expType, idType)) {
			throw new ContextualError("Incompatible type to cast", this.getLocation());
		}

		this.setType(idType);
		return idType;
	}

	public boolean correctCast(DecacCompiler compiler, Type typeToCast, Type castType) {
		return !typeToCast.isVoid() && (
				(typeToCast.isInt() && castType.isFloat()) || AbstractExpr.subtype(compiler, typeToCast, castType) ||
				(typeToCast.isFloat() && castType.isInt()) || AbstractExpr.subtype(compiler, castType, typeToCast)
				);
	}

	@Override
	public void decompile(IndentPrintStream s) {
			s.print("(");
			id.decompile(s);
			s.print(") ");
			s.print("(");
			exp.decompile(s);
			s.print(")");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
			exp.prettyPrint(s, prefix, false);
			id.prettyPrint(s, prefix, true);

	}

	@Override
	protected void iterChildren(TreeFunction f) {
		exp.iterChildren(f);
		id.iterChildren(f);

	}

	protected void codeGenPrint(DecacCompiler compiler, boolean printHex){
			this.codeGenExpr(compiler, false);
			compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.R1));
      compiler.getCompilerOptions().getRegisterManager().decrementRegisterCourant();
      if (this.id.getType().isInt()){
        compiler.addInstruction(new WINT());
      }
      else if (this.id.getType().isFloat()){
				if (!printHex){
					compiler.addInstruction(new WFLOAT());
				}
				else{
					compiler.addInstruction(new WFLOATX());
				}
      }

	}

	protected void codeGenExpr(DecacCompiler compiler, boolean init){
			if(! this.id.getType().sameType(this.exp.getType())){
					if(this.id.getType().isInt()){
							this.exp.codeGenExpr(compiler, init);
							if(init){
									compiler.addInstruction(new INT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
							}
							else{
								compiler.addInstruction(new INT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
							}
					}
					else if(this.id.getType().isFloat()){
							this.exp.codeGenExpr(compiler, init);
							if(init){
									compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
							}
							else{
								compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
							}
					}
					else {
							throw new UnsupportedOperationException("not yet implemented the cast of a class");
					}
			}
			else{
					this.exp.codeGenExpr(compiler, init);
			}
	}

	protected void codeGenExprMethod(DecacCompiler compiler, boolean init){
		if(! this.id.getType().sameType(this.exp.getType())){
				if(this.id.getType().isInt()){
						this.exp.codeGenExprMethod(compiler, init);
						if(init){
								compiler.addInstruction(new INT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
						}
						else{
							compiler.addInstruction(new INT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
						}
				}
				else if(this.id.getType().isFloat()){
						this.exp.codeGenExprMethod(compiler, init);
						if(init){
								compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant())));
						}
						else{
							compiler.addInstruction(new FLOAT(GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1), GPRegister.getR(compiler.getCompilerOptions().getRegisterManager().getRegisterCourant()-1)));
						}
				}
				else {
						throw new UnsupportedOperationException("not yet implemented the cast of a class");
				}
		}
		else{
				this.exp.codeGenExprMethod(compiler, init);
		}
	}

}
