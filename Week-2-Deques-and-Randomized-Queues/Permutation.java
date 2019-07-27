import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        String input;
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> d = new RandomizedQueue<>();

        // TODO: there is way to limit the size the queue (regardless of whether it is a Deque or a RandomizedQueue)
        //  to k
        while (!StdIn.isEmpty()) {
            input = StdIn.readString();
            d.enqueue(input);
        }

        int i = 0;
        Iterator<String> dIterator =  d.iterator();
        while (i < k && dIterator.hasNext()) {
            String toPrint = dIterator.next();
            System.out.println(toPrint);
            i++;
        }
    }
}