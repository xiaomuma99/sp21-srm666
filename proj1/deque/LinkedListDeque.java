package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class Node {
        private T item;
        private Node prev;
        private Node next;


        Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }
    private Node sentinel;
    private int size;
    /**
     * A constructor to create an empty LinkedListDeque
     */
    public LinkedListDeque() {
        sentinel = new Node(null, sentinel, sentinel);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /**
     * Adds an item to the front of the deque
     */
    @Override
    public void addFirst(T item) {
        if (sentinel.next == sentinel) {
            sentinel.next  = new Node(item, sentinel, sentinel.next);
            sentinel.prev = sentinel.next;
            size += 1;
            return;
        }
        sentinel.next  = new Node(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    /**
     * Add an item to the last of the deque
     */
    @Override
    public void addLast(T item) {
        if (sentinel.next == sentinel) {
            sentinel.prev.next = new Node(item, sentinel.prev, sentinel);
            sentinel.prev = sentinel.next;
            size += 1;
            return;
        }
        sentinel.prev.next = new Node(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
        size += 1;

    }
    /**
     * Returns the number of items in the deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     *  Prints the items in the deque from first to last, separated by a space.
     *  Once all the items have been printed, print out a new line.
     */

    @Override
    public void printDeque() {
        Node p = sentinel.next;
        for (int i = 0; i < size; i += 1) {
            System.out.println(p.item);
            p = p.next;
        }
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     * @return
     */

    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        T result = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return result;
    }
    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     */

    @Override
    public T removeLast() {
        if (sentinel.next == sentinel) {
            return null;
        }
        T result = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return result;
    }

    /**
     * Gets the item at the given index use iteration, not recursion
     */

    @Override
    public T get(int index) {
        Node p = sentinel.next;
        for (int i = 0; i < size; i += 1) {
            if (i == index) {
                return p.item;
            }
            p = p.next;
        }
        return null;
    }

    /**
     * Create a helper method for getRecurisve
     */
    private T getRecursiveHelper(int i, Node p) {
        if (i == 0) {
            return p.item;
        }
        if (p == sentinel) {
            return null;
        }
        return getRecursiveHelper(i - 1, p.next);
    }
    public T getRecursive(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        return getRecursiveHelper(index, sentinel.next);
    }

    /**
     * Something I don't know, copied from Github
     * @return
     */
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /**
     * Returns whether or not the parameter o is equal to the Deque.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (other.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (other.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }
    private class LinkedListDequeIterator implements Iterator<T> {
        private Node p;

        LinkedListDequeIterator() {
            p = sentinel.next;
        }

        public boolean hasNext() {
            return p != sentinel;
        }

        public T next() {
            T item = p.item;
            p = p.next;
            return item;
        }
    }

}
