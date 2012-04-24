package org.golang.runtime.tuples;

/**
 * TupleN class is used to return multiple values.
 *
 * <pre>
 *     {@code
 *     func f() (int,string) {
 *         return
 *     }
 *     }
 * </pre>
 */
public class Tuple2<V1,V2> {
    public V1 _1;
    public V2 _2;

    private Tuple2(V1 v1,V2 v2) {
        _1 = v1;
        _2 = v2;
    }

    static public <V1,V2> Tuple2<V1,V2> of(V1 v1,V2 v2) {
        return new Tuple2<V1,V2>(v1,v2);
    }

}
