package org.golang.runtime;

/**
 * Equivalent to single directional channel {@code var i chan<- int}
 *
 * See {@link Channel}.
 */
public class SendOnlyChannel<T> {
    private Channel<T> underlyingChannel;

    SendOnlyChannel(Channel<T> channel) {
        underlyingChannel = channel;
    }

    public void send(T message) {
        underlyingChannel.send(message);
    }
}
