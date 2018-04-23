package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl17
 * @date 01/01/2018
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to thhis expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     *
     * implements non-terminals "expr" and "lvalue"
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
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     *
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
    	Type expType = this.verifyExpr(compiler, localEnv, currentClass);

    	if (subtype(compiler, expType, expectedType)) {
    		if (expType.sameType(expectedType)) {
   			return this;
    		}
    		else {
    			Identifier expId = new Identifier(expectedType.getName());
    			Cast cast = new Cast(this, expId);
    			cast.setLocation(this.getLocation());
    			cast.verifyExpr(compiler, localEnv, currentClass);
    			return cast;
    		}
    	}
    	else if (expType.isInt() && expectedType.isFloat()) {
    		ConvFloat convFloat = new ConvFloat(this);
    		convFloat.setLocation(this.getLocation());
    		convFloat.verifyExpr(compiler, localEnv, currentClass);
    		return convFloat;
    	}
    	else    {
    		Symbol expectedTypeName = expectedType.getName();
    		if (expectedType.isClass()){
    			throw new ContextualError("Right value expression must be of type "+ expectedTypeName, this.getLocation());
    		}
    		else {
    			if (expectedType.isFloat()) {
    				throw new ContextualError("Right value expression must be of type int or float", this.getLocation());
    			}
    			else {
    				throw new ContextualError("Right value expression must be of type " + expectedTypeName, this.getLocation());
    			}
    		}
    	}	
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.verifyExpr(compiler, localEnv, currentClass);
        if (!t.isBoolean()){
          throw new ContextualError("Condition must be of type boolean", getLocation());
        }
    }

    /**
      *Generate code to print the expression

      *@see AbstractPrint.codeGenInst

      *@param compiler contains "env_types" attribute
    */
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) { }
    protected void codeGenPrintMethod(DecacCompiler compiler, boolean printHex){}


    @Override
    protected void codeGenInst(DecacCompiler compiler) {}


    protected void codeGenExpr(DecacCompiler compiler, boolean init){}


    protected void codeGenCompare(DecacCompiler compiler, Label deb, Label end) {}
    protected void codeGenCompareMethod(DecacCompiler compiler, Label deb, Label end) {}


    protected void codeGenExprMethod(DecacCompiler compiler, boolean init){}
    protected void codeGenExprConv(DecacCompiler compiler, boolean init){}
    protected void codeGenExprConvMethod(DecacCompiler compiler, boolean init){}

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.println(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    //Renvoie true ssi le type rightType est un sous-type du type leftType selon les relations de sous-typage
    //
    public static boolean subtype(DecacCompiler compiler, Type rightType, Type leftType) {
    	//R1
    	if (rightType.sameType(leftType)) {
    		return true;
    	}

    	//R5
    	if (rightType.isNull() && leftType.isClass()) {
    		return true;
    	}

    	if (rightType.isClass()){
    		//R2
    		if (leftType.getName().getName().equals("Object")) {
    			return true;
    		}
    		//on récupère le type parent du type droit
    		Type parentType;
    		try {
    			parentType = rightType.asClassType("", null).getDefinition().getSuperClass().getType();
    		} catch (ContextualError | NullPointerException e) {
    			return false;
    		}

    		if (leftType.isClass()){
    			//R3 : Cas d'une classe fille

    			if (parentType.getName().equals(leftType.getName())){
    				return true;
    			}
    			//R4 : On regarde si le parent du type droit est un sous-type du type gauche
    			else {
    				return subtype(compiler, parentType, leftType);
    			}
    		}
    	}
    	return false;
    }
}
