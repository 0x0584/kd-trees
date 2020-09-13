import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree
{
	private final class KdNode
	{
		private Point2D point;
		private KdNode left, right;
		private boolean is_vert;

		public KdNode(KdNode left, Point2D point, KdNode right, boolean is_vert) {
			this.left = left;
			this.point = point;
			this.right = right;
			this.is_vert = is_vert;
		}

		public boolean equal_to(Point2D p) {
			return point.x() == p.x() && point.y() == p.y();
		}

		public boolean on_left(Point2D p) { return on_left(p, is_vert); }

		public boolean on_left(Point2D p, boolean is_vertical) {
			return (is_vertical && point.x() > p.x())
				|| (!is_vertical && point.y() > p.y());
		}

	}

	KdNode root;
	int size_;

	// construct an empty set of points
	public KdTree() { root = null; size_ = 0; }

	// is the set empty?
	public boolean isEmpty() { return size_ == 0; }

	// number of points in the set
	public int size() { return size_; }

	private void draw(KdNode root) {
		if (root != null) {
			StdOut.println("drawing >> " + root.point);
			if (root.is_vert) {
				StdDraw.setPenColor(StdDraw.RED);
				root.point.drawTo(new Point2D(root.point.x(), 1));
				root.point.drawTo(new Point2D(root.point.x(), 0));
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				root.point.drawTo(new Point2D(1, root.point.y()));
				root.point.drawTo(new Point2D(0, root.point.y()));
			}
			StdDraw.show();
			StdDraw.pause(1000);
			draw(root.left);
			draw(root.right);
		}
	}

	// draw all points to standard draw
	public void draw() {
		StdDraw.enableDoubleBuffering();
		StdDraw.setPenRadius(0.001);
		draw(root);
	}

	private KdNode make_tree(KdNode root, Point2D p, boolean is_vertical) {
		if (root == null)
			return new KdNode(null, p, null, is_vertical);
		else if (root.equal_to(p))
			return root;
		else if (root.on_left(p, is_vertical))
			root.left = make_tree(root.left, p, !is_vertical);
		else
			root.right = make_tree(root.right, p, !is_vertical);
		return root;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("insert: null argument");
		root = make_tree(root, p, true);
		size_ += 1;
	}

	private boolean contains(KdNode root, Point2D p) {
		if (root == null) return false;
		else if (root.equal_to(p)) return true;
		else if (root.on_left(p)) return contains(root.left, p);
		else return contains(root.right, p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("contains: null argument");
		return contains(root, p);
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		return new SET<Point2D>();
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		return p;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
        // StdDraw.enableDoubleBuffering();
		// StdDraw.setPenColor(StdDraw.BOOK_RED);
		// StdDraw.setPenRadius(0.01);

		In in = new In(args[0]);
        // RectHV rect = null;
		// Point2D tmp = null;

        KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			// if (tmp == null) {
			// 	tmp = new Point2D(x, y);
			// 	continue;
			// } else if (rect == null) {
			// 	rect = new RectHV(Math.min(x, tmp.x()), Math.min(y, tmp.y()),
			// 					  Math.max(x, tmp.x()), Math.max(y, tmp.y()));
			// 	continue;
			// }
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
			StdOut.println(p);
		}

		kdtree.draw();
		StdOut.println("#####");

		// StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		// StdDraw.setPenRadius(0.001);
		// rect.draw();
		// StdDraw.show();

		// Point2D p = null;
        // while (true) {
        //     if (StdDraw.isMousePressed()) {
        //         double x = StdDraw.mouseX();
        //         double y = StdDraw.mouseY();
		// 		p = new Point2D(x, y);
		// 		StdOut.printf("%8.6f %8.6f\n", x, y);
		// 		StdDraw.clear();
		// 		StdDraw.setPenColor(StdDraw.BOOK_RED);
		// 		StdDraw.setPenRadius(0.01);
		// 		kdtree.draw();
		// 		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		// 		StdDraw.setPenRadius(0.001);
		// 		rect.draw();
		// 		StdDraw.setPenColor(StdDraw.GREEN);
		// 		StdDraw.setPenRadius(0.013);
		// 		p.draw();
		// 		StdDraw.setPenColor(StdDraw.MAGENTA);
		// 		for (Point2D q : kdtree.range(rect))
		// 			q.draw();
		// 		Point2D near = kdtree.nearest(p);
		// 		if (near != null) {
		// 			StdDraw.setPenColor(StdDraw.YELLOW);
		// 			near.draw();
		// 		}
		// 		StdDraw.show();
        //     } else if (p != null)
		// 		kdtree.insert(p);
        //     StdDraw.pause(20);
        // }

	}

}
