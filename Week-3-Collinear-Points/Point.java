/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else if (this.y == that.y) {
            return +0.0;
        } else {
            return (double) (that.y - this.y) / (that.x - this.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y < that.y) {
            return -1;
        } else if (this.y > that.y) {
            return 1;
        } else {
            return Integer.compare(this.x, that.x);
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            double s1 = slopeTo(p1);
            double s2 = slopeTo(p2);
            return Double.compare(s1, s2);
        }
    }

    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new SlopeOrder();
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */

        System.out.println("Creating an array of 3 points and 1 extra point");
        Point point1 = new Point(1, 1);
        Point point2 = new Point(0, 0);
        Point point3 = new Point(3, 2);
        Point point4 = new Point(2, 1);
        Point point5 = new Point(-1, 1);
        Point[] points = new Point[5];
        points[0] = point1;
        points[1] = point2;
        points[2] = point3;
        points[3] = point4;
        points[4] = point5;
        Point extra1 = new Point(3, -3);
        Point extra2 = new Point(3, 2);
        Point extra3 = new Point(1, 2);
        Point extra4 = new Point(1, 1);
        Point extra5 = new Point(2, 1);

        System.out.println("-----------------------------------------------");
        System.out.println("Testing sorting method");
        System.out.println("Original Order");
        for (int i = 0; i < 5; i++) {
            System.out.print(points[i] + " ");
        }
        System.out.println();
        System.out.println("Sort Order");
        Arrays.sort(points);
        for (int i = 0; i < 5; i++) {
            System.out.print(points[i] + " ");
        }
        System.out.println();

        System.out.println("-----------------------------------------------");
        System.out.println("Testing slope to method");
        double s = point3.slopeTo(extra1);
        System.out.println(s);
        s = point3.slopeTo(extra2);
        System.out.println(s);
        s = point3.slopeTo(extra5);
        System.out.println(s);
        s = point1.slopeTo(extra3);
        System.out.println(s);
        s = point1.slopeTo(extra4);
        System.out.println(s);
        s = point1.slopeTo(extra5);
        System.out.println(s);

        System.out.println("-----------------------------------------------");
        System.out.println("Testing slope order method");
        Arrays.sort(points, points[0].slopeOrder());
        for (int i = 0; i < 3; i++) {
            System.out.print(points[i] + " ");
        }
        System.out.println();

//        Point p = new Point(4, 5);
//        Point q = new Point(0, 5);
//        Point r = new Point(7, 5);
//        System.out.println(p.slopeTo(q));
//        System.out.println(p.slopeTo(r));
    }
}
