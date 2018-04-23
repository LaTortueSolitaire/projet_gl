package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *	Else if statement
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class ElseIf extends If {

    public ElseIf(AbstractExpr condition, ListInst thenBranch){
        super(condition, thenBranch);
    }


}
