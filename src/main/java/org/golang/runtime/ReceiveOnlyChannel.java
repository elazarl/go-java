package org.golang.runtime;

/**
 * Equivalent to single directional channel {@code var i <-chan int}
 *
 * See {@link Channel}.
 */
public class ReceiveOnlyChannel<T> {
    private Channel<T> underlyingChannel;

    ReceiveOnlyChannel(Channel<T> channel) {
        underlyingChannel = channel;
    }

    public T receive() {
        return underlyingChannel.receive();
    }
}
