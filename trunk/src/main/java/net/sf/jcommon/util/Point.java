package net.sf.jcommon.util;

public class Point {

	public int x;
	public int y;
	
	public Point() {}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(double x, double y) {
		this.x = (int)x;
		this.y = (int)y;
	}
	
	@Override
	public int hashCode() {
		return 31 * x + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
}
