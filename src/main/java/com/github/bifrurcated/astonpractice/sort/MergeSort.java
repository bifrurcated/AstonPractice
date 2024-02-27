package com.github.bifrurcated.astonpractice.sort;

import java.util.Comparator;

public class MergeSort<E> implements Consumer3<E[], Integer, Comparator<? super E>> {
    @Override
    public void accept(E[] array, Integer size, Comparator<? super E> c) {
        mergeSort(array, size, c);
    }

    private void mergeSort(E[] array, Integer size, Comparator<? super E> c) {
        if (size <= 1) return;
        int mid = size / 2;
        E[] leftArray = (E[]) new Object[mid];
        E[] rightArray = (E[]) new Object[size - mid];

        int i = 0; //left array
        int j = 0; //right array
        for (; i < size; i++) {
            if (i < mid) {
                leftArray[i] = array[i];
            } else {
                rightArray[j] = array[i];
                j++;
            }
        }
        mergeSort(leftArray, leftArray.length, c);
        mergeSort(rightArray, rightArray.length, c);
        merge(leftArray, rightArray, array, size, c);
    }

    private void merge(E[] leftArray, E[] rightArray, E[] array, Integer size, Comparator<? super E> c) {
        int leftSize = size / 2;
        int rightSize = size - leftSize;
        int i = 0, l = 0, r = 0;

        while (l < leftSize && r < rightSize) {
            if (c.compare(leftArray[l], rightArray[r]) < 0) {
                array[i] = leftArray[l];
                i++;
                l++;
            } else {
                array[i] = rightArray[r];
                i++;
                r++;
            }
        }

        while (l < leftSize) {
            array[i] = leftArray[l];
            i++;
            l++;
        }
        while (r < rightSize) {
            array[i] = rightArray[r];
            i++;
            r++;
        }
    }
}
