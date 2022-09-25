package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V> {
    private Node root;
    private class Node {
        private K key;  //sorted by key
        private V val;  //associated data, can be anything
        private Node left, right;
        private int size;

        public Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    /**
     * Initializes an empty symbol table
     */
    public BSTMap() {

    }

    /**
     * Return true if this symbol table is empty
     */
    private boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Return the number of key-value pairs in this symbol table
     */
    @Override
    public int size() {
        return size(root);
    }

    /**
     * Returns the number of key-value pairs in this symbol table
     */
    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        else return x.size;
    }

    /**
     * return positive if this.key larger than other key
     * return 0 if this .key equal to other key
     * return negative if this key smaller than other key
     */
    private int compareTo(K otherKey) {
        return this.compareTo(otherKey);
    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        if (root == null) {
            return;
        }
        root.size = 0;
        root = null;
    }
    /**
     *  Returns true if this map contains a mapping for the specified key.
     *  */
    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        Node x = find(root, key);
        if (x == null) {
            return false;
        }
        return true;
    }
    private Node find(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        else if (cmp < 0) {
            return find(x.left, key);
        }
            return find(x.right, key);
    }

    /**
     * Returns the value associated with the given key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        }
        else if (cmp > 0) {
            return get(x.right, key);
        }
        else return x.val;
    }


    /**
     * Inserts the specified key-value pair into the symbol table,
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        root = put(root, key, value);
    }
    private Node put(Node x, K key, V value) {
        if (x== null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, value);
        } else if (cmp > 0) {
            x.right = put(x.right, key, value);
        } else {
            x.val = value;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * Dont't know how to do this, copied from Github
     */
    @Override
    public Set<K> keySet() {
//        throw new NoSuchElementException("no such key exist");
        Set<K> hashSet = new HashSet<K>();
        addKeys(root, hashSet);
        return hashSet;
    }
    private void addKeys(Node x, Set<K> s) {
        if (x == null) {
            return;
        }
        s.add(x.key);
        addKeys(x.left, s);
        addKeys(x.right, s);
    }

    /**
     * Removes the mapping for the specified key from this map if present
     */
    @Override
    public V remove(K key) {
        if (key == null || !containsKey(key)) {
            throw new NoSuchElementException("no such key exist");
        }
        V value = get(key);
        root = remove(root, key, value);
        return value;
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value.
     */
    @Override
    public V remove(K key, V value) {
        if (key == null || !containsKey(key)) {
            throw new NoSuchElementException("no such key exist");
        }
        root = remove(root, key, value);
        return value;
    }
    private Node remove(Node x, K key, V value) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = remove(x.left, key, value);
        } else if (cmp > 0) {
            x.right = remove(x.right, key, value);
        } else {
            if (x.right == null) {
                return x.left;
            }
            if (x.left == null) {
                return x.right;
            }
            Node t = x;
            x = min(t.right);
            x.right = removeMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * remove the largest item from tree
     */
    private void removeMax() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        root = removeMax(root);
    }
    private Node removeMax(Node x) {
        if (x.right == null) {
            return x.left;
        }
        x.right = removeMax(x.right);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * remove the smallest item from tree
     */
    private void removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        root = removeMin(root);
    }
    private Node removeMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = removeMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * find the smallest key in the tree;
     */
    private Node min(Node x) {
        if (x.left == null) {
            return x;
        }
        return min(x.right);
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("does not support this operation");
    }

    public void printInOrder() {
        if (root == null) {
            return;
        }
        printInOrder(root);
    }
    private void printInOrder(Node x) {
            if (x == null) {
                return;
            }
            printInOrder(x.left);
            System.out.println("key is " + x.key.toString() + "  value is  " + x.val.toString());
            printInOrder(x.right);

    }

}
