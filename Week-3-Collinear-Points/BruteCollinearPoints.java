import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> collinearSegments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // Check whether the argument array is null
        if (points == null) {
            throw new IllegalArgumentException("BruteForce.Constructor(): Argument is null!");
        }
        // Check whether any point in the array is null
        if (Arrays.asList(points).contains(null)) {
            throw new IllegalArgumentException("BruteForce.Constructor(): Point in array cannot be null!");
        }

        int size = points.length;
        Point[] temp = new Point[size];
        System.arraycopy(points, 0, temp, 0, size);
        Arrays.sort(temp);
        for (int p = 0; p < size; p++) {
            Point pointP = temp[p];
            for (int q = p + 1; q < size; q++) {
                Point pointQ = temp[q];
                // Check whether there are duplicates in the array
                if (pointP.compareTo(pointQ) == 0) {
                    throw new IllegalArgumentException("BruteForce.Constructor(): Array cannot contain duplicates!");
                }
                for (int r = q + 1; r < size; r++) {
                    Point pointR = temp[r];
                    for (int s = r + 1; s < size; s++) {
                        Point pointS = temp[s];
                        if (isCollinear(pointP, pointQ, pointR, pointS)) {
                            LineSegment lineSeg = new LineSegment(pointP, pointS);
                            collinearSegments.add(lineSeg);
                        }
                    }
                }
            }
        }
    }

    private boolean isCollinear(Point p, Point q, Point r, Point s) {
        return Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0 && Double.compare(p.slopeTo(r), p.slopeTo(s)) == 0;
    }

    // the number of line segments
    public int numberOfSegments() {
        return collinearSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegment = new LineSegment[numberOfSegments()];
        return collinearSegments.toArray(lineSegment);
    }

    public static void main(String[] args) {
        Point[] points = new Point[6];
        points[0] = new Point(1, 1);
        points[1] = new Point(0, 0);
        points[2] = new Point(3, 2);
        points[3] = new Point(2, 1);
        points[4] = new Point(2, 2);
        points[5] = new Point(3, 3);

        // Draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}