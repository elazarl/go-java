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
        int nr = 0;
        CommaOk<Integer> commaOk;
        for (Object object : objects) {
            commaOk = Print(object);
            if (!commaOk.ok) return commaOk;
            nr += commaOk.value;
            commaOk = Print(" ");
            if (!commaOk.ok) return commaOk;
            nr += commaOk.value;
        }
        commaOk = Print("\n");
        if (!commaOk.ok) return commaOk;
        return CommaOk.of(commaOk.value+nr);
    }

    /**
     * Poor implementation of Go's fmt.Sprintf(...). Will use Java's {@code String.format}, and will replace %v with %s
     * @param pattern pattern
     * @param objects objects to embed in pattern
     * @return pattern with embedded objects
     */
    public static String Sprint(String pattern, Object... objects) {
        return String.format(pattern.replace("%v","%s"),objects);
    }
}
