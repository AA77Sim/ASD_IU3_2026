package ru.bmstu.lab02;

import java.util.Objects;

public class HashMapCustom<K, V> {
    private static final int START_CAPACITY = 16;
    private static final Object DELETED = new Object();
    private static final Object NULL_KEY = new Object();

    private Object[] keys = new Object[START_CAPACITY];
    private Object[] values = new Object[START_CAPACITY];
    private int size;
    private int used;

    public void put(K key, V value) {
        Object storedKey = maskNull(key);
        int i = findSlot(storedKey);
        if (keys[i] == null || keys[i] == DELETED) {
            int limit = capacity() - capacity() / 4;
            if (size == limit) {
                resize(capacity() * 2);
                i = findSlot(storedKey);
            } else if (keys[i] == null && used == capacity() - capacity() / 8) {
                resize(capacity());
                i = findSlot(storedKey);
            }
            if (keys[i] == null) {
                used++;
            }
            keys[i] = storedKey;
            size++;
        }
        values[i] = value;
    }

    @SuppressWarnings("unchecked")
    public V get(K key) {
        int i = findKey(maskNull(key));
        return i < 0 ? null : (V) values[i];
    }

    public boolean containsKey(K key) {
        return findKey(maskNull(key)) >= 0;
    }

    public boolean remove(K key) {
        int i = findKey(maskNull(key));
        if (i < 0) {
            return false;
        }
        keys[i] = DELETED;
        values[i] = null;
        size--;
        return true;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return keys.length;
    }

    private Object maskNull(K key) {
        return key == null ? NULL_KEY : key;
    }

    private int findKey(Object key) {
        int i = findSlot(key);
        return keys[i] == null || keys[i] == DELETED ? -1 : i;
    }

    private int findSlot(Object key) {
        long hash = key == NULL_KEY ? 0 : key.hashCode();
        int i = (int) (Math.abs(hash) % capacity());
        int deleted = -1;
        while (keys[i] != null) {
            if (keys[i] == DELETED) {
                if (deleted < 0) {
                    deleted = i;
                }
            } else if (Objects.equals(key, keys[i])) {
                return i;
            }
            i = (i + 1) % capacity();
        }
        return deleted < 0 ? i : deleted;
    }

    private void resize(int capacity) {
        Object[] oldKeys = keys;
        Object[] oldValues = values;
        keys = new Object[capacity];
        values = new Object[capacity];
        for (int i = 0; i < oldKeys.length; i++) {
            Object key = oldKeys[i];
            if (key != null && key != DELETED) {
                int j = findSlot(key);
                keys[j] = key;
                values[j] = oldValues[i];
            }
        }
        used = size;
    }
}
