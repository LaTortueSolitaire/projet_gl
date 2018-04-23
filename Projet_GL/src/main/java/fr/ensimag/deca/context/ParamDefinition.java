package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a method parameter.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class ParamDefinition extends ExpDefinition {

    private int index;
    
    public int getIndex() {
    	return this.index;
    }
    
    public void setIndex(int index) {
    	this.index = index;
    }
    
	public ParamDefinition(Type type, Location location) {
        super(type, location);
    }
    @Override
    public String getNature() {
        return "parameter";
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public boolean isParam() {
        return true;
    }

}
