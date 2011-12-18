package org.golang.example;

import org.golang.pkg.fmt.fmt;
import org.golang.runtime.Func;
import org.golang.runtime.Panic;

import static org.golang.runtime.Func.*;

/**
 * Example of defer and catch usage from http://blog.golang.org/2010/08/defer-panic-and-recover.html
 */
public class DeferPanicAndRecover {
    // Output should be 3210
    static public void b() {
        final Func _ = BEGIN();
        try {
            for (int i=0;i<4;i++) {
                final int finalI = i;
                _.defer(new Runnable(){
                    public void run() {
                        fmt.Print(finalI);
                    }
                });
            }
        } catch(Panic panic){_.END();} finally {
            _.END();
        }
    }

    // Return value is evaluated before calling to defer.
    // We need to rewrite return value so that it'll return the value changed by the closure.
    // There are two problems here (1) Java don't allow real closures. (2) return value is evaluated before the "finally"
    // clause, this is problematic for primitives, but not for classes.
    // We'll fix that by always returning a reference to something, and unwrapping the reference with a helper function
    static public int[] c_() {
        final Func _ = BEGIN();
        try {
            final int[] i = new int[1];
            _.defer(new Runnable() {
                public void run() {
                    i[0]++;
                }
            });
            return i;
        } catch(Panic panic){_.END();return null;} finally {
            _.END();
        }
    }

    static public int c() {
        return c_()[0];
    }

    static public void f() {
        final Func _ = BEGIN();
        try {
            _.defer(new Runnable() {
                public void run() {
                    String r = recover();
                    if (r != null) {
                        fmt.Println("recovered in f",r);
                    }
                }
            });
            fmt.Println("Calling g");
            g(0);
            fmt.Println("Returned normally from g.");
        } catch(Panic panic){_.END();} finally {
            _.END();
        }
    }

    // This function ends recursion with exception by design.
    @SuppressWarnings({"InfiniteRecursion"})
    private static void g(final int i) {
        final Func _ = BEGIN();
        try {
            if (i > 3) {
                fmt.Println("Panicking!");
                _.panic(fmt.Sprint("%v", i));
            }
            _.defer(new Runnable() {
                public void run() {
                    fmt.Println("Defer in g",i);
                }
            });
            fmt.Println("Printing in g",i);
            g(i+1);
        } catch(Panic panic){_.END();} finally {
            _.END();
        }
    }

    static public void main(String[] argv) {
        fmt.Println("running b, expected output 3210");
        b();
        fmt.Println("\nrunning c() expected output 1");
        fmt.Println(c());
        f();
        fmt.Println("Returned normally from f.");
        fmt.Println("Expected:'''");
        fmt.Println("Calling g.\n" +
                "Printing in g 0\n" +
                "Printing in g 1\n" +
                "Printing in g 2\n" +
                "Printing in g 3\n" +
                "Panicking!\n" +
                "Defer in g 3\n" +
                "Defer in g 2\n" +
                "Defer in g 1\n" +
                "Defer in g 0\n" +
                "Recovered in f 4\n" +
                "Returned normally from f.'''");
    }
}
