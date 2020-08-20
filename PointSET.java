import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.RectHV;

public class PointSET
{
	SET<Point2D> set;

	// construct an empty set of points
	public PointSET() { set = new SET<Point2D>(); }

	// is the set empty?
	public boolean isEmpty() { return set.isEmpty(); }

	// number of points in the set
	public int size() { return set.size(); }

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("insert: null argument");
		set.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("contains: null argument");
		return set.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D p : set) {
			p.draw();
			StdOut.println(" >> " + p);
		}
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new IllegalArgumentException("range: null argument");
		return set;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("nearest: null argument");
		return p;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.01);
        PointSET kdtree = new PointSET();
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
				StdOut.printf("%8.6f %8.6f\n", x, y);
				kdtree.insert(p);
				StdDraw.clear();
				kdtree.draw();
				StdDraw.show();
            }
            StdDraw.pause(20);
        }

	}

}
