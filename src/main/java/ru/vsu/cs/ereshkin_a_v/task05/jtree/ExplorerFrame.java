package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import ru.vsu.cs.ereshkin_a_v.task05.FileTree;
import ru.vsu.cs.ereshkin_a_v.task05.FileTree.FileTreeNode;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.File;

public class ExplorerFrame extends JFrame {
	private JTree jTree;
	private JPanel panelMain;
	private JScrollPane jTreeScrollPane;
	private JList<File> jList;
	private JScrollPane jListScrollPane;
	FileTree tree = new FileTree(".");

	public ExplorerFrame() {
		this.setTitle("JTree штука");
		this.setContentPane(panelMain);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	}
}
