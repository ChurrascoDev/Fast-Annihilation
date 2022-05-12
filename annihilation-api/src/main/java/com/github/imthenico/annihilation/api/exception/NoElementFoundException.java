package com.github.imthenico.annihilation.api.exception;

public class NoElementFoundException extends Exception {

    private final String elementName;

    public NoElementFoundException(String message, String elementName) {
        super(message);
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }
}