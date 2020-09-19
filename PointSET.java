import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.ResizingArrayBag;

public class PointSET
{
	private final SET<Point2D> set;

	public ResizingArrayBag<Point2D> points = new ResizingArrayBag<Point2D>();

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
		points.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("contains: null argument");
		return set.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D p : set)
			p.draw();
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new IllegalArgumentException("range: null argument");
		SET<Point2D> in = new SET<Point2D>();
		for (Point2D p : set) {
			if (rect.contains(p))
				in.add(p);
		}
		return in;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		Point2D champ = null;
		for (Point2D q : set) {
			if (champ == null || q.distanceTo(p) < champ.distanceTo(p))
				champ = q;
		}
		return champ;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
		StdDraw.setPenColor(StdDraw.BOOK_RED);
		StdDraw.setPenRadius(0.01);

		In in = new In(args[0]);
        RectHV rect = null;
		Point2D tmp = null;

        PointSET kdtree = new PointSET();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			if (tmp == null) {
				 tmp = new Point2D(x, y);
				 continue;
			} else if (rect == null) {
				rect = new RectHV(Math.min(x, tmp.x()), Math.min(y, tmp.y()),
								  Math.max(x, tmp.x()), Math.max(y, tmp.y()));
				continue;
			}
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
			StdOut.println(p);
		}
		kdtree.draw();
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		StdDraw.setPenRadius(0.001);
		rect.draw();
		StdDraw.show();

		Point2D p = null;
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
				p = new Point2D(x, y);
				StdOut.printf("%8.6f %8.6f\n", x, y);
				StdDraw.clear();
				StdDraw.setPenColor(StdDraw.BOOK_RED);
				StdDraw.setPenRadius(0.01);
				kdtree.draw();
				StdDraw.setPenColor(StdDraw.BOOK_BLUE);
				StdDraw.setPenRadius(0.001);
				rect.draw();
				StdDraw.setPenColor(StdDraw.GREEN);
				StdDraw.setPenRadius(0.013);
				p.draw();
				StdDraw.setPenColor(StdDraw.MAGENTA);
				for (Point2D q : kdtree.range(rect))
					q.draw();
				Point2D near = kdtree.nearest(p);
				if (near != null) {
					StdDraw.setPenColor(StdDraw.YELLOW);
					near.draw();
				}
				StdDraw.show();
            } else if (p != null)
				kdtree.insert(p);
            StdDraw.pause(20);
        }

	}

}
