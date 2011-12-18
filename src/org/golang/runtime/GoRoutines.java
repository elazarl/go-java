package org.golang.runtime;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Static class that would runWith all goroutines created in runtime.
 * Like the Go implementation, goroutines will runWith in a thread pool limited to
 */
public class GoRoutines {
    private static final int GOMAXPROCS = Runtime.getRuntime().availableProcessors();
    static final Executor goroutinesExecutor = Executors.newFixedThreadPool(GOMAXPROCS);

    public static void go(Runnable goroutine) {
        goroutinesExecutor.execute(goroutine);
    }
}
