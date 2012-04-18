package org.golang.pkg.time;


import org.golang.runtime.Channel;
import org.golang.runtime.Panic;

/**
 * Equivalent to the go time package. Currently contains sleep only
 */
public class time {
    static public void Sleep(Duration duration) {
        try {
            Thread.sleep(duration.nanos /1000L, (int) (duration.nanos %1000L));
        } catch (InterruptedException e) {
            throw new Panic(Channel.noInterruptError);
        }
    }
}
