package org.golang.runtime;

/**
 * Data class to assist returning two values from a go function. Caching will be added to ease tension on the GC.
 *
 * Usually N multiple return values will be compiled to return an instance of the TupleN class, however
 * for the very common case of returning a value and a boolean, we made this helper class.
 */
//@Immutable
public class CommaOk<T> {
    final public T value;
    final public boolean ok;

    private CommaOk(T value, boolean ok) {
        this.value = value;
        this.ok = ok;
    }

    static public <T> CommaOk<T> of(T value) {
        return new CommaOk<T>(value,true);
    }

    // Unchecked on purpose, so that we'll be able to return the same CommaOk failed object for all failures.
    @SuppressWarnings("unchecked")
    static final private CommaOk FAILURE = new CommaOk(null,false);

    @SuppressWarnings("unchecked")
    static public <T> CommaOk<T> fail() {
        // unchecked on purpose, see above. Due to type erasure it's OK to return the same object.
        return FAILURE;
    }
}
