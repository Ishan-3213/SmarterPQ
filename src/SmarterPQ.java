class SmarterPriorityQueue<K extends Comparable<K>, V> {
    public static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class Node<K, V> {
        Entry<K, V> entry;
        Node<K, V> left;
        Node<K, V> right;

        public Node(Entry<K, V> entry) {
            this.entry = entry;
            this.left = null;
            this.right = null;
        }
    }

    private Node<K, V> root;
    private boolean isMinHeap;

    public SmarterPriorityQueue(boolean isMinHeap) {
        this.root = null;
        this.isMinHeap = isMinHeap;
    }

    public void toggle() {
        isMinHeap = !isMinHeap;
        heapify(root);
    }

    public Entry<K, V> removeTop() {
        if (isEmpty()) return null;
        Entry<K, V> top = root.entry;
        // Replace top entry with the last entry
        Entry<K, V> lastEntry = removeLastEntry();
        if (lastEntry != null) {
            root.entry = lastEntry;
            heapify(root);
        } else {
            root = null;
        }
        return top;
    }

    private Entry<K, V> removeLastEntry() {
        if (isEmpty()) return null;
        Node<K, V> parent = null;
        Node<K, V> current = root;
        while (current.right != null) {
            parent = current;
            current = current.right;
        }
        Entry<K, V> lastEntry = current.entry;
        if (parent == null) {
            root = null;
        } else {
            parent.right = current.left; // Removing the last node
            heapify(parent); // Heapify the tree after removal
        }
        return lastEntry;
    }

    public Entry<K, V> insert(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value);
        if (root == null) {
            root = new Node<>(entry);
        } else {
            insert(root, entry);
        }
        return entry;
    }

    private void insert(Node<K, V> node, Entry<K, V> entry) {
        if (node.left == null) {
            node.left = new Node<>(entry);
            heapify(root); // Heapify after insertion
        } else if (node.right == null) {
            node.right = new Node<>(entry);
            heapify(root); // Heapify after insertion
        } else {
            // Recursive call to find the right place for insertion
            // For simplicity, we insert into left subtree first
            insert(node.left, entry);
        }
    }

    public Entry<K, V> top() {
        if (isEmpty()) return null;
        return root.entry;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private void heapify(Node<K, V> node) {
        if (node == null || (node.left == null && node.right == null)) return;

        // Find the child with the highest or lowest priority based on the heap type
        Node<K, V> child = findMaxOrMinChild(node);

        // If the child has higher priority, swap with the parent
        if (compare(child.entry.key, node.entry.key) < 0) {
            Entry<K, V> temp = node.entry;
            node.entry = child.entry;
            child.entry = temp;

            // Recursively heapify the subtree
            heapify(child);
        }
    }

    private Node<K, V> findMaxOrMinChild(Node<K, V> parent) {
        Node<K, V> leftChild = parent.left;
        Node<K, V> rightChild = parent.right;

        if (isMinHeap) {
            if (leftChild == null) return parent;
            if (rightChild == null || compare(leftChild.entry.key, rightChild.entry.key) <= 0)
                return leftChild;
            return rightChild;
        } else {
            if (leftChild == null) return parent;
            if (rightChild == null || compare(leftChild.entry.key, rightChild.entry.key) >= 0)
                return leftChild;
            return rightChild;
        }
    }

    private int compare(K key1, K key2) {
        if (isMinHeap) {
            return key1.compareTo(key2);
        } else {
            return key2.compareTo(key1);
        }
    }
}
public class SmarterPQ {
    public static void main(String[] args) {
        // Create a min-heap SPQ
        SmarterPriorityQueue<Integer, String> minHeapSPQ = new SmarterPriorityQueue<>(true);

        // Insert entries
        minHeapSPQ.insert(10, "Ten");
        minHeapSPQ.insert(5, "Five");
        minHeapSPQ.insert(15, "Fifteen");

        // Top entry
        System.out.println("Top entry: " + minHeapSPQ.top().value);

        // Remove top entry
        System.out.println("Removed top entry: " + minHeapSPQ.removeTop().value);

        // Toggle to max-heap
        minHeapSPQ.toggle();

        // Insert entries in max-heap mode
        minHeapSPQ.insert(1, "One");
        minHeapSPQ.insert(2, "Five");
        minHeapSPQ.insert(3, "Fifteen");

        // Top entry in max-heap mode
        System.out.println("Top entry in max-heap mode: " + minHeapSPQ.top().value);

        minHeapSPQ.toggle();
        System.out.println("Top entry in min-heap mode: " + minHeapSPQ.top().value);

    }
}