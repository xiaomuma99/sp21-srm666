package hashmap;

import java.sql.Array;
import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Morris Ma
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }
    private class MyHashMapIterator implements Iterator<K> {
        private final Iterator<Node> nodeIterator = new MyHashMapNodeIterator();
        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        @Override
        public K next() {
            return nodeIterator.next().key;
        }
    }
    private class MyHashMapNodeIterator implements Iterator<Node> {
        private final Iterator<Collection<Node>> bucketsIterator = Arrays.stream(buckets).iterator();
        private Iterator<Node> currentBucketIterator;
        private int nodesNum = size;
        @Override
        public boolean hasNext() {
            return  nodesNum > 0;
        }

        @Override
        public Node next() {
            if (currentBucketIterator == null || !currentBucketIterator.hasNext()) {
                Collection<Node> currentBucket = bucketsIterator.next();
                while (currentBucket.size() == 0) {
                    currentBucket = bucketsIterator.next();
                }
                currentBucketIterator = currentBucket.iterator();
            }
            nodesNum -= 1;
            return currentBucketIterator.next();
        }
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets; //array of linked-list symbol tables;
    // You should probably define some more!
    private int size; // number of key-value pairs
    private static final double MAX_LOAD_FACTOR = 0.75;  // hash load factor
    private static final int INIT_CAPACITY = 16;
    private final double maxLoadFactor;
    /** Constructors */
    public MyHashMap() {
        this(INIT_CAPACITY,MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize,MAX_LOAD_FACTOR);

    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        size = 0;
        maxLoadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        //Collection<Node>[] table = new ArrayList[tableSize]; //my code would have issue
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

        // TODO: Implement the methods of the Map61B Interface below
        // Your code won't compile until you do so!

        @Override
        public void clear () {
            buckets = createTable(INIT_CAPACITY);
            this.size = 0;
        }
        /** Returns true if this map contains a mapping for the specified key. */
        @Override
        public boolean containsKey (K key){
            if (key == null) {
                throw new IllegalArgumentException("argument to contains() is null");
            }
            return get(key) != null;
        }
        /**
         * Returns the value to which the specified key is mapped, or null if this
         * map contains no mapping for the key.
         */
        @Override
        public V get (K key){
            if (key == null) {
                throw new IllegalArgumentException("argument to get() is null");
            }
            int index = hash(key);
            if (getNode(key, index) == null) {
                return null;
            }
            return getNode(key, index).value;

        }
        private Node getNode(K key, int bucketIndex) {
            for (Node node : buckets[bucketIndex]) {
                if(node.key.equals(key)) {
                    return node;
                }
            }
            return null;
        }
        /** Returns the number of key-value mappings in this map. */
        @Override
        public int size () {
            return size;
        }

        /**
         * Associates the specified value with the specified key in this map.
         * If the map previously contained a mapping for the key,
         * the old value is replaced.
         */
        @Override
        public void put (K key, V value){
            if (key == null) {
                throw new IllegalArgumentException("first argument to put() is null");
            }
            int bucketIndex = hash(key);
            Node node = getNode(key, bucketIndex);
            if (node != null) {
                node.value = value;
                return;
            }
            node = createNode(key, value);
            buckets[bucketIndex].add(node);
            size += 1;
            if (hasReachedMaxLoad()) {
                resize(2 * buckets.length);
            }
        }
        private boolean hasReachedMaxLoad() {
            return (double) (size / buckets.length) > MAX_LOAD_FACTOR;
        }

        /**
         * resize the hash table to have the given number
         * rehashing all of the keys
         */
        private void resize (int capacity){
//            MyHashMap<K, V> temp = new MyHashMap<>(x);
//            Iterator<K> iterator = this.iterator();
//            while (iterator.hasNext()) {
//                temp.put(iterator.next(), get(iterator.next()));
//            }
//            this.initialSize = temp.initialSize;
//            this.size = temp.size;
//            this.buckets = temp.buckets;
            Collection<Node>[] newBuckets= createTable(capacity);
            Iterator<Node> nodeIterator = new MyHashMapNodeIterator();
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.next();
                int bucketIndex = hash(node.key, newBuckets);
                newBuckets[bucketIndex].add(node);
            }
            buckets = newBuckets;

        }

        /** Returns a Set view of the keys contained in this map. */
        @Override
        public Set<K> keySet () {
            HashSet<K> set = new HashSet<>();
            for (K key : this) {
                set.add(key);
            }
            return set;
        }

        /**
         * hash function for keys - returns value between 0 and initialSize-1
         */
        private int hash (K key){
            return hash(key, buckets);

        }
        private int hash(K key, Collection<Node>[] table) {
            int x = key.hashCode();
            return Math.floorMod(x, table.length);
        }

        /**
         * Removes the mapping for the specified key from this map if present.
         * Not required for Lab 8. If you don't implement this, throw an
         * UnsupportedOperationException.
         */
        @Override
        public V remove (K key){
            int bucketIndex = hash(key);
            Node node = getNode(key, bucketIndex);
            if (node == null) {
                return null;
            }
            buckets[bucketIndex].remove(node);
            size -= 1; // I forgot this;
            return node.value;
        }

        /**
         * Removes the entry for the specified key only if it is currently mapped to
         * the specified value. Not required for Lab 8. If you don't implement this,
         * throw an UnsupportedOperationException.
         */
        @Override
        public V remove (K key, V value){
            int bucketIndex = hash(key);
            Node node = getNode(key, bucketIndex);
            if (node == null) {
                return null;
            }
            buckets[bucketIndex].remove(node);
            size -= 1; // I forgot this;
            return value;
        }
}
