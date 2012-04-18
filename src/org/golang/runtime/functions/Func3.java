package org.golang.runtime.functions;

/**
 * * A function that receives a two variables and returns a single variable
 */
public abstract class Func3<R,T1,T2> {
    abstract public R call(T1 t1, T2 t2 );
}
