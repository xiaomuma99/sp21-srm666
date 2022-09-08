package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /** Creates an empty list. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
        size = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int firstLocation = Math.abs(capacity - size) / 2;
        System.arraycopy(items, nextFirst + 1, a, firstLocation, size);
        items = a;
        nextFirst = firstLocation - 1;
        nextLast = firstLocation + size;
    }
    /** Gets the ith item in the list (0 is the front). */
    @Override
    public T get(int i) {
        if (i < 0 || i > size - 1) {
            return null;
        }
        return (items[i + nextFirst + 1]);

    }

    /** Inserts X into the front of the list. */
    @Override
    public void addFirst(T x) {
        if (isEmpty()) {
            resize(8);
            items[nextFirst] = x;
            nextFirst -= 1;
            size += 1;
            return;
        }
        if (nextFirst == -1 && size == 1) {
            resize(8);
        }
        items[nextFirst] = x;
        size = size + 1;
        nextFirst -= 1;
        if (nextFirst == -1) {
            resize(size * 2);
        }
    }
    /** Inserts X into the back of the list. */
    @Override
    public void addLast(T x) {
        if (isEmpty()) {
            resize(8);
            items[nextLast] = x;
            size += 1;
            nextLast += 1;
            return;
        }
        if (nextLast == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        nextLast += 1;
        size = size + 1;

    }
    /** Returns the number of items in the list. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Print out each item in the Array list
     */
    @Override
    public void printDeque() {
        for (int i = 0; i < size; i += 1) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    /**
     * Remove the first item of the list and return the removed item
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T firstItem = items[nextFirst + 1];
        items[nextFirst + 1] = null;
        size -= 1;
        nextFirst += 1;
        if (size < items.length / 4 && size >= 4) {
            resize(size * 2);
        }
        return firstItem;
    }

    /** Deletes item from back of the list and
     * returns deleted item. */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T lastItem = items[nextLast - 1];
        items[nextLast - 1] = null;
        nextLast -= 1;
        size = size - 1;
        if (size < items.length / 4 && size >= 4) {
            resize(size * 2);
        }
        return lastItem;
    }
    /**
     * Returns whether or not the parameter o is equal to the Deque.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> L = (Deque<T>) o;
        if (L.size() != size) {
            return false;
        }
        int index = nextFirst + 1;
        for (int i = 0; i < size; i++) {
            if (!(items[index].equals(L.get(i)))) {
                return false;
            }
            index = index + 1;
        }
        return true;
    }
    /**
     * The Deque objects we’ll make are iterable (i.e. Iterable<T>)
     * so we must provide this method to return an iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int index;

        ArrayDequeIterator() {
            index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public T next() {
            T item = get(index);
            index += 1;
            return item;
        }
    }
}
