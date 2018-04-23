package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.*;

/**
 *	Declaration of classes
 * @author gl17
 * @date 01/01/2018
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
//        LOG.debug("verify listClass: start");
        for (AbstractDeclClass dc : this.getList()) {
        	dc.verifyClass(compiler);
        }
//         LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass dc : this.getList()) {
        	dc.verifyClassMembers(compiler);
        }
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass c : getList()) {
          c.verifyClassBody(compiler);
        }
    }

    /**
    	*  Generate code to produce the method table of every class of the program deca.
    	*
    	* @param compiler  (contains the "env_types" attribute)
    	*
    	*/
    protected void codeGenPreListDeclClass(DecacCompiler compiler) {
    	//classe objet toujours en 1(GB) et 2(GB)*
    	compiler.getLinkedList().getLast().addComment("Code de la table des méthodes de Object");
    	compiler.addInstruction(new LOAD(new NullOperand(), GPRegister.R0));
    	compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(1, Register.GB)));
    	compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), GPRegister.R0));
    	compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(2, Register.GB)));
    	 for (AbstractDeclClass c : getList()) {
             c.codeGenPreDeclClass(compiler);
         }
    }

    /**
      *  Generate code to produce the initialization of the fields of every class in the program deca.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      */
    protected void codeGenListDeclClass(DecacCompiler compiler) {
    	 for (AbstractDeclClass c : getList()) {
         IMAProgram lastOne = new IMAProgram();
         compiler.getLinkedList().addLast(lastOne);
         c.codeGenDeclClass(compiler);
         }
    }

    /**
      *  Generate code to produce the method body of every class of the program deca.
      *
      * @param compiler  (contains the "env_types" attribute)
      *
      */
    protected void codeGenListDeclClassMethod(DecacCompiler compiler){
      Label objet = new Label("code.Object.equals");
      compiler.getLinkedList().getLast().addLabel(objet);
      compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),Register.getR(0)));
      compiler.addInstruction(new LOAD(new RegisterOffset(-3,Register.LB),Register.getR(1)));
      compiler.addInstruction(new CMP(Register.getR(0),Register.getR(1)));
     compiler.addInstruction(new SEQ(Register.R0));      //ajouter les instructions
      for (AbstractDeclClass c : getList()) {
        c.codeGenDeclClassMethod(compiler);
      }
    }


}
