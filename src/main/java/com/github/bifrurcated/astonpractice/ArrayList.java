package com.github.bifrurcated.astonpractice;

import com.github.bifrurcated.astonpractice.sort.Consumer3;
import com.github.bifrurcated.astonpractice.sort.QuickSort;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class ArrayList<E> implements List<E> {

    public static final int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    private Object[] data;
    private int size;

    public ArrayList() {
        this(10);
    }

    public ArrayList(int initialCapacity) {
        data = new Object[initialCapacity];
    }

    @Override
    public void add(E element) {
        add(size, element);
    }

    @Override
    public void add(int index, E element) {
        checkIndexRange(index);
        int newSize = size + 1;
        int length = data.length;
        if (newSize > length) {
            Object[] newData;
            if (length * 2L >= MAX_ARRAY_LENGTH) {
                newData = new Object[MAX_ARRAY_LENGTH];
            } else {
                newData = new Object[length*2];
            }
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
        data[index] = element;
        size = newSize;
    }

    private void checkIndexRange(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index [" + index + "] out of range: from 0 to " + size);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        for (E el : c) {
            add(el);
        }
        return true;
    }

    @Override
    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        Objects.checkIndex(index, size);
        return (E) data[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        Objects.checkIndex(index, size);
        Object removed = data[index];
        data[index] = null;
        if (index < size-1) {
            System.arraycopy(data, index + 1, data, index, size);
        }
        size--;
        return (E) removed;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || isEmpty()) return false;
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) {
                data[i] = null;
                if (i < size-1) {
                    System.arraycopy(data, i + 1, data, i, size);
                }
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        sort(c, new QuickSort<>());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c, Consumer3<E[], Integer, Comparator<? super E>> sort) {
        if (isEmpty()) return;
        sort.accept((E[]) data, size, c);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }
}
