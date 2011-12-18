package org.golang.example;

import org.golang.pkg.fmt.fmt;
import org.golang.runtime.Channel;
import org.golang.runtime.ReceiveOnlyChannel;
import org.golang.runtime.SendOnlyChannel;

import static org.golang.runtime.GoRoutines.go;

/**
 * Translated example from golang.org home page
 */
@SuppressWarnings({"InfiniteLoopStatement"})
public class PrimeSieve {

    static class Filter implements Runnable {
        ReceiveOnlyChannel<Integer> in;
        SendOnlyChannel<Integer> out;
        int prime;
        public Filter(ReceiveOnlyChannel<Integer> _in, SendOnlyChannel<Integer> _out, int _prime) {
            in = _in;out = _out;prime = _prime;
        }

        public void run() {
            // Note: It is safe to ignore Intellij's comment about infinite loop, it's a valid Go pattern
            while (true) {
                int i = in.receive();
                if ((i % prime) != 0) {
                    out.send(i);
                }
            }
        }
    }

    public static void main(String[] argv) {
        Channel<Integer> ch = Channel.make();

        go(new Runnable() {
            SendOnlyChannel<Integer> ch;
            public Runnable with(SendOnlyChannel<Integer> ch) {this.ch = ch;return this;}

            public void run() {
                for (int i = 2; ; i++) {
                    this.ch.send(i);
                }
            }
        }.with(ch.getSendOnly()));

        for (int i=0;i<10;i++) {
            int prime = ch.receive();
            fmt.Println(prime);
            Channel<Integer> ch1 = Channel.make();
            go(new Filter(ch.getReceiveOnly(),ch1.getSendOnly(),prime));
            ch = ch1;
        }
    }
}
