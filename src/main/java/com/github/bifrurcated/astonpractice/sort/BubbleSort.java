package com.github.bifrurcated.astonpractice.sort;

import java.util.Comparator;

public class BubbleSort<E> implements Consumer3<E[], Integer, Comparator<? super E>> {
    @Override
    public void accept(E[] array, Integer size, Comparator<? super E> c) {
        for (int i = 0; i < size; i++) {
            for (int j = i+1; j < size; j++) {
                if (c.compare(array[i], array[j]) > 0) {
                    E tmp = array[j];
                    array[j] = array[i];
                    array[i] = tmp;
                }
            }
        }
    }
}
