package org.golang.pkg.time;

/**
 * Created by IntelliJ IDEA.
 * User: elazar
 * Date: 4/17/12
 * Time: 8:29 AM
 * To change this template use File | Settings | File Templates.
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
