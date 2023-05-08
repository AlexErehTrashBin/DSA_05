package ru.vsu.cs.ereshkin_a_v.task05.render;

import java.awt.*;

class NodeDrawResult {
	protected final int width;
	protected final int height;
	protected final int childrenOverallWidth;
	protected final int childrenOverallHeight;
	protected final Point topLeft;
	protected final Point bottomRight;
	protected Color color;

	public NodeDrawResult(Point topLeft, Point bottomRight, int childrenOverallWidth, int childrenOverallHeight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		this.childrenOverallWidth = childrenOverallWidth;
		this.childrenOverallHeight = childrenOverallHeight;
		width = bottomRight.x - topLeft.x;
		height = bottomRight.x - topLeft.y;
	}
	public void setColor(Color color){
		this.color = color;
	}

	public Point getCenterPoint() {
		int x = (topLeft.x + bottomRight.x) / 2;
		int y = (topLeft.y + bottomRight.y) / 2;
		return new Point(x, y);
	}

	public Point getTopHinge() {
		int x = (topLeft.x + bottomRight.x) / 2;
		int y = topLeft.y;
		return new Point(x, y);
	}

	public Point getBottomHinge() {
		int x = (topLeft.x + bottomRight.x) / 2;
		int y = bottomRight.y;
		return new Point(x, y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Point getTopLeft() {
		return new Point(topLeft);
	}

	public Point getBottomRight() {
		return new Point(bottomRight);
	}

	public int getChildrenOverallWidth() {
		return childrenOverallWidth;
	}

	public int getChildrenOverallHeight() {
		return childrenOverallHeight;
	}

	public Color getColor() {
		return color;
	}
}
