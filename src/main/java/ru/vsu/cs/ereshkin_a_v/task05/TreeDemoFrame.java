package ru.vsu.cs.ereshkin_a_v.task05;

import ru.vsu.cs.ereshkin_a_v.task05.jtree.ExplorerFrame;
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
import java.io.File;

public class TreeDemoFrame extends JFrame {
	private JPanel panelMain;
	private JSplitPane splitPaneMain;
	private JPanel panelPaintArea;
	private JButton buttonSaveImage;
	private JCheckBox checkBoxTransparent;
	private JTree jTree;
	private JButton openExplorerButton;
	private JButton setPathButton;

	private final JPanel paintPanel;
	private final JFileChooser fileChooserSave;
	private final JFileChooser dirPathChooser;
	FileTree tree;


	public TreeDemoFrame() {
		this.setTitle("Недвоичное дерево");
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
				if (tree == null) return;
				Dimension paintSize = FileTreePainter.paint(tree, gr);
				if (!paintSize.equals(this.getPreferredSize())) {
					SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
				}
			}
		};
		JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
		paintJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		paintJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelPaintArea.add(paintJScrollPane);

		fileChooserSave = new JFileChooser();
		fileChooserSave.setCurrentDirectory(new File("./images"));
		FileFilter filter = new FileNameExtensionFilter("SVG images", "svg");
		fileChooserSave.addChoosableFileFilter(filter);
		fileChooserSave.setAcceptAllFileFilterUsed(false);
		fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooserSave.setApproveButtonText("Save");

		dirPathChooser = new JFileChooser();
		dirPathChooser.setCurrentDirectory(new File("."));
		dirPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirPathChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		dirPathChooser.setApproveButtonText("Choose");

		setPathButton.addActionListener(event -> {
			try {
				if (dirPathChooser.showOpenDialog(TreeDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
					String dirName = dirPathChooser.getSelectedFile().getPath();
					initTree(dirName);
					//initJTree();
				}
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

		openExplorerButton.addActionListener(actionEvent -> EventQueue.invokeLater(() -> {
			try {
				JFrame explorerFrame = new ExplorerFrame(tree);
				explorerFrame.setVisible(true);
			} catch (Exception ex) {
				SwingUtils.showErrorMessageBox(ex);
			}
		}));

		initTree(".");
	}

	private void initTree(String dirPath){
		tree = new FileTree(dirPath);
		FileTreeModel treeModel = new FileTreeModel(tree);
		FileTreeCellRenderer cellRenderer = new FileTreeCellRenderer();
		jTree.setCellRenderer(cellRenderer);
		jTree.setModel(treeModel);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.getRoot());
		JTreeUtils.fillJTreeRoot(root, tree);
		jTree.repaint();
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

}
