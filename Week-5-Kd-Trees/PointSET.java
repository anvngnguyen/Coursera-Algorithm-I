import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> treeSet;

    /**
     * Initialize an empty set
     */
    public PointSET() {
        treeSet = new TreeSet<>();
    }

    /**
     * Check to see if the set is empty
     *
     * @return true if the set is empty, false otherwise
     */
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return treeSet.size();
    }

    /**
     * Add the point to the set (if it is not already in the set)
     *
     * @param p the given point needed to be added
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("PointSET.insert(): Argument cannot be null!");

        treeSet.add(p);
    }

    /**
     * Check whether if the set contains the given point
     *
     * @param p the given point
     * @return true if the set contains p, false otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("PointSET.contains(): Argument cannot be null!");

        return treeSet.contains(p);
    }

    /**
     * Draw all the points in the set
     */
    public void draw() {
        for (Point2D p : treeSet)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)

    /**
     * Find any point that lies within or on the given rectangle by checking if said rectangle contains any of the
     * point in the set
     *
     * @param rect the given rectangle
     * @return a list containing all points that are inside the given rectangle (or on the boundary of said rectangle)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("PointSET.range(): Argument cannot be null!");

        List<Point2D> container = new ArrayList<>();
        for (Point2D p : treeSet) {
            if (rect.contains(p))
                container.add(p);
        }

        return container;
    }

    /**
     * Find the nearest neighbor to a given point by calculating and comparing the distance between every single point
     * in the Set to the given point
     *
     * @param p the given point
     * @return a nearest neighbor in the set to point p, null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("PointSET.nearest(): Argument cannot be null!");

        Point2D nearestNeighbor = null;
        double minDistance = Double.POSITIVE_INFINITY;
        double distance;
        for (Point2D point : treeSet) {
            distance = point.distanceTo(p);
            if (distance < minDistance) {
                nearestNeighbor = point;
                minDistance = distance;
            }
        }

        return nearestNeighbor;
    }

    /**
     * Unit testing (optional)
     *
     * @param args arguments got passed on while using the terminal
     */
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();

        System.out.println("Testing size(): An empty set should have a size of 0");
        System.out.println(pointSET.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing isEmpty(): An empty set should return true");
        System.out.println(pointSET.isEmpty());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing nearest(): An empty set should return null");
        Point2D neighbor = new Point2D(1, 0);
        System.out.println(pointSET.nearest(neighbor));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing range(): An empty set should return an empty list");
        RectHV rectangle = new RectHV(0, 0, 5, 5);
        for (Point2D p : pointSET.range(rectangle))
            System.out.println(p);
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing size(): After adding 1 Point2D to Set, it should have the size of 1");
        Point2D point1 = new Point2D(2, 4);
        pointSET.insert(point1);
        System.out.println(pointSET.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing isEmpty(): After adding point1 to Set, it should no longer be empty");
        System.out.println(pointSET.isEmpty());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing nearest(): After adding point1 to Set, the nearest neighbor of neighbor within " +
                "the tree should be point1");
        System.out.println(pointSET.nearest(neighbor));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing range(): After adding point1 to Set, the returned list should contain point1 as " +
                "it is inside the rectangle");
        for (Point2D p : pointSET.range(rectangle))
            System.out.println(p);
        System.out.println("-----------------------------------------------------------------------------------------");

        Point2D point2 = new Point2D(1, 2);
        Point2D point3 = new Point2D(1, -2);
        Point2D point4 = new Point2D(5, -5);
        pointSET.insert(point2);
        pointSET.insert(point3);
        pointSET.insert(point4);
        pointSET.insert(neighbor);

        System.out.println("Testing nearest(): After adding several points to the set (including the neighbor itself)" +
                ", the nearest neighbor of neighbor within the tree should be neighbor");
        System.out.println(pointSET.nearest(neighbor));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing range(): After adding several to the set, the returned list should contain " +
                "point1 + point2 + neighbor");
        for (Point2D p : pointSET.range(rectangle))
            System.out.println(p);
        System.out.println("-----------------------------------------------------------------------------------------");

    }
}
