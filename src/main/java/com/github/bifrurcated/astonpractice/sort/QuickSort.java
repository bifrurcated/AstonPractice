package com.github.bifrurcated.astonpractice.sort;

import java.util.Comparator;

public class QuickSort<E> implements Consumer3<E[], Integer, Comparator<? super E>> {
    @Override
    public void accept(E[] array, Integer size, Comparator<? super E> c) {
        qsort(array, c, -1, size-1);
    }

    private void qsort(E[] array, Comparator<? super E> c, int wall, int pivot) {
        int start = wall + 1;
        if (pivot - start <= 0) return;
        for (int j = start; j < pivot; j++) {
            if (c.compare(array[j], array[pivot]) < 0) {
                if (++wall != j) {
                    E tmp = array[wall];
                    array[wall] = array[j];
                    array[j] = tmp;
                }

            }
        }
        E tmp = array[++wall];
        array[wall] = array[pivot];
        array[pivot] = tmp;
        if (pivot - start == 1) return;
        qsort(array, c,  start - 1, wall - 1); //left side
        qsort(array, c, wall, pivot); //right side
    }
}
