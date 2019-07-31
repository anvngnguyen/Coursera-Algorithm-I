import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;
        private RectHV rect;
        private Node left, right;

        public Node(Point2D p) {
            this.p = p;
            left = null;
            right = null;
        }
    }

    /**
     * Construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * Check whether the set is empty
     *
     * @return true if the set is empty (its size equals 0). Otherwise, false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * Add a point to the set (if it is not already in the set)
     *
     * @param p the point needed to be added to the set
     * @throws IllegalArgumentException if the given point is null
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("KdTree.insert(): Argument cannot be null!");
        root = insert(p, root, null, 0, 0);
    }

    /**
     * Add a point to a sub-tree of the set. If the sub-tree is empty, create a new Node, make it the root of that
     * sub-tree and assign that point to that Node. Otherwise, compare the value of the root of the sub-tree versus
     * the value of the point (which value to use for comparison is determined by the level of the sub-tree in relative
     * to the whole tree). If the root's value is greater than the point's value, recursively call this function using
     * the point and the root's left child. If the root's value is greater or equals to the point's value, and the point
     * does not equal to the root's assigned point, recursively call this function using the point and the root's
     * right child. Increment the size of the tree after insert and return the root of the sub-tree.
     *
     * @param p      the point needed to be added to the set
     * @param node   the root of the sub-tree that will contain the given point
     * @param parent the parent of the root of the sub-tree (this one is mainly used to create the sub-region the
     *               root belongs to in relative to its parent)
     * @param level  the level of the sub-tree in relative to the whole tree
     * @param side   the value determines which sub-region the root belongs to in relative to its parent
     *               0  means there is no parent
     *               1  means the root belongs to the right/top region
     *               -1 means the root belongs to the left/bottom region
     * @return the root of the sub-tree
     */
    private Node insert(Point2D p, Node node, Node parent, int level, int side) {
        if (node == null) {
            node = new Node(p);
            if (side == 0)
                createSubRectangle(node, 0, 0, 1, 1);
            else if (side == -1)
                createLesserNode(node, parent, level);
            else
                createGreaterNode(node, parent, level);
            size++;
        } else {
            double compare = level % 2 == 0 ? Double.compare(p.x(), node.p.x()) : Double.compare(p.y(), node.p.y());
            if (compare < 0)
                node.left = insert(p, node.left, node, level + 1, -1);
            else if (compare >= 0 && !p.equals(node.p))
                node.right = insert(p, node.right, node, level + 1, 1);
        }
        return node;
    }

    private void createGreaterNode(Node node, Node parent, int level) {
        if (level % 2 == 0)
            createSubRectangle(node, parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
        else
            createSubRectangle(node, parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
    }

    private void createLesserNode(Node node, Node parent, int level) {
        if (level % 2 == 0)
            createSubRectangle(node, parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
        else
            createSubRectangle(node, parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
    }

    private void createSubRectangle(Node node, double xmin, double y, double xmax, double ymax) {
        node.rect = new RectHV(xmin, y, xmax, ymax);
    }

    /**
     * Does the set contain point p?
     *
     * @param p the point needed to be check whether it exits in the set or not
     * @return true if the point exits in the set. Otherwise, false
     * @throws IllegalArgumentException if the given point is null
     */
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("KdTree.contains(): Argument cannot be null!");
        return get(p, root, 0) != null;
    }

    /**
     * Does the sub-tree of the set contain point p? If the sub-tree is null, return null. Otherwise, compare the value
     * of the root of the sub-tree versus the value of the point (which value to use for comparison is determined by
     * the level of the sub-tree in relative to the whole tree). If the root's value is greater than the point's
     * value, recursively call this function using the point and the root's left child. If the root's value is
     * greater or equals to the point's value, and the point does not equal to the root's assigned point, recursively
     * call this function using the point and the root's right child. If the point point equals to the root's
     * assigned point, return that root
     *
     * @param p     the point needed to be check whether it exits in the set or not
     * @param node  the sub-tree that could potentially contain the given point
     * @param level the level of the sub-tree in relative to the whole tree
     * @return the node itself if it exists inside the tree. Otherwise, null
     */
    private Node get(Point2D p, Node node, int level) {
        if (node == null) return null;

        double compare = level % 2 == 0 ? Double.compare(p.x(), node.p.x()) : Double.compare(p.y(), node.p.y());
        if (compare < 0)
            return get(p, node.left, level + 1);
        else if (compare >= 0 && !p.equals(node.p))
            return get(p, node.right, level + 1);
        else
            return node;
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        draw(root, 0);
    }

    /**
     * Draw all points to standard draw. Then all the split line belongs that every point make. Continue on every
     * sub-tree until the sub-tree is null
     *
     * @param node  the sub-tree needed to be drawn
     * @param level the level of the sub-tree in relative to the whole tree
     */
    private void draw(Node node, int level) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.05);
            node.p.draw();
            String location = "(" + node.p.x() + ", " + node.p.y() + ")";
            StdDraw.text(node.p.x() + 0.05, node.p.y() + 0.05, location);
            StdDraw.setPenRadius(0.01);

            // Drawing vertical line (RED node)
            if ((level % 2) == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            // Drawing horizontal line (BLACK node)
            else {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }

            draw(node.left, level + 1);
            draw(node.right, level + 1);
        }
    }

    /**
     * Find all points that are inside the rectangle (or on the boundary)
     *
     * @param rect the rectangle needed to be searched for points
     * @return all points that are inside the rectangle (or on the boundary)
     * @throws IllegalArgumentException if the given rect is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("KdTree.range(): Argument cannot be null!");
        List<Point2D> range = new ArrayList<>();
        if (!isEmpty())
            range(rect, root, range);
        return range;
    }

    /**
     * Find all points that are inside the rectangle (or on the boundary) of the sub-tree. If the rectangle contains
     * the point, add that point to the list. Than, for every child of the root of the sub-tree, if the child is not
     * null and the rectangle intersects with the child's rectangle, recursively call this function using the child
     * node and the rectangle.
     *
     * @param rect         the rectangle needed to be searched for points
     * @param node            the sub-tree that potentially contains the points that are inside the given rectangle (or
     *                     on the boundary)
     * @param rangeMatches the list contains all the points that are inside the given rectangle (or on the boundary)
     */
    private void range(RectHV rect, Node node, List<Point2D> rangeMatches) {
        if (rect.contains(node.p)) rangeMatches.add(node.p);
        if (node.right != null && rect.intersects(node.right.rect))
            range(rect, node.right, rangeMatches);
        if (node.left != null && rect.intersects(node.left.rect))
            range(rect, node.left, rangeMatches);
    }

    /**
     * Find the nearest neighbor in the set to the given point
     *
     * @param p the given point
     * @return a nearest neighbor in the set to point p, null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("KdTree.nearest(): Argument cannot be null!");

        if (isEmpty())
            return null;
        return nearest(p, root, root.p, Double.POSITIVE_INFINITY, 0);
    }

    /**
     * Find the nearest neighbor in the sub-tree to the given point. If the sub-tree is either null or its rectangle
     * distance to the given point is higher than the minimum distance that have been found so far, return null.
     * Otherwise, calculate the distance between the root of the sub-tree and the given point. If the calculated
     * distance is less than the minimum distance, update the minimum distance and the nearest neighbor. Then, based
     * on the given point's relative position to the root of the sub-tree (left/bottom vs right/top), recursively
     * call this function using the root's left or right child node respectively. If the returned result is not null,
     * that means there is a point in the children node of the root that is closer to the given point. If that is the
     * case, update the minimum distance and nearest neighbor again. In case the distance between the other branch's
     * rectangle distance is less than the minimum distance, check the other branch by recursively calling this
     * function using the other child of the root as well. Update once again if a new neighbor has been found and
     * return the nearest neighbor.
     *
     * @param p the given point
     * @param node the sub-tree that could potentially contain the nearest neighbor to point p
     * @param nearestPoint the nearest neighbor to point p that has been found so far
     * @param minDistance the minimum distance between the nearest neighbor and point p so far
     * @param level the level of the sub-tree in relative to the whole tree
     * @return the close point in the sub-tree to point p, null if sub-tree is either empty or its rectangle distance
     * to point p is higher or equals to the minimum distance
     */
    private Point2D nearest(Point2D p, Node node, Point2D nearestPoint, double minDistance, int level) {
        if (node == null) return null;
        if (node.rect.distanceSquaredTo(p) >= minDistance) return null;

        double distance = node.p.distanceSquaredTo(p);
        double closestDistance = distance < minDistance ? distance : minDistance;
        Point2D closestPoint = distance < minDistance ? node.p : nearestPoint;

        Point2D currentPoint;
        double compare = level % 2 == 0 ? Double.compare(p.x(), node.p.x()) : Double.compare(p.y(), node.p.y());
        if (compare < 0) {
            currentPoint = nearest(p, node.left, closestPoint, closestDistance, level + 1);
            if (currentPoint != null && currentPoint != closestPoint) {
                closestDistance = currentPoint.distanceSquaredTo(p);
                closestPoint = currentPoint;
            }
            if (node.right != null && node.right.rect.distanceSquaredTo(p) < closestDistance)
                currentPoint = nearest(p, node.right, closestPoint, closestDistance, level + 1);
        } else {
            currentPoint = nearest(p, node.right, closestPoint, closestDistance, level + 1);
            if (currentPoint != null && currentPoint != closestPoint) {
                closestDistance = currentPoint.distanceSquaredTo(p);
                closestPoint = currentPoint;
            }
            if (node.left != null && node.left.rect.distanceSquaredTo(p) < closestDistance)
                currentPoint = nearest(p, node.left, closestPoint, closestDistance, level + 1);
        }

        if (currentPoint != null)
            closestPoint = currentPoint;

        return closestPoint;
    }

    /**
     * Unit testing (optional)
     *
     * @param args arguments got passed on while using the terminal
     */
    public static void main(String[] args) {
        KdTree pointSET = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.6);
        Point2D neighbor = new Point2D(0.8, 0.3);

        System.out.println("Testing size(): An empty set should have a size of 0");
        System.out.println(pointSET.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing isEmpty(): An empty set should return true");
        System.out.println(pointSET.isEmpty());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing contains(): An empty set should return false");
        System.out.println(pointSET.contains(neighbor));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing nearest(): An empty set should return null");
        System.out.println(pointSET.nearest(neighbor));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing range(): An empty set should return an empty list");
        RectHV rectangle = new RectHV(0.25, 0.25, 0.75, 0.75);
        for (Point2D p : pointSET.range(rectangle))
            System.out.println(p);
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing size(): After adding 1 Point2D to Set, it should have the size of 1");
        pointSET.insert(point1);
        System.out.println(pointSET.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing isEmpty(): After adding point1 to Set, it should no longer be empty");
        System.out.println(pointSET.isEmpty());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing contains(): After adding point1 to Set, it should contains point1");
        System.out.println(pointSET.contains(point1));
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

        Point2D point2 = new Point2D(0.2, 0.1);
        Point2D point3 = new Point2D(0.25, 0.25);
        Point2D point4 = new Point2D(0.76, 0.23);
        pointSET.insert(point2);
        pointSET.insert(point3);
        pointSET.insert(point4);
        pointSET.insert(neighbor);

        System.out.println("Testing size(): After adding adding several points to the set , its size should be 5");
        System.out.println(pointSET.size());
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing contains(): After adding adding several points to the set, it should contain  " +
                "every one of them except neighbor");
        System.out.println(pointSET.contains(point1));
        System.out.println(pointSET.contains(point2));
        System.out.println(pointSET.contains(point3));
        System.out.println(pointSET.contains(point4));
        System.out.println(pointSET.contains(neighbor));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing nearest(): After adding several points to the set, the nearest neighbor of " +
                "neighbor within the tree should be neighbor");
        System.out.println(pointSET.nearest(neighbor));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Testing range(): After adding several to the set, the returned list should contain " +
                "point1 + point2");
        for (Point2D p : pointSET.range(rectangle))
            System.out.println(p);
        System.out.println("-----------------------------------------------------------------------------------------");
    }
}
