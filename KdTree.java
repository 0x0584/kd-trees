import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import edu.princeton.cs.algs4.Queue;

public class KdTree
{
	private final class KdNode
	{
		private final Point2D point;
		private KdNode left, right;
		private final boolean is_vert;

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

		public void drawUp() {
			StdDraw.setPenColor(StdDraw.RED);
			point.drawTo(new Point2D(point.x(), 1));
		}

		public void drawDown() {
			StdDraw.setPenColor(StdDraw.RED);
			point.drawTo(new Point2D(point.x(), 0));
		}

		public void drawLeft() {
			StdDraw.setPenColor(StdDraw.BLUE);
			point.drawTo(new Point2D(1, point.y()));
		}

		public void drawRight() {
			StdDraw.setPenColor(StdDraw.BLUE);
			point.drawTo(new Point2D(0, point.y()));
		}
	}

	private KdNode root;
	private int size_;

	// construct an empty set of points
	public KdTree() { root = null; size_ = 0; }

	// is the set empty?
	public boolean isEmpty() { return size_ == 0; }

	// number of points in the set
	public int size() { return size_; }

	private void draw(KdNode root, RectHV rect) {
		if (root != null) {
			Point2D point = root.point, min, max;
			RectHV left, right;

			if (root.is_vert) {
				StdDraw.setPenColor(StdDraw.RED);
				min = new Point2D(point.x(), rect.ymin());
				max = new Point2D(point.x(), rect.ymax());
				left = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
				right = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				min = new Point2D(rect.xmin(), point.y());
				max = new Point2D(rect.xmax(), point.y());
				left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
				right = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
			}
			StdDraw.setPenRadius(0.001);
			min.drawTo(max);
			StdDraw.setPenRadius(0.01);
			StdDraw.setPenColor(StdDraw.BLACK);
			point.draw();
			StdDraw.show();
			draw(root.left, left);
			draw(root.right, right);
		}
	}

	// draw all points to standard draw
	public void draw() {
		StdDraw.enableDoubleBuffering();
		draw(root, new RectHV(0, 0, 1, 1));
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

	private void range(KdNode root, RectHV query, RectHV rect, Queue<Point2D> queue) {
		if (root == null) return;

		if (query.intersects(rect)) {
			Point2D point = root.point;
			RectHV left, right;

			if (query.contains(point))
				queue.enqueue(point);
			if (root.is_vert) {
				left = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
				right = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
			} else {
				left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
				right = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
			}
			range(root.left, query, left, queue);
			range(root.right, query, right, queue);
		}
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		Queue<Point2D> queue = new Queue<Point2D>();
		range(root, rect, new RectHV(0, 0, 1, 1), queue);
		return queue;
	}

	private Point2D _nearest(KdNode root, Point2D p, RectHV rect, Point2D champ) {
		if (root == null) return champ;

		Point2D nearest = champ;
		double p2near = 0.0, p2rect = 0.0;

		if (nearest != null) {
			p2near = p.distanceSquaredTo(nearest);
			p2rect = rect.distanceSquaredTo(p);
		}

		if (nearest == null || p2near > p2rect) {
			Point2D point = root.point;
			RectHV left, right;

			if (nearest == null || p2near > point.distanceSquaredTo(p))
				nearest = point;
			if (root.is_vert) {
				left = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
				right = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
				if (p.x() < point.x()) {
					nearest = _nearest(root.left, p, left, nearest);
					nearest = _nearest(root.right, p, right, nearest);
				} else  {
					nearest = _nearest(root.right, p, right, nearest);
					nearest = _nearest(root.left, p, left, nearest);
				}
			} else {
				left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
				right = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
				if (p.y() < point.y()) {
					nearest = _nearest(root.left, p, left, nearest);
					nearest = _nearest(root.right, p, right, nearest);
				} else  {
					nearest = _nearest(root.right, p, right, nearest);
					nearest = _nearest(root.left, p, left, nearest);
				}
			}
		}
		return nearest;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		return _nearest(root, p, new RectHV(0, 0, 1, 1), null);
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		In in = new In(args[0]);
        KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
		}
		kdtree.draw();
	}

}
