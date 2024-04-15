import java.util.Objects;

/**
 * A functional interface for comparing two objects of type K.
 *
 * @param <K> the type of objects to be compared
 */
interface ComparisonFunction<K> {
    /**
     * Compares two objects.
     *
     * @param first  the first object to be compared
     * @param second the second object to be compared
     * @return true if first is less than second, false otherwise
     */
    boolean compare(K first, K second);
}

/**
 * A class representing a smarter priority queue (SmartPriorityQueue).
 *
 * @param <K> the type of elements in this queue
 */
class SmartPriorityQueue<K extends Comparable<K>> {
    private boolean isMinHeap;
    private K[] heapArray;
    private ComparisonFunction<K> comparator;
    private int size;

    /**
     * Constructs a new SmartPriorityQueue with the specified initial capacity and compare function.
     *
     * @param capacity    the initial capacity of the heapArray
     * @param comparisonFunction the compare function
     */
    public SmartPriorityQueue(int capacity, ComparisonFunction<K> comparisonFunction) {
        this.heapArray = (K[]) new Comparable[capacity];
        this.size = 0;
        this.isMinHeap = true;
        this.comparator = comparisonFunction;
    }

    /**
     * Inserts an element into the heapArray.
     *
     * @param element the element to be inserted
     */
    public void insert(K element) {
        if (size == heapArray.length) {
            expandHeap();
        }
        heapArray[size++] = element;
        siftUp(size - 1);
    }

    /**
     * Returns the top element from the heapArray without removing it.
     *
     * @return the top element
     */
    public K top() {
        if (isEmpty()) {
            throw new IllegalStateException("SmartPriorityQueue is empty");
        }
        return heapArray[0];
    }

