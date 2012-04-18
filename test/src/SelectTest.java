import org.golang.pkg.fmt.fmt;
import org.golang.pkg.time.Duration;
import org.golang.pkg.time.time;
import org.golang.runtime.Channel;
import org.golang.runtime.GoRoutines;
import org.golang.runtime.Select;
import org.golang.runtime.functions.Func1;
import org.junit.Test;

import static org.golang.runtime.GoRoutines.go;
import static org.golang.runtime.Select.select;
import static org.golang.runtime.Select.Case;

/**
 * Created by IntelliJ IDEA.
 * User: elazar
 * Date: 4/17/12
 * Time: 1:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectTest {
    @Test
    public void testSimpleSelect() {
        final Channel<String> first = Channel.ofLength(1);
        final Channel<String> last = Channel.ofLength(1);
        go(new Runnable() {
            @Override
            public void run() {
                time.Sleep(Duration.Millisecond.times(5));
                first.send("Should be seen first");
            }
        });
        go(new Runnable() {
            @Override
            public void run() {
                time.Sleep(Duration.Millisecond.times(50));
                last.send("Should never be seen");
            }
        });

        select(
                Case.of(first).then(new Func1<String>() {
                    @Override
                    public void call(String s) {
                        fmt.Println("case first:",s);
                    }
                }),
                Case.of(last).then(new Func1<String>() {
                    @Override
                    public void call(String s) {
                        fmt.Println("case last:",s);
                    }
                })
        );
    }
}
