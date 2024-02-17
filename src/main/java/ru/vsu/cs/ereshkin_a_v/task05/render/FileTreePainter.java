package ru.vsu.cs.ereshkin_a_v.task05.render;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import ru.vsu.cs.ereshkin_a_v.task05.FileTree;
import ru.vsu.cs.ereshkin_a_v.task05.FileTree.FileTreeNode;
import ru.vsu.cs.util.DrawUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class FileTreePainter {
	public static final int TREE_NODE_ONE_CHAR_WIDTH = 16;
	public static final int TREE_NODE_HEIGHT = 30;
	public static final int HORIZONTAL_INDENT = 10;
	public static final int VERTICAL_INDENT = 75;
	public static final Color EDGE_COLOR = Color.BLACK;
	public static final Color OUTLINE_COLOR = Color.BLACK;
	public static final Color DIRECTORY_BG_COLOR = Color.ORANGE;
	public static final Color FILE_BG_COLOR = Color.WHITE;

	public static final Font FONT = new Font("Microsoft Sans Serif", Font.PLAIN, 20);

	private static NodeDrawResult paint(FileTreeNode node, Graphics2D g2d, int topLeftX, int topLeftY) {
		/// Если ноды нет, то и рисовать нечего
		if (node == null) {
			return null;
		}
		/// Список с результатами отрисовки дочерних элементов.
		List<NodeDrawResult> nodesResultList = new ArrayList<>();
		// Смещение координаты X для отрисовки следующих нод.
		int nextNodeXOffset = 0;
		// Максимальная
		int maxHeight = 0;
		/// Перебор и рекурсивный вызов отрисовки
		for (int i = 0; i < node.numberOfChildren(); i++) {
			// i-ый дочерний нод
			FileTreeNode ithChildNode = node.getChild(i);

			// Следующий X начала ноды
			int nextPaintX = topLeftX + nextNodeXOffset;
			// Следующий Y начала ноды
			int nextPaintY = topLeftY + (TREE_NODE_HEIGHT + VERTICAL_INDENT);
			NodeDrawResult ithResult = paint(ithChildNode, g2d, nextPaintX, nextPaintY);
			if (ithResult.childrenOverallHeight > maxHeight) {
				maxHeight = ithResult.childrenOverallHeight;
			}
			nodesResultList.add(ithResult);
			nextNodeXOffset += (Math.max(ithResult.getChildrenOverallWidth(), ithResult.getWidth()) + HORIZONTAL_INDENT);
		}
		/// Создание NodeDrawResult текущего элемента.
		// Высчитываем ширину прямоугольника (узла), чтобы вся строка умещалась.
		int treeNodeWidthCalculated = node.getValue().getName().length() * TREE_NODE_ONE_CHAR_WIDTH;
		//
		int overallWidthCalculated = treeNodeWidthCalculated + nextNodeXOffset + HORIZONTAL_INDENT;
		int overallHeightCalculated = TREE_NODE_HEIGHT + VERTICAL_INDENT + maxHeight;
		if (!nodesResultList.isEmpty()){
			overallWidthCalculated = nextNodeXOffset /*+ lastNode.getWidth()*/ /*+ HORIZONTAL_INDENT*/;
		}
		int currentTopLeftX = topLeftX + overallWidthCalculated/2 - HORIZONTAL_INDENT/2 - treeNodeWidthCalculated/2;
		int currentBottomRightX = topLeftX + treeNodeWidthCalculated + overallWidthCalculated/2 -HORIZONTAL_INDENT/2 - treeNodeWidthCalculated/2;



		Point currentTopLeft = new Point(currentTopLeftX, topLeftY);
		Point currentBottomRight = new Point(currentBottomRightX, topLeftY + TREE_NODE_HEIGHT);
		NodeDrawResult currentResult = new NodeDrawResult(currentTopLeft, currentBottomRight, overallWidthCalculated, overallHeightCalculated);

		Color bgColor = DIRECTORY_BG_COLOR;
		if (!node.isDirectory()) bgColor = FILE_BG_COLOR;
		/// Отрисовка
		// Рисование фона
		drawFilledRectNode(g2d, currentTopLeft, treeNodeWidthCalculated, bgColor);
		// Рисование контуров
		drawOutlineForRectNode(g2d, currentTopLeft, treeNodeWidthCalculated);
		// Рисование рёбер
		for (NodeDrawResult nodeDrawResult : nodesResultList) {
			drawEdge(g2d, nodeDrawResult.getTopHinge(), currentResult.getBottomHinge());
		}
		// Рисование текста внутри ноды
		drawTextInNode(g2d, currentTopLeft, node.getValue().getName(), treeNodeWidthCalculated, bgColor);
		return currentResult;
	}

	private static void drawEdge(Graphics2D g2d, Point topHinge, Point bottomHinge){
		Color prevColor = g2d.getColor();
		g2d.setColor(FileTreePainter.EDGE_COLOR);
		g2d.drawLine(bottomHinge.getX(), bottomHinge.getY(), topHinge.getX(), topHinge.getY());
		g2d.setColor(prevColor);
	}
	private static void drawOutlineForRectNode(Graphics2D g2d, Point topLeft, int width){
		Color prevColor = g2d.getColor();
		g2d.setColor(FileTreePainter.OUTLINE_COLOR);
		g2d.drawRect(topLeft.getX(), topLeft.getY(), width, FileTreePainter.TREE_NODE_HEIGHT);
		g2d.setColor(prevColor);
	}
	private static void drawFilledRectNode(Graphics2D g2d, Point topLeft, int width, Color fillColor){
		Color prevColor = g2d.getColor();
		g2d.setColor(fillColor);
		g2d.fillRect(topLeft.getX(), topLeft.getY(), width, FileTreePainter.TREE_NODE_HEIGHT);
		g2d.setColor(prevColor);
	}
	private static void drawTextInNode(Graphics2D g2d, Point topLeft, String text, int width, Color fillColor){
		Color prevColor = g2d.getColor();
		g2d.setColor(DrawUtils.getContrastColor(fillColor));
		DrawUtils.drawStringInCenter(g2d, FONT, text,topLeft.getX(), topLeft.getY(), width, TREE_NODE_HEIGHT);
		//g2d.fillRect(topLeft.getX(), topLeft.getY(), width, FileTreePainter.TREE_NODE_HEIGHT);
		g2d.setColor(prevColor);
	}
	/**
	 * Рисование дерева
	 *
	 * @param tree Дерево файлов
	 * @param gr   Graphics
	 * @return Размеры картинки
	 */
	public static Dimension paint(FileTree tree, Graphics gr) {
		Graphics2D g2d = (Graphics2D) gr;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		NodeDrawResult rootResult = paint(tree.getRoot(), g2d, HORIZONTAL_INDENT, HORIZONTAL_INDENT);
		if (rootResult == null) {
			return new Dimension(0, 0);
		}
		return new Dimension(rootResult.getChildrenOverallWidth(), rootResult.getChildrenOverallHeight() + VERTICAL_INDENT);
	}

	public static void saveIntoFile(FileTree tree, String filename, boolean backgroundTransparent)
			throws IOException {
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
	public static void saveIntoFile(FileTree tree, String filename) throws IOException {
		saveIntoFile(tree, filename, false);
	}

}
