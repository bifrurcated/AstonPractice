package com.github.bifrurcated.astonpractice;

import com.github.bifrurcated.astonpractice.sort.BubbleSort;
import com.github.bifrurcated.astonpractice.sort.MergeSort;
import com.github.bifrurcated.astonpractice.sort.QuickSort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListTest {

    private List<String> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(10);
    }

    @AfterEach
    void tearDown() {
        list.clear();
    }

    @Test
    void testAddOneElement() {
        String text = "Hello";
        list.add(text);
        String get = list.get(0);
        assertEquals("Hello", get);
    }

    @Test
    void testAddOutOfRange() {
        String text = "Hello";
        list.add(0, text);
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> list.add(2, text),
                "Index [2] out of range: from 0 to 1"
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> list.add(-1, text),
                "Index [-1] out of range: from 0 to 1"
        );
    }

    @Test
    void testAddManyElement() {
        for (int i = 0; i < 11; i++) {
            list.add(String.valueOf(i));
        }
        assertEquals(11, list.size());
    }

    @Test
    void testAddManyElementOnIndex() {
        for (int i = 0; i < 11; i++) {
            list.add(i, String.valueOf(i));
        }
        assertEquals(11, list.size());
    }

    @Test
    void testAddAll() {
        java.util.List<String> strings = java.util.List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
        boolean added = list.addAll(strings);

        assertTrue(added);
        assertEquals(11, list.size());
        assertArrayEquals(strings.toArray(), list.toArray());
    }

    @Test
    void testClearAndIsEmpty() {
        list.add("1");
        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testGetElementIndexOutOutOfBoundsException() {
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> list.get(0),
                "Index 0 out of bounds for length 0"
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> list.get(-1),
                "Index -1 out of bounds for length 0"
        );
    }

    @Test
    void testRemoveByIndex() {
        list.add("1");
        String removed = list.remove(0);

        assertEquals("1", removed);
        assertTrue(list.isEmpty());
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> list.get(0),
                "Index 0 out of bounds for length 0"
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> list.remove(0),
                "Index 0 out of bounds for length 0"
        );

        list.add("1");list.add("2");list.add("3");
        list.remove(1);
        assertEquals("3", list.get(1));
    }

    @Test
    void testRemoveByObject() {
        list.add("1");
        boolean removed = list.remove("1");
        assertTrue(removed);
        assertFalse(list.remove(null));
        assertFalse(list.remove("2"));
        assertEquals(0, list.size());
        list.add("1");list.add("2");list.add("3");
        list.remove("2");
        assertEquals("3", list.get(1));
    }

    @Test
    void testBubbleSort() {
        list.addAll(java.util.List.of("3","1","2","4","5"));
        list.sort(String::compareTo, new BubbleSort<>());
        assertArrayEquals(java.util.List.of("1","2","3","4","5").toArray(), list.toArray());
        list.sort(Comparator.comparing(String::valueOf).reversed());
        assertArrayEquals(java.util.List.of("5","4","3","2","1").toArray(), list.toArray());
    }

    @Test
    void testQuickSort() {
        list.addAll(java.util.List.of("8","2","4","7","1", "3", "9", "6", "5"));
        list.sort(String::compareTo, new QuickSort<>());
        assertArrayEquals(java.util.List.of("1","2","3","4","5", "6", "7", "8", "9").toArray(), list.toArray());
        list.clear();
        list.addAll(java.util.List.of("6","5","1","3","8", "4", "7", "9", "2"));
        list.sort(String::compareTo, new QuickSort<>());
        assertArrayEquals(java.util.List.of("1","2","3","4","5", "6", "7", "8", "9").toArray(), list.toArray());
        list.clear();
        list.addAll(java.util.List.of("3","1","2","4","5"));
        list.sort(String::compareTo, new QuickSort<>());
        assertArrayEquals(java.util.List.of("1","2","3","4","5").toArray(), list.toArray());
    }

    @Test
    void testMergeSort() {
        list.addAll(java.util.List.of("8", "2", "5", "3", "4", "7", "6", "1"));
        list.sort(String::compareTo, new MergeSort<>());
        assertArrayEquals(java.util.List.of("1", "2", "3", "4", "5", "6", "7", "8").toArray(), list.toArray());
    }

    //@Test
    void testAddMaxArraySize() {
        //add VM option -Xms9G -Xmx9G
        for (int i = 0; i < ArrayList.MAX_ARRAY_LENGTH; i++) {
            list.add("1");
        }
        assertEquals(ArrayList.MAX_ARRAY_LENGTH, list.size());
    }
}