package org.golang.runtime;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
    private Queue<T> queue = new ArrayDeque<T>();
    private int maxSize;

    public static final String noInterruptError = "In go-like program, Thread.interrupt should never be called";

    /** Main lock guarding all access */
    private final ReentrantLock lock;
    /** Condition for waiting takes */
    private final Condition notEmpty;
    /** Condition for waiting puts */
    private final Condition notFull;

    private Channel(int length) {
        maxSize  = length;
        lock     = new ReentrantLock(true);
        notEmpty = lock.newCondition();
        notFull  = lock.newCondition();
    }

    private Channel() {
        this(0);
    }

    static public <T> Channel<T> make() {
        return new Channel<T>();
    }

    static public <T> Channel<T> ofLength(int length) {
        if (length == 0) throw new RuntimeException("Not implemented yet");
        return new Channel<T>(length);
    }

    public SendOnlyChannel<T> getSendOnly() {
        return new SendOnlyChannel<T>(this);
    }

    public ReceiveOnlyChannel<T> getReceiveOnly() {
        return new ReceiveOnlyChannel<T>(this);
    }

    void startReceive() {
        lock.lock();
        while (queue.size() == 0) notEmpty.awaitUninterruptibly();
    }

    T endReceive() {
        try {
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }

    void cancelReceive() {
        lock.unlock();
    }

    public void send(T data) {
        lock.lock();
        try {
            while (queue.size() == maxSize) notFull.awaitUninterruptibly();
            queue.add(data);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T receive() {
        lock.lock();
        try {
            while (queue.size() == 0) notEmpty.awaitUninterruptibly();
            T t = queue.remove();
            notFull.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

}
