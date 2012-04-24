package org.golang.runtime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Static class that would runWith all goroutines created in runtime.
 * Like the Go implementation, goroutines will runWith in a thread pool limited to
 */
public class GoRoutines {
//    private static final int GOMAXPROCS = Runtime.getRuntime().availableProcessors();
    // I'm cheating a little, since I can't "steal" the stack of a thread in Java, so I can't have one thread running
    // two goroutines.
    static final ExecutorService goroutinesExecutor = Executors.newCachedThreadPool();

    public static void go(Runnable goroutine) {
        goroutinesExecutor.execute(goroutine);
    }
}
