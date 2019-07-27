import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] randomizedQueue;
    private int size;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        randomizedQueue = (Item[]) new Object[2];
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
     * @return number of items on the queue
     */
    public int size() {
        return size;
    }

    /**
     * Add the item to the front of the deque. If the item is null, throw an IllegalArgumentException. If the queue
     * is full (size has reached the queue's capacity, double the queue's capacity. Add the item to the queue.
     * Increment the size of the queue by 1
     *
     * @param item needed to be added to the front of the deque
     * @throws IllegalArgumentException if item is null
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("RandomizedQueue.enqueue(): Item cannot be null!");

        if (size == randomizedQueue.length)
            resize(randomizedQueue.length * 2);

        randomizedQueue[size] = item;
        size++;
    }

    /**
     * Resize the queue's array to its new capacity
     *
     * @param capacity queue's new capacity
     */
    private void resize(int capacity) {
        // For testing purpose only. Enable it will make it fail Coursera test
        // System.out.println("Resize from " + randomizedQueue.length + " to " + capacity);
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(randomizedQueue, 0, copy, 0, size);
        randomizedQueue = copy;
    }

    /**
     * Remove and return a random item. If the size of the queue is less than 1/4 of its capacity after the removal,
     * resize the queue to 1/2 of it current capacity
     *
     * @return a random item in the queue
     */
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("RandomizedQueue.sample(): Queue is empty!");
        int index = StdRandom.uniform(size);
        Item temp = randomizedQueue[index];
        randomizedQueue[index] = randomizedQueue[--size];

        if (size < randomizedQueue.length / 4)
            resize(randomizedQueue.length / 2);

        return temp;
    }

    /**
     * Return a random item
     *
     * @return a random item in the queue
     */
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("RandomizedQueue.sample(): Queue is empty!");
        int index = StdRandom.uniform(size);
        return randomizedQueue[index];
    }

    /**
     *
     * @return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int iterSize = size;
        private final Item[] copy = copy(randomizedQueue);

        private Item[] copy(Item[] origin) {
            Object[] copy = new Object[iterSize];
            System.arraycopy(origin, 0, copy, 0, iterSize);
            StdRandom.shuffle(copy);
            return (Item[]) copy;
        }

        public boolean hasNext() {
            return iterSize > 0;
        }

        public Item next() {
            if (iterSize == 0)
                throw new NoSuchElementException(
                        "RandomizedQueue.iterator().next(): There is no more element to return!");
            return copy[--iterSize];
        }

        public void remove() {
            throw new UnsupportedOperationException("RandomizedQueue.Iterator.remove(): Method is unsupported!");
        }
    }

    /**
     * Unit testing (required)
     *
     * @param args arguments got passed on while using the terminal
     */
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        int temp;

        System.out.println(
                "Testing iterator().next(): Enqueue a null item to the queue. Should throw a NoSuchElementException");
        try {
            rq.iterator().next();
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing sample(): Get a random item in the empty queue. Should throw a " +
                "NoSuchElementException");
        try {
            rq.sample();
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing dequeue(): Remove and return a random item in the empty queue. Should throw a " +
                "NoSuchElementException");
        try {
            rq.dequeue();
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println(
                "Testing enqueue(): Enqueue a null item to the queue. Should throw a IllegalArgumentException");
        try {
            rq.enqueue(null);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println(
                "Testing enqueue(): Enqueue 1-10 to the queue. This should invoke resize thrice.");
        for (int i = 1; i <= 10; i++) {
            rq.enqueue(i);
            System.out.println("Added " + i + " to the queue");
            System.out.print("RandomizedQueue: ");
            for (Integer j : rq)
                System.out.print(j + "->");
            System.out.println();
            System.out.println("Sample from RandomizedQueue: " + rq.sample());
        }
        System.out.println("Size of the queue: " + rq.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing sample(): Get a random item from the queue");
        for (int i = 0; i < 10; i++)
            System.out.print(rq.sample() + " - ");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing dequeue(): Remove and return every single item in the queue. Should invoke resize" +
                " thrice and at the end, the size of the queue should be 0");
        while (!rq.isEmpty()) {
            temp = rq.dequeue();
            System.out.println("Removed " + temp + " from deque");
            System.out.print("RandomizedQueue: ");
            for (Integer i : rq)
                System.out.print(i + "->");
            System.out.println();
        }
        System.out.println("Size of the queue: " + rq.size());
        System.out.println("-----------------------------------------------------------------------------------------");
    }
}