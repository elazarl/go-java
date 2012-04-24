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
 *      DeferEnv _ = DeferEnv.BEGIN();
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
public class DeferEnv {
    static ThreadLocal<Boolean> isPanic = new ThreadLocal<Boolean>() {
        @Override protected Boolean initialValue() {
            return false;
        }
    };
    static ThreadLocal<Panic> panicException = new ThreadLocal<Panic>();

    private List<Runnable> defers = new ArrayList<Runnable>();

    public static DeferEnv BEGIN() {
        return new DeferEnv();
    }

    public void defer(Runnable runnable) {
        defers.add(runnable);
    }

    public void panic(String message) {
        isPanic.set(true);
        panicException.set(new Panic(message));
        throw panicException.get();
    }

    // result of exception not thrown since we're recovering from it
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    static public String recover() {
        if (!isPanic.get()) return null;
        // Note: It is safe to ignore Intellij comment here. Go programs don't use exceptions by design.
        String message = panicException.get().getMessage();
        panicException.set(null);
        isPanic.set(false);
        return message;
    }

    private Runnable popDefered() {
        return defers.remove(defers.size()-1);
    }

    public void END() {
        while (!defers.isEmpty()) {
            popDefered().run();
        }
        if (isPanic.get()) throw panicException.get();
    }
}
