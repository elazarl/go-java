import org.golang.pkg.time.Duration;
import org.golang.pkg.time.time;
import org.golang.runtime.Channel;
import org.golang.runtime.functions.Func1;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.golang.runtime.GoRoutines.go;
import static org.golang.runtime.Select.select;
import static org.golang.runtime.Select.Case;

/**
 * Tests implementation of Go's select in Java.
 */
public class SelectTest {
    @Test
    public void testSimpleSelectNoBuf() {
        testSimpleSelect(0);
    }

    @Test
    public void testSimpleSelectBuf() {
        testSimpleSelect(1);
    }

    private void testSimpleSelect(int buf) {
        final Channel<String> first = Channel.ofLength(buf);
        final Channel<String> last = Channel.ofLength(buf);
        final String FIRST = "first";
        final String LAST = "last";
        go(new Runnable() {
            @Override
            public void run() {
                time.Sleep(Duration.Millisecond.times(5));
                first.send(FIRST);
            }
        });
        go(new Runnable() {
            @Override
            public void run() {
                time.Sleep(Duration.Millisecond.times(50));
                last.send(LAST);
            }
        });

        final List<String> list = new ArrayList<String>();

        select(
                Case.of(first).then(new Func1<String>() {
                    @Override
                    public void call(String s) {
                        list.add(s);
                    }
                }),
                Case.of(last).then(new Func1<String>() {
                    @Override
                    public void call(String s) {
                        list.add(s);
                    }
                })
        );

        Assert.assertArrayEquals(new String[]{FIRST},list.toArray());

        select(
                Case.of(first).then(new Func1<String>() {
                    @Override
                    public void call(String s) {
                        list.add(s);
                    }
                }),
                Case.of(last).then(new Func1<String>() {
                    @Override
                    public void call(String s) {
                        list.add(s);
                    }
                })
        );

        Assert.assertArrayEquals(new String[]{FIRST, LAST},list.toArray());
    }
}
