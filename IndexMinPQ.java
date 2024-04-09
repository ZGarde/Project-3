

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key> {
    private Key[] keys;
    private int[] pq;
    private int[] qp;
    private int n;
    private Comparator<Key> comparator;

    public IndexMinPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        keys = (Key[]) new Object[initCapacity + 1];
        pq = new int[initCapacity + 1];
        qp = new int[initCapacity + 1];
        for (int i = 0; i <= initCapacity; i++)
            qp[i] = -1;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(int i) {
        validateIndex(i);
        return qp[i] != -1;
    }

    public int size() {
        return n;
    }

    public void insert(int i, Key key) {
        validateIndex(i);
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        if (n == keys.length - 1) resize(2 * keys.length);
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }

    public int minIndex() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    public Key minKey() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return keys[pq[1]];
    }

    public int delMin() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = pq[1];
        exch(1, n--);
        sink(1);
        qp[min] = -1;
        keys[min] = null;
        pq[n+1] = -1;
        return min;
    }

    public Key keyOf(int i) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return keys[i];
    }

    public void changeKey(int i, Key key) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    public void delete(int i) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= pq.length) throw new IllegalArgumentException("index >= capacity: " + i);
    }

    private boolean greater(int i, int j) {
        return comparator.compare(keys[pq[i]], keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private void resize(int capacity) {
        assert capacity > n;
        Key[] tempKeys = (Key[]) new Object[capacity + 1];
        int[] tempPq = new int[capacity + 1];
        int[] tempQp = new int[capacity + 1];
        for (int i = 0; i <= n; i++) {
            tempKeys[i] = keys[i];
            tempPq[i] = pq[i];
            tempQp[i] = qp[i];
        }
        for (int i = n + 1; i <= capacity; i++)
            tempQp[i] = -1;
        keys = tempKeys;
        pq = tempPq;
        qp = tempQp;
    }
}
