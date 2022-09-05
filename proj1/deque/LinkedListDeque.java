package deque;

import jh61b.junit.In;

public class LinkedListDeque<T> implements Deque<T> {

    private class Node {
        public T item;
        public Node prev;
        public Node next;


        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }
    private Node sentinel;
    private int size;
    /**
     * A constructor to create a LinkedListDeque with first item x;
     * Use on sentinel to create circular linked list
     */
    public LinkedListDeque(T x){
        sentinel = new Node(null,sentinel,sentinel);
        sentinel.next = new Node(x,sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
    }

    /**
     * A constructor to create an empty LinkedListDeque
     */
    public LinkedListDeque(){
        sentinel = new Node(null, sentinel, sentinel);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /**
     * Adds an item to the front of the deque
     */
    @Override
    public void addFirst(T item){
        if(sentinel.next == sentinel){
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
    public void addLast(T item){
        if(sentinel.next == sentinel){
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
     * Returns true if deque is empty, false otherwise
     * @return
     */
//    @Override
//    public boolean isEmpty(){
//        if(sentinel.next == sentinel){
//            return true;
//        }
//        return false;
//    }
    /**
     * Returns the number of items in the deque.
     */
    public int size(){
        return size;
    }

    /**
     *  Prints the items in the deque from first to last, separated by a space. Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque(){
        Node p = sentinel.next;
        for(int i = 0; i < size; i += 1){
            System.out.println(p.item);
            p = p.next;
        }
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     * @return
     */
    @Override
    public T removeFirst(){
        if(sentinel.next == sentinel){
            return null;
        }
        T result = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.next.prev = sentinel;
        size -= 1;
        return result;
    }
    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     */
    @Override
    public T removeLast(){
        if(sentinel.next == sentinel){
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
    public T get(int index){
        Node p = sentinel.next;
        for(int i= 0; i < size; i += 1){
            if(i == index){
                return p.item;
            }
            p = p.next;
        }
        return null;
    }

    /**
     * Create a helper method for getRecurisve
     */
    private T getRecursive(int i, Node p){
        if(i == 0){
            return p.item;
        }
        return getRecursive(i-1, p.next);
    }
    public T getRecursive(int index){
        Node p = sentinel.next;
        if(index == 0){
            return p.item;
        }
        return getRecursive(index, p);
    }

//    public static void main(String arg[]){
//
//        LinkedListDeque<Integer> L = new LinkedListDeque<Integer>(100);
//        LinkedListDeque<String> L2 = new LinkedListDeque<String>();
////        System.out.println(L.isEmpty());
////        L.printDeque();
////        System.out.println(L.removeFirst());
//        L.addLast(30);
//        L.addFirst(10);
//        L.addLast(40);
////        L2.addFirst("Wendy");
////        L2.addLast("Mia");
////        L.printDeque();
////        System.out.println(L.size);
//////        System.out.println(L.isEmpty());
////        System.out.println(L.removeFirst());
////        System.out.println(L2.removeLast());
//        for(int i = 0; i < L.size ; i += 1){
//            System.out.println(L.getRecursive(i));
//        }
//
////        System.out.println(L.getRecurisve(3));
//    }

}
