package ru.bmstu.lab02;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PriorityQueueCustom<T> {
    private static final int START_CAPACITY = 16;

    private final Comparator<? super T> comparator;
    private Object[] data = new Object[START_CAPACITY];
    private int size;

    public PriorityQueueCustom() {
        this(null);
    }

    public PriorityQueueCustom(Comparator<? super T> comparator) {
        this.comparator = comparator;
    }

    public void add(T item) {
        Objects.requireNonNull(item);
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
        data[size++] = item;
        siftUp(size - 1);
    }

    public T poll() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T first = itemAt(0);
        data[0] = data[--size];
        data[size] = null;
        if (!isEmpty()) {
            siftDown(0);
        }
        return first;
    }

    public T peek() {
        return isEmpty() ? null : itemAt(0);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (compare(itemAt(parent), itemAt(i)) <= 0) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }

    private void siftDown(int i) {
        while (i < size / 2) {
            int child = 2 * i + 1;
            int right = child + 1;
            if (right < size && compare(itemAt(right), itemAt(child)) < 0) {
                child = right;
            }
            if (compare(itemAt(i), itemAt(child)) <= 0) {
                break;
            }
            swap(i, child);
            i = child;
        }
    }

    private void swap(int i, int j) {
        Object item = data[i];
        data[i] = data[j];
        data[j] = item;
    }

    @SuppressWarnings("unchecked")
    private T itemAt(int i) {
        return (T) data[i];
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super T>) a).compareTo(b);
    }
}
