package com.thattonybo.modbase.errors;

/**
 * Basic exception when Mewnloader hasn't found the right class
 */
public class UnknownClassException extends Exception {
    /**
     * Creates a new instance of `ClassNotFoundException`
     * @param className The class name
     */
    public UnknownClassException(String className) {
        super("Couldn't find class " + className);
    }
}