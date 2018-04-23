package fr.ensimag.deca.context;


import java.util.*;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 *
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 *
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 *
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 *
 * Insertion (through method declare) is always done in the "current" dictionary.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class EnvironmentType {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).
    public Map<Symbol, TypeDefinition> environnement;

    EnvironmentType parentEnvironment;

    public EnvironmentType(EnvironmentType parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.environnement = new HashMap<Symbol, TypeDefinition>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        // throw new UnsupportedOperationException("not yet implemented");
        TypeDefinition res = (TypeDefinition) this.environnement.get(key);

        if (res != null) {
          return res;
        }
        if (this.parentEnvironment != null) {
          return this.parentEnvironment.get(key);
        }
        return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     *
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     *
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {
      // Symbol is in the current dictionary
      if (this.environnement.containsKey(name)) {
        throw new DoubleDefException();
      }
      // Symbol is in a previous dictionary
      EnvironmentType current = this.parentEnvironment;
      while (current != null) {
        if (current.environnement.containsKey(name)) {
          current.environnement.remove(name);
        }
        current = current.parentEnvironment;
      }
      // Adding the symbol
      this.environnement.put(name, def);
        // throw new UnsupportedOperationException("not yet implemented");
    }

}
