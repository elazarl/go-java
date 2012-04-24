package org.golang.pkg.time;

/**
 * Mimics Go's {@code time.Duration} type. Duration contains a {@code long} value, counting nanoseconds, and can be
 * used in a readable way with the predefined constants.
 *
 * <pre>
 *     time.Sleep(Duration.Second.times(5)); // sleeps for five seconds
 * </pre>
 */
public class Duration {

    static final public Duration Nanosecond = new Duration(1);
    static final public Duration Millisecond = Nanosecond.times(1000);
    static final public Duration Second = Millisecond.times(1000);
    static final public Duration Minute = Second.times(60);
    static final public Duration Hour = Minute.times(60);

    private Duration(long nanos) {
        this.nanos = nanos;
    }
    final public long nanos;
    
    public Duration times(long l) {
        return new Duration(nanos *l);
    }
    
    static public Duration of(Duration duration) {
        return new Duration(duration.nanos);
    }
}
