package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.Signature;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl17
 * @date 01/01/2018
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
      int virgule = 0;
      for(AbstractExpr i : getList()){
        if (virgule > 0){
          s.print(",");
        }
          i.decompile(s);
          virgule = virgule +1;
      }
    }
    
    public void verrifyAsParams(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
    		Signature methodSig, Location location  ) throws ContextualError {
    	if (getList().size() != methodSig.size()) {
    		throw new ContextualError("Wrong number of parameters in method signature", location);
    	}
    	int index = 0;
    	for(AbstractExpr exp : getList()) {
    		try {
    			Type expectedType = methodSig.getArgs().get(index);
    			this.set(index, exp.verifyRValue(compiler, localEnv, currentClass, expectedType));
    			index ++;
    		}
    		catch (ContextualError e) {
    			throw new ContextualError("Parameter n°" + (index+1) + " : " + e.getMessage().substring(12), this.getLocation());
    		}
    	}
    }
    
}