    /**
     * Toggles the state of the heapArray between min and max priority queue.
     */
    public void toggle() {
        isMinHeap = !isMinHeap;
        comparator = isMinHeap ? (a, b) -> a.compareTo(b) < 0 : (a, b) -> a.compareTo(b) > 0;

        // Re-heapify the entire heapArray to adjust according to the new state
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * Removes and returns the top element from the heapArray without changing its state.
     *
     * @return the removed top element
     */
    public K removeTop() {
        if (isEmpty()) {
            throw new IllegalStateException("SmartPriorityQueue is empty");
        }
        K topItem = isMinHeap ? heapArray[0] : heapArray[size - 1];
        K removedItem = heapArray[0];
        heapArray[0] = heapArray[size - 1];
        heapArray[size - 1] = null;
        size--;
        siftDown(0);
        return topItem;
    }

    /**
     * Removes and returns a specific element from the heapArray.
     *
     * @param element the element to be removed
     * @return the removed element
     */
    public K remove(K element) {
        if (isEmpty()) {
            throw new IllegalStateException("SmartPriorityQueue is empty");
        }
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heapArray[i], element)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("Element not found in SmartPriorityQueue");
        }
        K removedItem = heapArray[index];
        heapArray[index] = heapArray[size - 1];
        heapArray[size - 1] = null;
        size--;
        siftDown(index);
        return removedItem;
    }

    /**
     * Replaces an old key with a new key in the heapArray.
     *
     * @param oldKey the old key to be replaced
     * @param newKey the new key to replace the old key
     * @return the old key
     */
    public K replaceKey(K oldKey, K newKey) {
        if (isEmpty()) {
            throw new IllegalStateException("SmartPriorityQueue is empty");
        }
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heapArray[i], oldKey)) {
                heapArray[i] = newKey;
                if (comparator.compare(newKey, oldKey)) {
                    siftUp(i);
                } else {
                    siftDown(i);
                }
                break;
            }
        }
        return oldKey;
    }

    /**
     * Replaces an old value with a new value in the heapArray.
     *
     * @param oldValue the old value to be replaced
     * @param newValue the new value to replace the old value
     * @return the old value
     */
    public K replaceValue(K oldValue, K newValue) {
        if (isEmpty()) {
            throw new IllegalStateException("SmartPriorityQueue is empty");
        }
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heapArray[i], oldValue)) {
                heapArray[i] = newValue;
                break;
            }
        }
        return oldValue;
    }

    /**
     * Returns the current status of the heapArray.
     *
     * @return the current status
     */
    public String status() {
        return isMinHeap ? "Min" : "Max";
    }

    /**
     * Checks whether the heapArray is empty.
     *
     * @return true if the heapArray is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the current size of the heapArray.
     *
     * @return the current size
     */
    public int size() {
        return size;
    }

    /**
     * Returns an array representation of the heapArray.
     *
     * @return an array representation of the heapArray
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(heapArray, 0, array, 0, size);
        return array;
    }

    /**
     * Restores the heapArray property by moving the element at the specified index upwards.
     *
     * @param currentIndex the index of the element to heapify up
     */
    private void siftUp(int currentIndex) {
        while (currentIndex > 0 && comparator.compare(heapArray[currentIndex], heapArray[parentIndex(currentIndex)])) {
            swap(currentIndex, parentIndex(currentIndex));
            currentIndex = parentIndex(currentIndex);
        }
    }

    /**
     * Restores the heapArray property by moving the element at the specified index downwards.
     *
     * @param index the index of the element to heapify down
     */
    private void siftDown(int index) {
        int leftChild = leftChildIndex(index);
        int rightChild = rightChildIndex(index);
        int dominant = index;

        if (leftChild < size && comparator.compare(heapArray[leftChild], heapArray[index])) {
            dominant = leftChild;
        }
        if (rightChild < size && comparator.compare(heapArray[rightChild], heapArray[dominant])) {
            dominant = rightChild;
        }
        if (dominant != index) {
            swap(index, dominant);
            siftDown(dominant);
        }
    }

    /**
     * expandHeap the underlying array to accommodate more elements.
     */
    private void expandHeap() {
        K[] updatedHeap = (K[]) new Comparable[heapArray.length * 2];
        for (int i = 0; i < heapArray.length; i++) {
            updatedHeap[i] = heapArray[i];
        }
        heapArray = updatedHeap;
    }

    /**
     * Swaps the elements at the specified indices in the heapArray array.
     *
     * @param i the index of the first element to be swapped
     * @param j the index of the second element to be swapped
     */
    private void swap(int i, int j) {
        K temp = heapArray[i];
        heapArray[i] = heapArray[j];
        heapArray[j] = temp;
    }

    /**
     * Returns the index of the parent of the element at the specified index.
     *
     * @param index the index of the element whose parent is to be found
     * @return the index of the parent element
     */
    private int parentIndex(int index) {
        return (index - 1) / 2;
    }

    /**
     * Returns the index of the left child of the element at the specified index.
     *
     * @param index the index of the element whose left child is to be found
     * @return the index of the left child element
     */
    private int leftChildIndex(int index) {
        return 2 * index + 1;
    }

    /**
     * Returns the index of the right child of the element at the specified index.
     *
     * @param index the index of the element whose right child is to be found
     * @return the index of the right child element
     */
    private int rightChildIndex(int index) {
        return 2 * index + 2;
    }
}

/**
 * A class for testing the functionality of the SmartPriorityQueue class.
 */
