package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Declaration of type and field visibility
 * @author gl17 Anais
 * @date 01/01/2018
 */
public class DeclFieldSet extends AbstractFieldSet {

    final private Visibility visibility;
    final private AbstractIdentifier type;
    final private ListFields listFields;

    public DeclFieldSet(Visibility visibility, AbstractIdentifier type, ListFields listFields) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(listFields);
        this.visibility = visibility;
        this.type = type;
        this.listFields = listFields;

    }

    @Override
    protected void verifyFieldSetMembers(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError{
              Type t = this.type.verifyType(compiler);
              if (t.isVoid()) {
            	  throw new ContextualError("Unsupported type in field set: Field cannot be of type void", this.getLocation());
              }
              this.listFields.verifyListFieldsMembers(compiler, localEnv, currentClass, t, this.visibility);
            }

    @Override
    protected void verifyFieldSetBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError{
    	Type fieldType = this.type.getType();
    	this.listFields.verifyListFieldsBody(compiler, localEnv, currentClass, fieldType);
            }

    @Override
    protected void iterChildren(TreeFunction f) {
                type.iter(f);
                //TODO
            }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
          type.prettyPrint(s, prefix, false);
          listFields.prettyPrint(s, prefix, true);
            }

    @Override
    public void decompile(IndentPrintStream s) {
      if (visibility == visibility.PROTECTED){
        s.print(visibility.toString().toLowerCase());
      }
      s.print(" ");
      type.decompile(s);
      s.print(" ");
      listFields.decompile(s);
      }

    @Override
    /**
     *  Generate code for all the fields contains in a class
     *
     * @param compiler  (contains the "env_types" attribute)
     *
     */
    protected void codeGenFieldSet(DecacCompiler compiler) {
    	this.listFields.codeGenListField(compiler);
    	}

}
