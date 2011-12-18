package org.golang.pkg.fmt;

import org.golang.runtime.CommaOk;

import java.io.IOException;

/**
 * Implement the functions in the fmt package
 */
public class fmt {

    static private int print(String string) throws IOException {
        byte[] bytes = string.getBytes();
        System.out.write(bytes);
        return bytes.length;
    }
    static public CommaOk<Integer> Print(Object... objects) {
        int nr = 0;
        try {
            for (Object object : objects) {
                if (object instanceof String) {
                    nr += print((String) object);
                } else if (object instanceof Stringer) {
                    nr += print(((Stringer) object).String());
                } else {
                    nr += print(object.toString());
                }
            }
            return CommaOk.of(nr);
        } catch (IOException e) {
            return CommaOk.fail();
        }
    }

    static public CommaOk<Integer> Println(Object... objects) {
        CommaOk<Integer> commaOk = Print(objects);
        if (!commaOk.ok) return commaOk;
        return Print("\n");
    }
}
