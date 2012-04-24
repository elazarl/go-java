package org.golang.runtime.tuples;

/**
 * See {@link Tuple2}
 */
public class Tuple3<V1,V2,V3> {
    public V1 _1;
    public V2 _2;
    public V3 _3;

    private Tuple3(V1 v1,V2 v2, V3 v3) {
        _1 = v1;
        _2 = v2;
        _3 = v3;
    }

    static public <V1,V2,V3> Tuple3<V1,V2,V3> of(V1 v1,V2 v2, V3 v3) {
        return new Tuple3<V1,V2,V3>(v1,v2,v3);
    }
}
