package org.golang.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement "exception"-like functionality, should be called at the beginning and end of each function that uses
 * defer, or that might panic.
 *
 * The required usage for each function that uses defers is
 *
 * <pre>
 * {@code
 * void fun() {
 *      Func _ = Func.BEGIN();
 *      try {
 *          ...
 *          _.defer(new Runnable() {
 *             @Override void runWith() {
 *                 ...
 *             }
 *          });
 *          ...
 *          if (error) _.panic("Panic should rarely occur!");
 *          ...
 *      } finally {
 *          _.END();
 *      }
 * }
 * }
 * </pre>
 *
 */
public class Func {
    ThreadLocal<Boolean> isPanic = new ThreadLocal<Boolean>() {
        @Override protected Boolean initialValue() {
            return false;
        }
    };
    ThreadLocal<Panic> panicException = new ThreadLocal<Panic>();

    private List<Runnable> defers = new ArrayList<Runnable>();

    static Func BEGIN() {
        return new Func();
    }

    void defer(Runnable runnable) {
        defers.add(runnable);
    }

    void panic(String message) {
        isPanic.set(true);
        throw new Panic(message);
    }

    String recover() {
        if (!isPanic.get()) return null;
        // Note: It is safe to ignore Intellij comment here. Go programs don't use exceptions by design.
        String message = panicException.get().getMessage();
        panicException.set(null);
        isPanic.set(false);
        return message;
    }

    void END() {
        for (Runnable defer : defers) {
            defer.run();
        }
        if (isPanic.get()) throw panicException.get();
    }
}
