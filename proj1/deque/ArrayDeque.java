package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private T nextFirst;
    private T nextLast;

    /** Creates an empty list. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        T nextFirst = null;
        T nextLast = null;
        size = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }
    /** Inserts X into the front of the list. */
    @Override
    public void addFirst(T x) {
        T[] a = (T[]) new Object[size+1];
        System.arraycopy(items, 1, a, 0, size);
        items = a;
        items[0] = x;
        nextFirst = x;
        size = size + 1;
    }
    /** Inserts X into the back of the list. */
    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(size + 100);
        }
        items[size] = x;
        nextLast = x;
        size = size + 1;
    }

    /**
     * Determine if list is empty or not
     */
//    @Override
//    public boolean isEmpty(){
//        if(size == 0){
//            return true;
//        }
//        return false;
//    }
    /** Returns the number of items in the list. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Print out each item in the Array list
     */
    @Override
    public void printDeque(){
        for(int i = 0; i < size; i += 1){
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }
    /** Returns the item from the back of the list. */
    public T getLast() {
        return nextLast;
    }
    /** Returns the first from of the list. */
    public T getFirst() {
        return nextFirst;
    }

    /**
     * Remove the first item of the list and return the removed item
     */
    @Override
    public T removeFirst(){
        if(isEmpty()){
            return null;
        }
        T First_item = getFirst();
        T[] a = (T[]) new Object[size-1];
        System.arraycopy(items, 1, a, 0, size);
        items = a;
        size -= 1;
        nextFirst = items[0];
        return First_item;
    }

    /** Deletes item from back of the list and
     * returns deleted item. */
    @Override
    public T removeLast() {
        if(isEmpty()){
            return null;
        }
        if ((size < items.length / 4) && (size > 4)) {
            resize(items.length / 4); //this part: implement usage ratio: R = size/items.length, if R < 0.25, resize to 1/4 item.length, not 1/4 size.
        }
        T x = getLast();
        items[size - 1] = null;
        size = size - 1;
        if(size != 0){
            nextLast = items[size-1];
        }else {
            nextLast = null;
        }
        return x;
    }
    /** Gets the ith item in the list (0 is the front). */
    @Override
    public T get(int i) {
        return items[i];
    }



}
