package com.djh.test;

/**
 * @author duan
 * @create 2021-07-22 20:54
 */


public class Test<T> implements Generator<T> {
    @Override
    public T next() {
        return null;
    }

    public <T> T hi() {
        return null;
    }
}

