package ru.vsu.cs.ereshkin_a_v.task05;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import ru.vsu.cs.ereshkin_a_v.task05.FileTree.FileTreeNode;
import ru.vsu.cs.util.DrawUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class FileTreePainter {
	public static final int TREE_NODE_ONE_CHAR_WIDTH = 10;
	public static final int TREE_NODE_HEIGHT = 30;
	public static final int HORIZONTAL_INDENT = 20;
	public static final int VERTICAL_INDENT = 50;

	public static final Font FONT = new Font("Microsoft Sans Serif", Font.PLAIN, 20);

	private static NodeDrawResult paint(FileTreeNode node, Graphics2D g2d, int topLeftX, int topLeftY) {
		/// Если ноды нет, то и рисовать нечего
		if (node == null) {
			return null;
		}
		/// Список с результатами отрисовки.
		List<NodeDrawResult> nodesResultList = new ArrayList<>();
		// Смещение координаты X для отрисовки следующих нод.
		int nextNodeXOffset = 0;
		/// Перебор и рекурсивный вызов отрисовки
		for (int i = 0; i < node.numberOfChildrenNodes(); i++) {
			// i-ый дочерний нод
			FileTreeNode ithChildNode = node.getChildNode(i);

			// Ширина i-й ноды (длина строки * ширина 1 символа).
			int localTreeNodeWidth = ithChildNode.getValue().toString().length() * TREE_NODE_ONE_CHAR_WIDTH;

			// Следующий X начала ноды
			int nextPaintX = topLeftX + nextNodeXOffset;
			// Следующий Y начала ноды
			int nextPaintY = topLeftY + (TREE_NODE_HEIGHT + VERTICAL_INDENT);
			NodeDrawResult ithResult = paint(ithChildNode, g2d, nextPaintX, nextPaintY);
			nodesResultList.add(ithResult);
			nextNodeXOffset += (nextPaintX + localTreeNodeWidth + HORIZONTAL_INDENT);
		}
		/// Создание NodeDrawResult текущего элемента.
		// Высчитываем ширину прямоугольника (узла), чтобы вся строка умещалась.
		int treeNodeWidthCalculated = node.getValue().getName().length() * TREE_NODE_ONE_CHAR_WIDTH;
		//
		int overallWidthCalculated = treeNodeWidthCalculated;
		if (!nodesResultList.isEmpty()){
			overallWidthCalculated = topLeftX + nodesResultList.get(nodesResultList.size() - 1).width + HORIZONTAL_INDENT;
		}
		Point currentTopLeft = new Point(topLeftX, topLeftY);
		Point currentBottomRight = new Point(topLeftX + treeNodeWidthCalculated, topLeftY + TREE_NODE_HEIGHT);
		NodeDrawResult currentResult = new NodeDrawResult(currentTopLeft, currentBottomRight, overallWidthCalculated);

		/// Отрисовка
		// Рисование фона
		Color color = Color.WHITE;
		g2d.setColor(color);
		g2d.fillRect(topLeftX, topLeftY, treeNodeWidthCalculated, TREE_NODE_HEIGHT);
		// Рисование контуров
		g2d.setColor(Color.BLACK);
		g2d.drawRect(topLeftX, topLeftY, treeNodeWidthCalculated, TREE_NODE_HEIGHT);
		// Рисование рёбер
		g2d.setColor(Color.BLACK);
		Point bottomHinge = currentResult.getBottomHinge();
		for (NodeDrawResult nodeDrawResult : nodesResultList) {
			Point topHinge = nodeDrawResult.getTopHinge();
			g2d.drawLine(bottomHinge.x, bottomHinge.y, topHinge.x, topHinge.y);
		}

		// Рисование текста внутри ноды
		g2d.setColor(DrawUtils.getContrastColor(color));
		DrawUtils.drawStringInCenter(g2d, FONT, node.getValue().getName(), topLeftX, topLeftY, treeNodeWidthCalculated, TREE_NODE_HEIGHT);



		return currentResult;
	}

	/**
	 * Рисование дерева
	 *
	 * @param tree Дерево
	 * @param gr   Graphics
	 * @return Размеры картинки
	 */
	public static Dimension paint(FileTree tree, Graphics gr) {
		Graphics2D g2d = (Graphics2D) gr;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		NodeDrawResult rootResult = paint(tree.getRoot(), g2d, HORIZONTAL_INDENT, HORIZONTAL_INDENT);
		return new Dimension((rootResult == null) ? 0 : rootResult.childrenOverallWidth, /*(rootResult == null) ?*/ 0 /*: rootResult.maxY + HORIZONTAL_INDENT*/);
	}

	public static void saveIntoFile(FileTree tree, String filename, boolean backgroundTransparent) throws IOException {
		// первый раз рисуем, только чтобы размеры изображения определить
		SVGGraphics2D g2 = new SVGGraphics2D(1, 1);
		Dimension size = FileTreePainter.paint(tree, g2);
		// второй раз рисуем непосредственно для сохранения
		g2 = new SVGGraphics2D(size.width, size.height);
		if (!backgroundTransparent) {
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, size.width, size.height);
		}
		FileTreePainter.paint(tree, g2);

		SVGUtils.writeToSVG(new File(filename), g2.getSVGElement());
	}

	public static void saveIntoFile(FileTree tree, String filename)
			throws IOException {
		saveIntoFile(tree, filename, false);
	}

	private static class NodeDrawResult {
		private final int width;
		private final int height;
		private final int childrenOverallWidth;
		private final Point topLeft;
		private final Point bottomRight;

		public NodeDrawResult(Point topLeft, Point bottomRight, int childrenOverallWidth) {
			this.topLeft = topLeft;
			this.bottomRight = bottomRight;
			this.childrenOverallWidth = childrenOverallWidth;
			width = bottomRight.x - topLeft.x;
			height = bottomRight.x - topLeft.y;
		}

		public Point getCenterPoint(){
			int x = (topLeft.x + bottomRight.x)/2;
			int y = (topLeft.y + bottomRight.y)/2;
			return new Point(x, y);
		}

		public Point getTopHinge(){
			int x = (topLeft.x + bottomRight.x)/2;
			int y = topLeft.y;
			return new Point(x, y);
		}

		public Point getBottomHinge(){
			int x = (topLeft.x + bottomRight.x)/2;
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
	}
}