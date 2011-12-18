package org.golang.runtime;

/**
 * Internal exception thrown to emulate Go's panic.
 */
public class Panic extends Error {

    public Panic(String message) {
        super(message);
    }
}
