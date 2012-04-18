package org.golang.runtime;

import org.golang.runtime.functions.Func1;
import org.golang.runtime.functions.Func2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.golang.runtime.GoRoutines.go;

/**
 * Emulate Go's select statement, usage
 *
 * <pre>{@code select(
 * Case.of(ch1).then(
 *     new Func1<Integer>(Integer i) {
 *         fmt.Println("ch1",i)
 *     }
 * ),
 * Case.of(ch2).then(
 *     new Func1<Integer>(Integer i) {
 *         fmt.Println("ch2",i)
 *     }
 * ),
 * );
 * }
 * </pre>
 */
public class Select {
    public static class Do<T> {
        private Channel<T> channel;

        public Do(Channel<T> channel) {
            this.channel = channel;
        }

        public Case<T> then(Func1<T> action) {
            return new Case<T>(channel,action);
        }
    }
    public static class Case<T> {
        private Channel<T> channel;
        private Func1<T> action;

        private Case(Channel<T> channel, Func1<T> action) {
            this.channel = channel;
            this.action  = action;
        }

        static public <T> Do<T> of(Channel<T> channel1) {
            return new Do<T>(channel1);
        }
    }
    
    public static void select(Case... cases) {
        final AtomicBoolean iAmFirst = new AtomicBoolean(true);
        final CountDownLatch latch = new CountDownLatch(cases.length);
        for (final Case aCase : cases) {
            go(new Runnable() {
                @Override
                public void run() {
                    aCase.channel.startReceive();
                    if (iAmFirst.getAndSet(false)) {
                        aCase.action.call(aCase.channel.endReceive());
                    } else {
                        aCase.channel.cancelReceive();
                    }
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new Panic(Channel.noInterruptError);
        }
    }
}
