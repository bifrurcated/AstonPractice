package com.github.bifrurcated.astonpractice;

import com.github.bifrurcated.astonpractice.sort.Consumer3;

import java.util.Collection;
import java.util.Comparator;

public interface List<E> {
    void add(E element);
    void add(int index, E element);
    boolean addAll(Collection<? extends E> c);
    void clear();
    E get(int index);
    boolean isEmpty();
    E remove(int index);
    boolean remove(Object o);
    int size();
    void sort(Comparator<? super E> c);
    void sort(Comparator<? super E> c, Consumer3<E[], Integer, Comparator<? super E>> sort);
    Object[] toArray();
}
