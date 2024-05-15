package fspm.config.params;

import fspm.util.KeyElement;

/**
 * Parameter represents variables that can be stored using Java data types
 * or other user-defined types.
 * <p>
 * The value field should be implemented by extending members, as values require
 * specific primitive type declarations.
 * 
 * @author Ou-An Chuang
 */
public abstract class Parameter extends KeyElement {
    /**
     * Protected constructor as Parameter is an abstract class and
     * should not be used for instantiation.
     * 
     * @param key The parameter key.
     */
    protected Parameter(String key) {
        super(key);
    }
}
