package org.golang.runtime;

/**
 * Equivalent to Go pointer.
 *
 * <pre>
 * {@code Ptr<Foo> p} ⇔ {@code var p *Foo}
 * </pre>
 */
public class Ptr<T> {
    T inner;
    public T get() {
        return inner;
    }
    
    private Ptr(T val) {this.inner = val;}
    
    static public <T> Ptr<T> of(T value) {
        return new Ptr<T>(value);
    }
    
    @Override
    public String toString() {
        return "*"+inner;
    }
}
