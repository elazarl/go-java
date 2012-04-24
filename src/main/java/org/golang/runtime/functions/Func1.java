package org.golang.runtime.functions;

/**
 * A function that receives an argument, and returns none
 */
public abstract class Func1<T> {
    public abstract void call(T t);
}
