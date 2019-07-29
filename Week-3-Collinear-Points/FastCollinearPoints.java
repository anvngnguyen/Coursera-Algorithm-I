import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FastCollinearPoints {

    private final List<LineSegment> collinearSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Fast.Constructor(): Array cannot be null!");
        }
        if (Arrays.asList(points).contains(null)) {
            throw new IllegalArgumentException("Fast.Constructor(): Array cannot contain null!");
        }

        int size = points.length;
        Point[] pointsClone = points.clone();
        Arrays.sort(pointsClone);
        Point[] pointsCloneForSlopeOrder = new Point[size];

        for (int p = 0; p < size; p++) {
            System.arraycopy(pointsClone, p, pointsCloneForSlopeOrder, p, size - p);
            Point pointP = pointsClone[p];
            Arrays.sort(pointsCloneForSlopeOrder, p + 1, size, pointP.slopeOrder());
            Arrays.sort(pointsCloneForSlopeOrder, 0, p, pointP.slopeOrder());

            int count = 0;
            double previousSlope = Double.NEGATIVE_INFINITY;
            for (int i = p + 1; i < size; i++) {
                double slope = pointP.slopeTo(pointsCloneForSlopeOrder[i]);
                if (slope == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("Fast.Constructor(): Array cannot contain duplicates!");
                }
                if (Double.compare(slope, previousSlope) == 0) {
                    count++;
                }
                if (count >= 2 && (Double.compare(slope, previousSlope) != 0 || i == size - 1)) {
                    Arrays.sort(pointsCloneForSlopeOrder, i - count, i);
                    Point minPt = pointsCloneForSlopeOrder[i - count];
                    Point maxPt = pointsCloneForSlopeOrder[i - 1];
                    if (Double.compare(slope, previousSlope) == 0) {
                        maxPt = pointsCloneForSlopeOrder[i];
                    }
                    if (pointP.compareTo(minPt) < 0) {
                        minPt = pointP;
                    }
                    if (pointP.compareTo(maxPt) > 0) {
                        maxPt = pointP;
                    }

                    boolean insert = true;
                    for (int j = 0; j < p; j++) {
                        double pastSlope = pointP.slopeTo(pointsCloneForSlopeOrder[j]);
                        if (Double.compare(pastSlope, previousSlope) >= 0) {
                            if (Double.compare(pastSlope, previousSlope) == 0) {
                                insert = false;
                            }
                            break;
                        }
                    }
                    if (insert) {
                        LineSegment lineSeg = new LineSegment(minPt, maxPt);
                        collinearSegments.add(lineSeg);
                    }
                }
                if (Double.compare(slope, previousSlope) != 0) {
                    count = 0;
                    previousSlope = slope;
                }
            }
        }
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
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}