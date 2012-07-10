package com.fastbiz.common.structure;

public class Pair<A, B> {

    private A a;

    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Pair<A, B> with(A a, B b){
        return new Pair<A, B>(a, b);
    }

    public A getFirst(){
        return a;
    }

    public B getSecond(){
        return b;
    }

    public void setFirst(A a){
        this.a = a;
    }

    public void setSecond(B b){
        this.b = b;
    }

    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        if (a == null) {
            if (other.a != null)
                return false;
        } else if (!a.equals(other.a))
            return false;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;
        return true;
    }
}
