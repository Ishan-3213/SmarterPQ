/**
 * A priority queue implementation that supports both min-heap and max-heap operations.
 * @param <K> The type of keys in the entries.
 * @param <V> The type of values in the entries.
 */
class SmarterPriorityQueue<K extends Comparable<K>, V> {
    /**
     * Represents an entry in the priority queue.
     * @param <K> The type of the key.
     * @param <V> The type of the value.
     */
    public static class Entry<K, V> {
        K key;
        V value;

        /**
         * Constructs an entry with the given key and value.
         * @param key The key of the entry.
         * @param value The value of the entry.
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Represents a node in the binary tree.
     * @param <K> The type of the key.
     * @param <V> The type of the value.
     */
    private static class Node<K, V> {
        Entry<K, V> entry;
        Node<K, V> left;
        Node<K, V> right;

        /**
         * Constructs a node with the given entry.
         * @param entry The entry of the node.
         */
        public Node(Entry<K, V> entry) {
            this.entry = entry;
            this.left = null;
            this.right = null;
        }
    }

    private Node<K, V> root;
    private boolean isMinHeap;

    /**
     * Constructs a priority queue with the specified heap type.
     * @param isMinHeap True for min-heap, false for max-heap.
     */
    public SmarterPriorityQueue(boolean isMinHeap) {
        this.root = null;
        this.isMinHeap = isMinHeap;
    }

    /**
     * Toggles the heap type between min-heap and max-heap.
     */
    public void toggle() {
        isMinHeap = !isMinHeap;
        heapify(root);
    }

    /**
     * Removes and returns the entry with the top priority.
     * @return The entry with the top priority.
     */
    public Entry<K, V> removeTop() {
        if (isEmpty()) return null;
        Entry<K, V> top = root.entry;
//        Entry<K, V> lastEntry = removeEntry(top);
//        if (lastEntry != null) {
//            root.entry = lastEntry;
//            heapify(root);
//        } else {
//            root = null;
//        }
        return removeEntry(top);
    }

    public Entry<K, V> removeEntry(Entry<K, V> entryToRemove) {
        if (isEmpty()) return null;
        Node<K, V> parent = null;
        Node<K, V> current = root;
        while (current != null) {
            if (current.entry == entryToRemove) {
                // Found the entry to remove
                break;
            }
            parent = current;
            if (compare(entryToRemove.key, current.entry.key) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (current == null) {
            // Entry not found
            return null;
        }
        Entry<K, V> removedEntry = current.entry;
        removeNode(current, parent);
        return removedEntry;
    }

    public void removeNode(Node<K, V> nodeToRemove, Node<K, V> parent) {
        if (nodeToRemove.left == null && nodeToRemove.right == null) {
            // Case 1: Node to remove has no children
            if (parent == null) {
                root = null;
            } else if (parent.left == nodeToRemove) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else if (nodeToRemove.left != null && nodeToRemove.right != null) {
            // Case 2: Node to remove has two children
            Node<K, V> successorParent = nodeToRemove;
            Node<K, V> successor = nodeToRemove.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            nodeToRemove.entry = successor.entry;
            removeNode(successor, successorParent);
        } else {
            // Case 3: Node to remove has one child
            Node<K, V> child = (nodeToRemove.left != null) ? nodeToRemove.left : nodeToRemove.right;
            if (parent == null) {
                root = child;
            } else if (parent.left == nodeToRemove) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            heapify(child); // Heapify the subtree after removal
        }
    }

    /**
     * Inserts a new entry into the priority queue.
     * @param key The key of the entry.
     * @param value The value of the entry.
     * @return The inserted entry.
     */
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
            heapify(root);
        } else if (node.right == null) {
            node.right = new Node<>(entry);
            heapify(root);
        } else {
            // Recursive call to find the right place for insertion
            // For simplicity, we insert into left subtree first
            insert(node.left, entry);
        }
    }

    /**
     * Returns the entry with the top priority without removing it.
     * @return The entry with the top priority.
     */
    public Entry<K, V> top() {
        if (isEmpty()) return null;
        return root.entry;
    }

    /**
     * Checks if the priority queue is empty.
     * @return True if the priority queue is empty, false otherwise.
     */
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

/**
 * Main class to demonstrate the usage of SmarterPriorityQueue.
 */
public class SmarterPQ {
    public static void main(String[] args) {
        SmarterPriorityQueue<Integer, String> minHeapSPQ = new SmarterPriorityQueue<>(true);
        minHeapSPQ.insert(10, "Ten");
        minHeapSPQ.insert(5, "Five");
        minHeapSPQ.insert(15, "Fifteen");
        System.out.println("Top entry: " + minHeapSPQ.top().value);
        System.out.println("Removed top entry: " + minHeapSPQ.removeTop().value);
        SmarterPriorityQueue.Entry<Integer, String> beforeToggle = minHeapSPQ.top();
        minHeapSPQ.toggle();
        SmarterPriorityQueue.Entry<Integer, String> afterToggle = minHeapSPQ.top();
        minHeapSPQ.removeEntry(beforeToggle);
        minHeapSPQ.removeEntry(afterToggle);
        minHeapSPQ.insert(1, "One");
        minHeapSPQ.insert(2, "Five");
        minHeapSPQ.insert(3, "Fifteen");
        System.out.println("Top entry in max-heap mode: " + minHeapSPQ.top().value);
        minHeapSPQ.toggle();
        System.out.println("Top entry in min-heap mode: " + minHeapSPQ.top().value);
    }
}
