package org.golang.runtime.functions;

/**
 * A function that receives a single variable and returns a single variable
 */
public abstract class Func2<R,T> {
    abstract R call(T t);
}
