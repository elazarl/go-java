package org.golang.runtime;

/**
 * A {@code Slice<T> s} is equivalent to {@code var s []T} in Go
 */
public class Slice<T> {
    private T[] rawData;
    private int cap;
    private int size;
    private int start;
    
    private Slice(T[] raw, int start, int size, int cap) {
        this.rawData = raw;
        this.size    = size;
        this.cap     = cap;
        this.start   = start;
    }

    static public <T> Slice<T> of(T... ts) {
        return new Slice<T>(ts,0,ts.length,ts.length);
    }

    @SuppressWarnings("unchecked")
    static public <T> Slice<T> make(int size, int cap) {
        return new Slice<T>((T[])new Object[cap],0,size,cap);
    }

    static public <T> Slice<T> make(int size) {
        return make(size,size);
    }

    public Slice<T> sub(int start, int nonInclusiveEnd) {
        int subSize = nonInclusiveEnd - start;
        return new Slice<T>(rawData,this.start+start, subSize, cap-(start-this.start));
    }

    public Slice<T> sub(int start) {
        return sub(start,start+size);
    }

    public Slice<T> startTo(int nonInclusiveEnd) {
        return sub(start,nonInclusiveEnd);
    }
    
    static public <T> Slice<T> append(Slice<T> s, T... ts) {
        if (ts.length < s.cap - s.size) {
            for (int i = 0; i < ts.length; i++) {
                s.rawData[s.start+s.size+i] = ts[i];
            }
            s.size += ts.length;
            return s;
        }
        // TODO: double the cap every append
        Slice<T> result = make(s.size + ts.length);
        copy(s,result);
        System.arraycopy(ts, 0, result.rawData, s.size, ts.length);
        return result;
    }
    
    static public <T> int copy(Slice<T> src, Slice<T> dst) {
        System.arraycopy(src.rawData, src.start, dst.rawData, dst.start, src.size);
        return src.size;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = start; i < size; i++) {
            builder.append(rawData[i]);
            if (i < size-1) builder.append(" ");
        }
        builder.append("]");
        return builder.toString();
    }
}