public class SmarterPQ {
    public static void main(String[] args) {
        SmartPriorityQueue<Integer> heap1 = new SmartPriorityQueue<>(5, (a, b) -> a.compareTo(b) < 0);
        heap1.insert(3);
        heap1.insert(1);
        heap1.insert(5);
        heap1.insert(4);
        heap1.insert(2);



        printArray("Test Case 1: Insertion Min-Heap:", heap1.toArray());


        heap1.removeTop();
        printArray("Test Case 2: Removed top 2 items", heap1.toArray());

        SmartPriorityQueue<Integer> heap2 = new SmartPriorityQueue<>(5, (a, b) -> a.compareTo(b) > 0);
        heap2.insert(3);
        heap2.insert(1);
        heap2.insert(5);
        heap2.insert(4);
        heap2.insert(2);
        printArray("Test Case 3: Max-Heap Insertion:", heap2.toArray());

        heap2.removeTop();
        heap2.removeTop();
        printArray("Test Case 4: Removed top 2 items", heap2.toArray());


        printArray("Test Case 5: Before Toggling to Max Status", heap1.toArray());
        heap1.toggle();
        printArray("Test Case 5: After Toggling to Max Status", heap1.toArray());

        heap2.removeTop();
        printArray("Test Case 6: Removing Element", heap2.toArray());

        System.out.println("Test Case 7: Top Element: " + heap2.top());

        printArray("Test Case 8: Before Toggling to Min Status", heap2.toArray());
        heap2.toggle();
        printArray("Test Case 8: After Toggling to Min Status", heap2.toArray());


        heap2.replaceKey(3, 6);
        printArray("Test Case 9: Replacing Key 3->6 in Toggled Min-Heap", heap2.toArray());

        heap2.replaceValue(2, 8);
        printArray("Test Case 10: Replacing Value 2->8 in Min-Heap", heap2.toArray());

        System.out.println("Test Case 11: Current State: " + heap2.status());

        heap2.remove(5);
        printArray("Test Case 12: Removing an Element 5", heap2.toArray());

        System.out.println("Test Case 13: Size: " + heap2.size());

        System.out.println("Test Case 14: Is Empty? " + heap2.isEmpty());

        SmartPriorityQueue<Integer> heap3 = new SmartPriorityQueue<>(3, (a, b) -> a.compareTo(b) > 0);
        heap3.insert(1);
        heap3.insert(2);
        heap3.insert(3);
        printArray("Test Case 15: Inserting More Elements Than Initial Capacity", heap3.toArray());
        heap3.insert(5);
        heap3.insert(4);
        printArray("After Inserting More Elements 5, 25", heap3.toArray());

        System.out.println("Test Case 16: Removing Elements Until Empty");
        while (!heap3.isEmpty()) {
            heap3.removeTop();
            printArray("Removing Elements Until Empty", heap3.toArray());
        }

        SmartPriorityQueue<Integer> heap4 = new SmartPriorityQueue<>(5, (a, b) -> a.compareTo(b) < 0);
        heap4.insert(9);
        printArray("Test Case 17: Inserting into Empty Min-Heap", heap4.toArray());

        SmartPriorityQueue<Integer> heap5 = new SmartPriorityQueue<>(5, (a, b) -> a.compareTo(b) > 0);
        try {
            heap5.removeTop();
        } catch (IllegalStateException e) {
            System.out.println("Test Case 18: Exception Caught: " + e.getMessage());
        }

        SmartPriorityQueue<Integer> heap6 = new SmartPriorityQueue<>(5, (a, b) -> a.compareTo(b) < 0);
        try {
            System.out.println("Test Case 19: Top Element of Empty Heap: " + heap6.top());
        } catch (IllegalStateException e) {
            System.out.println("Test Case 19: Exception Caught: " + e.getMessage());
        }

        SmartPriorityQueue<Integer> heap7 = new SmartPriorityQueue<>(5, (a, b) -> a.compareTo(b) < 0);
        heap7.toggle();
        System.out.println("Test Case 20: Toggling an Empty Heap State: " + heap7.status());

        SmartPriorityQueue<Integer> heap8 = new SmartPriorityQueue<>(5, (a, b) -> a.compareTo(b) < 0);
        heap8.insert(6);
        printArray("Test Case 21: Removing 10 from Single-Element Min-Heap", heap8.toArray());
        heap8.removeTop();
        printArray("Test Case 21: After Removing from Single-Element Min-Heap", heap8.toArray());    }

    /**
     * Prints an array of objects.
     *
     * @param message a message to be printed before the array
     * @param array   the array to be printed
     */
    private static void printArray(String message, Object[] array) {
        System.out.print(message + ":- [");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(array[i]);
        }
        System.out.println("]");
    }
}
