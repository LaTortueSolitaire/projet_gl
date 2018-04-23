package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import java.util.ArrayList;

/**
 * Definition of a class.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class ClassDefinition extends TypeDefinition {

    private DAddr operand;
    private ArrayList<Label> tableEtiquette;


    public void ajoutLabel(Label ajoute, int index){
        this.tableEtiquette.add(index, ajoute);
    }

    public ArrayList<Label> getTableEtiquette(){
      return this.tableEtiquette;
    }

    public Label getLabelTable(int index){
        return this.tableEtiquette.get(index);
    }

    public void setOperand(DAddr operand) {
      this.operand = operand;
    }

    public DAddr getOperand() {
      return operand;
    }

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }

    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass;

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
        this.tableEtiquette = new ArrayList<Label>();
    }

}
