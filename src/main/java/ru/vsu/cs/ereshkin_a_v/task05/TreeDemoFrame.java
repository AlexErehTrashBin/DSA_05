package ru.vsu.cs.ereshkin_a_v.task05;

import ru.vsu.cs.ereshkin_a_v.task05.jtree.FileTreeCellRenderer;
import ru.vsu.cs.ereshkin_a_v.task05.jtree.FileTreeModel;
import ru.vsu.cs.ereshkin_a_v.task05.jtree.JTreeUtils;
import ru.vsu.cs.ereshkin_a_v.task05.render.FileTreePainter;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class TreeDemoFrame extends JFrame {
	private JPanel panelMain;
	private JTextArea textAreaSystemOut;
	private JTextField textFieldBracketNotationTree;
	private JButton buttonMakeTree;
	private JSplitPane splitPaneMain;
	private JPanel panelPaintArea;
	private JButton buttonSaveImage;
	private JButton buttonToBracketNotation;
	private JCheckBox checkBoxTransparent;
	private JTree jTree;

	private final JPanel paintPanel;
	private final JFileChooser fileChooserSave;

	FileTree tree = new FileTree("./rootDirectory");


	public TreeDemoFrame() {
		this.setTitle("Двоичные деревья");
		this.setContentPane(panelMain);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();

		//createMenu();

		splitPaneMain.setDividerLocation(0.5);
		splitPaneMain.setResizeWeight(1.0);
		splitPaneMain.setBorder(null);

		paintPanel = new JPanel() {

			@Override
			public void paintComponent(Graphics gr) {
				super.paintComponent(gr);
				Dimension paintSize = FileTreePainter.paint(tree, gr);
				if (!paintSize.equals(this.getPreferredSize())) {
					SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
				}
			}
		};
		JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
		panelPaintArea.add(paintJScrollPane);

		fileChooserSave = new JFileChooser();
		fileChooserSave.setCurrentDirectory(new File("./images"));
		FileFilter filter = new FileNameExtensionFilter("SVG images", "svg");
		fileChooserSave.addChoosableFileFilter(filter);
		fileChooserSave.setAcceptAllFileFilterUsed(false);
		fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooserSave.setApproveButtonText("Save");

		buttonToBracketNotation.addActionListener(actionEvent -> {
			if (tree == null) {
				return;
			}
			try {
				textFieldBracketNotationTree.setText(tree.toBracketNotation());
			} catch (Exception e) {
				SwingUtils.showErrorMessageBox(e);
			}
		});

		buttonSaveImage.addActionListener(actionEvent -> {
			if (tree == null) {
				return;
			}
			try {
				if (fileChooserSave.showSaveDialog(TreeDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
					String filename = fileChooserSave.getSelectedFile().getPath();
					if (!filename.toLowerCase().endsWith(".svg")) {
						filename += ".svg";
					}
					FileTreePainter.saveIntoFile(tree, filename, checkBoxTransparent.isSelected());
				}
			} catch (Exception e) {
				SwingUtils.showErrorMessageBox(e);
			}
		});
		FileTreeModel treeModel = new FileTreeModel(tree);
		FileTreeCellRenderer cellRenderer = new FileTreeCellRenderer();
		jTree.setCellRenderer(cellRenderer);
		jTree.setModel(treeModel);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.getRoot());
		JTreeUtils.fillJTreeRoot(root, tree);
		jTree = new JTree(root);
		repaintTree();
	}

	/**
	 * Перерисовка дерева
	 */
	public void repaintTree() {
		panelPaintArea.repaint();
		paintPanel.repaint();
		panelPaintArea.revalidate();
	}

	/**
	 * Выполнение действия с выводом стандартного вывода в окне (textAreaSystemOut)
	 *
	 * @param action Выполняемое действие
	 */
	private void showSystemOut(Runnable action) {
		PrintStream oldOut = System.out;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos, true, StandardCharsets.UTF_8));

		action.run();

		textAreaSystemOut.setText(baos.toString(StandardCharsets.UTF_8));
		System.setOut(oldOut);
	}

}
