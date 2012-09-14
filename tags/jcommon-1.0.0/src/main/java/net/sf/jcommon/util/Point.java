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
	public boolean equals(Object o) {
		return o instanceof Point && x == ((Point)o).x && y == ((Point)o).y;
	}
}
