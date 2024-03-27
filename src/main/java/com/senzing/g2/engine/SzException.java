package com.senzing.g2.engine;

/**
 * 
 */
public class SzException extends Exception {
    /**
     * 
     */
    public SzException() {
        super();
    }

    /**
     * 
     */
    public SzException(String message) {
        super(message);
    }

    /**
     * 
     */
    public SzException(Throwable cause) {
        super(cause);
    }

    /**
     *
     */
    public SzException(String message, Throwable cause) {
        super(message, cause);
    }
}
