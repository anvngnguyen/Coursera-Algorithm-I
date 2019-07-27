import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Node previous;
        Node next;
        Item item;
    }

    private Node first;
    private Node last;
    private int size;

    /**
     * Construct a empty deque
     */
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Is the queue empty?
     *
     * @return true if the size equals to 0, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return number of items on the deque
     */
    public int size() {
        return size;
    }

    /**
     * Add the item to the front of the deque. If the item is null, throw an IllegalArgumentException. Create newNode
     * that will contain the item. If the deque is empty, set first and last node to newNode. Otherwise, set
     * newNode.next equals to first and first.previous to newNode. Then set first equals to newNode and essentially make
     * newNode the first node of the deque. Increment the size of the deque by 1
     *
     * @param item needed to be added to the front of the deque
     * @throws IllegalArgumentException if item is null
     */
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Deque.addFirst(): Item cannot be null!");

        Node newNode = new Node();
        newNode.item = item;
        newNode.next = null;
        newNode.previous = null;

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.previous = newNode;
            first = newNode;
        }

        size++;
    }

    /**
     * Add the item to the back of the deque. If the item is null, throw an IllegalArgumentException. Create newNode
     * that will contain the item. If the deque is empty, set first and last node to newNode. Otherwise, set
     * newNode.previous equals to last and last.next to newNode. Then set last equals to newNode and essentially make
     * newNode the last node of the deque. Increment the size of the deque by 1
     *
     * @param item needed to be added to the back of the deque
     * @throws IllegalArgumentException if item is null
     */
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Deque.addLast(): Item cannot be null!");

        Node newNode = new Node();
        newNode.item = item;
        newNode.next = null;
        newNode.previous = null;

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            newNode.previous = last;
            last.next = newNode;
            last = newNode;
        }

        size++;
    }

    /**
     * Remove and return the first item in the deque. If the deque is empty, throw a NoSuchElementException. Retrieve
     * item from the first node of the deque. Decrement the size of the deque by 1. If the deque is empty as the
     * result, set first and last to null. Otherwise, set first.next.previous equals to null and then set first
     * equals to first.next, essentially remove the first node from the deque. In both cases, let Garbage Collector
     * handles the rest
     *
     * @return first item of the deque
     * @throws NoSuchElementException if the deque is empty
     */
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque.removeFirst(): Queue is empty!");

        Item item = first.item;
        size--;

        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            first.next.previous = null;
            first = first.next;
        }

        return item;
    }

    /**
     * Remove and return the last item in the deque. If the deque is empty, throw a NoSuchElementException. Retrieve
     * item from the last node of the deque. Decrement the size of the deque by 1. If the deque is empty as the
     * result, set first and last to null. Otherwise, set last.previous.next equals to null and then set last equals
     * to last.previous, essentially remove the last node from the deque. In both cases, let Garbage Collector
     * handles the rest
     *
     * @return last item of the deque
     * @throws NoSuchElementException if the deque is empty
     */
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque.removeLast(): Queue is empty!");

        Item item = last.item;
        size--;

        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            last.previous.next = null;
            last = last.previous;
        }

        return item;
    }


    /**
     * @return an iterator over items in order from front to back
     */
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("Deque.Iterator.next(): There is no more element to return!");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Deque.Iterator.remove(): Method is unsupported!");
        }
    }

    /**
     * Unit testing (required)
     *
     * @param args arguments got passed on while using the terminal
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        int temp;

        System.out.println(
                "Testing iterator().next(): retrieve from an empty deque. Should throw a NoSuchElementException");
        try {
            deque.iterator().next();
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");


        System.out.println("Testing addFirst(): add a null item to the deque. Should throw an " +
                "IllegalArgumentException");
        try {
            deque.addFirst(null);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing addFirst(): add 1-5 to the deque. The size of the deque should be 5");
        for (int i = 1; i <= 5; i++) {
            deque.addFirst(i);
            System.out.println("Added " + i + " to the queue");
        }
        System.out.print("Deque: ");
        for (Integer i : deque)
            System.out.print(i + "->");
        System.out.println();
        System.out.println("Size of the queue: " + deque.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing removeFirst(): remove every item from the deque using removeFirst(). The size of " +
                "the queue should now be 0");
        while (!deque.isEmpty()) {
            temp = deque.removeFirst();
            System.out.println("Remove " + temp + " from deque");
            System.out.print("Deque: ");
            for (Integer i : deque)
                System.out.print(i + "->");
            System.out.println();
        }
        System.out.println("Size of the queue: " + deque.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing removeFirst(): remove from an empty deque. Should throw a NoSuchElementException");
        try {
            deque.removeFirst();
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing addLast(): add a null item to the deque. Should throw an IllegalArgumentException");
        try {
            deque.addLast(null);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing addLast(): add 1-5 to the deque. The size of the deque should be 5");
        for (int i = 1; i <= 5; i++) {
            deque.addLast(i);
            System.out.println("Added " + i + " to the queue");
        }
        System.out.print("Deque: ");
        for (Integer i : deque)
            System.out.print(i + "->");
        System.out.println();
        System.out.println("Size of the queue: " + deque.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing removeLast(): remove every item from the deque using removeLast(). The size of " +
                "the queue should now be 0");
        while (!deque.isEmpty()) {
            temp = deque.removeLast();
            System.out.println("Remove " + temp + " from deque");
            System.out.print("Deque: ");
            for (Integer i : deque)
                System.out.print(i + "->");
            System.out.println();
        }
        System.out.println("Size of the queue: " + deque.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing removeLast(): remove from an empty deque. Should throw a NoSuchElementException");
        try {
            deque.removeLast();
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }
}
