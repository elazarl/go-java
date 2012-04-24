package org.golang.example;

import org.golang.pkg.fmt.fmt;
import org.golang.runtime.Ptr;
import org.golang.runtime.Slice;

import static org.golang.runtime.Slice.append;
import static org.golang.runtime.Slice.copy;

/**
 * Translated http://tour.golang.org/ to Java
 */
public class Tour {
    static class Vector {
        int x,y;
        public Vector(int x, int y) {this.x = x;this.y = y;}
        public Vector() {}
        @Override public String toString() {return "Vector{"+x+","+y+"}";}
    }

    public static void main(String[] args) {
        Ptr<Vector> p    = Ptr.of(new Vector(1,2)); // p := &Vector{1,2}
        Ptr<Vector> newp = Ptr.of(new Vector());
        fmt.Println(p,newp);

        Slice<Integer> s1 = Slice.of(1, 2, 3, 4);
        Slice<Integer> s2 = Slice.of(11, 12, 13, 14, 15, 16);
        copy(s1, s2.sub(2));

        fmt.Println(s1,s2,append(s1.sub(1,2),7,8,9));

        // p := Vector{1}, Vector{X:1} is lacking here. Can't find typesafe way to do that
        // in C# it's workable though
    }
}
