/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {

        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
		In in2 = new In(args[1]);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree(args[0]);
		SET<Point2D> queries = new SET<>();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
			if (!in2.isEmpty())
				queries.add(new Point2D(in2.readDouble(), in2.readDouble()));
            kdtree.insert(p);
            brute.insert(p);
        }


        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
		double rad = StdDraw.getPenRadius();
		int i = 0;
		SET<Point2D> nears = new SET<Point2D>();
        for (Point2D query : queries) {
            // the location (x, y) of the mouse

            // draw all of the points
            StdDraw.clear();
			// kdtree.draw();
            // StdDraw.setPenRadius(0.005);
			// StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.filledSquare(0.5 ,0.5, 0.5);
            StdDraw.setPenColor(StdDraw.CYAN);
            brute.draw();

            StdDraw.setPenColor(StdDraw.ORANGE);
            StdDraw.setPenRadius(0.007);
			for (Point2D p : queries)
				p.draw();
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.setPenRadius(0.007);
			for (Point2D p : nears)
				p.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            // draw in blue the nearest neighbor (using kd-tree algorithm)
			Point2D p = kdtree.nearest(query);
            StdDraw.setPenColor(StdDraw.ORANGE);
			double size = StdDraw.getPenRadius();
            StdDraw.setPenRadius(0.015);
			// p.draw();
			nears.add(p);
            StdDraw.setPenColor(StdDraw.ORANGE);
            StdDraw.setPenRadius(0.015);
            // brute.nearest(query).draw();
            StdDraw.setPenRadius(size);
			StdDraw.show();
			StdDraw.save(args[0].split("\\.(?=[^\\.]+$)")[0] + "-near-" + i + ".png");
			i++;
            // StdDraw.pause(30);
        }
            StdDraw.clear();
			// kdtree.draw();
            // StdDraw.setPenRadius(0.005);
			// StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.filledSquare(0.5 ,0.5, 0.5);
            StdDraw.setPenColor(StdDraw.CYAN);
            brute.draw();

            StdDraw.setPenColor(StdDraw.ORANGE);
            StdDraw.setPenRadius(0.007);
			for (Point2D p : queries)
				p.draw();
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.setPenRadius(0.007);
			for (Point2D p : nears)
				p.draw();
		StdDraw.show();
		StdDraw.save(args[0].split("\\.(?=[^\\.]+$)")[0] + "-near-" + i + ".png");
    }
}
