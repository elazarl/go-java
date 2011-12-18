package org.golang.runtime;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Equivalent to bidirectional Go's channels
 *
 * <pre>
 * {@code x := make(chan int)
 *   y := make(chan string,2)}
 * </pre>
 *
 * is equivalent to
 * <pre>
 * {@code Channel<Integer> x = Channel.make();
 *   Channel<String> y = Channel.ofLength(2);}
 * </pre>
 */
public class Channel<T> {
    private BlockingQueue<T> queue;

    static final String noInterruptError = "In go-like program, Thread.interrupt should never be called";

    private Channel(int length) {
        queue = new ArrayBlockingQueue<T>(length);
    }

    private Channel() {
        queue = new SynchronousQueue<T>();
    }

    static public <T> Channel<T> make() {
        return new Channel<T>();
    }

    static public <T> Channel<T> ofLength(int length) {
        return new Channel<T>(length);
    }

    public SendOnlyChannel<T> getSendOnly() {
        return new SendOnlyChannel<T>(this);
    }

    public ReceiveOnlyChannel<T> getReceiveOnly() {
        return new ReceiveOnlyChannel<T>(this);
    }

    public void send(T data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            throw new AssertionError(noInterruptError);
        }
    }

    public T receive() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new AssertionError(noInterruptError);
        }
    }
}
