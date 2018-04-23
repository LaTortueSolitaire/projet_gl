package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.context.ParamDefinition;



/**
 * Declaration of a method parameter
 * @author gl17
 * @date 01/01/2018
 */
public class DeclParam extends AbstractDeclParam{


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;


    public DeclParam(AbstractIdentifier type, AbstractIdentifier varName){
        Validate.notNull(type);
        Validate.notNull(varName);
        this.type = type;
        this.varName = varName;
    }


    /**
     * Verify the parameters as argument of method for contextual error
     *
     * implements non-terminal "decl_param"
     *    of [SyntaxeContextuelle] in pass 2
     *
     * @param compiler  (contains the "env_types" attribute)
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     * @throws ContextualError if contextual syntax isn't respected
     */
    @Override
    protected Type verifyParamMembers(DecacCompiler compiler) throws ContextualError{
    	Type paramType = type.verifyType(compiler);
    	if (paramType.isVoid()){
    		throw new ContextualError("Unsupported type in method arguments: Parameter cannot be of type void", this.getLocation());
    	}
    	type.setType(paramType);
    	return paramType;
    }

    /**
     * Verify the parameters as argument of method for contextual error
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
     * @param index
     * 			  Parameter stack index for stage C
     * @throws ContextualError if contextual syntax isn't respected
     */

    @Override
    protected void verifyParamBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, int index) throws ContextualError {

        try {
        	ParamDefinition paramDef = new ParamDefinition(type.getType(), this.getLocation());
        	paramDef.setIndex(index);
        	localEnv.declare(this.varName.getName(), paramDef);
        }
        catch (EnvironmentExp.DoubleDefException e) {
        	throw new ContextualError("Double definition of parameter", this.getLocation());
        }
        varName.verifyExpr(compiler, localEnv, currentClass);

}

  /*  @Override
    protected void codeGenParam(DecacCompiler compiler){
    }*/

    @Override
    protected void iterChildren(TreeFunction f) {
                type.iter(f);
                //TODO
            }

    @Override
    public void decompile(IndentPrintStream s) {
      type.decompile(s);
      s.print(" ");
      varName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, true);
    }

}
