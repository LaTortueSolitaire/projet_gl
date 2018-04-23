package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl17
 * @date 01/01/2018
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public List<Type> getArgs(){
    	return args;
    }

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == null) {
    		return false;
    	}
    	else if (obj instanceof Signature) {
    		Signature sig = (Signature) obj;
    		return this.getArgs().equals(sig.getArgs());
    	}
    	return false;
    }

}
