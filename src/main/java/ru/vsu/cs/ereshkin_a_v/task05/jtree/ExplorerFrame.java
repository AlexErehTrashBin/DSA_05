package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import ru.vsu.cs.ereshkin_a_v.task05.FileTree;
import ru.vsu.cs.ereshkin_a_v.task05.FileTree.FileTreeNode;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class ExplorerFrame extends JFrame {
	private JTree jTree;
	private JPanel panelMain;
	private JScrollPane jTreeScrollPane;
	private JList<File> jList;
	private JScrollPane jListScrollPane;
	private JTextField textField1;
	private JButton searchButton;
	FileTree tree = new FileTree(".");

	public ExplorerFrame() {
		this.setTitle("JTree штука");
		this.setContentPane(panelMain);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		FileTreeModel treeModel = new FileTreeModel(tree);
		FileTreeCellRenderer treeRenderer = new FileTreeCellRenderer();
		jTree.setCellRenderer(treeRenderer);
		jTree.setModel(treeModel);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.getRoot());
		JTreeUtils.fillJTreeRoot(root, tree);
		jTree = new JTree(root);

		FileListModel listModel = new FileListModel(tree.getRoot());
		FileListCellRenderer listRenderer = new FileListCellRenderer();
		jList.setModel(listModel);
		jList.setCellRenderer(listRenderer);
		jList.addListSelectionListener(e -> {
			int selectedIndex = e.getFirstIndex();
			File elementAtIndex = listModel.getElementAt(selectedIndex);
			boolean isFolder = elementAtIndex.isDirectory();
			if (!isFolder) return;
			Object object = treeModel.getChild(treeModel.getRoot(), selectedIndex);
			if (!(object instanceof FileTreeNode)) return;
			DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode((tree));
			JTreeUtils.fillJTreeRoot(newRoot, tree);
			treeModel.setRootFileTree(((FileTreeNode) object).getValue());
			treeModel.reload();
			//jTree.setModel(treeModel);
			jTree.repaint();
		});
		searchButton.addActionListener(e -> {
			String name = textField1.getText();
			List<File> searchList = tree.searchByNameAndExtension(name);
			EventQueue.invokeLater(() -> {
				try {
					//JFrame frameMain = new TreeDemoFrame();
					new SearchFrame(searchList).setVisible(true);
					//explorerFrame.setExtendedState(MAXIMIZED_BOTH);
				} catch (Exception ex) {
					SwingUtils.showErrorMessageBox(ex);
				}
			});
		});
	}
}
