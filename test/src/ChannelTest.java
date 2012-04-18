import org.golang.runtime.Channel;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: elazar
 * Date: 4/18/12
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChannelTest {
    @Test
    public void simpleTest() {
        Channel<String> channel = Channel.ofLength(1);
        channel.send("a");
        Assert.assertEquals("a",channel.receive());
    }

    @Test
    public void simpleTwoThreads() {
        final Channel<String> channel = Channel.ofLength(1);
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Sending");
                channel.send("a");
                System.out.println("Sent");
            }
        });
        System.out.println("Receiving");
        Assert.assertEquals("a",channel.receive());
        System.out.println("Received");
    }
}
