package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import ru.vsu.cs.ereshkin_a_v.task05.FileTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeFrame extends JFrame {
	private JTree jTree;
	private JPanel panelMain;
	private JScrollPane jTreeScrollPane;
	FileTree tree = new FileTree("./rootDirectory");

	public JTreeFrame() {
		this.setTitle("JTree штука");
		this.setContentPane(panelMain);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		FileTreeModel treeModel = new FileTreeModel(tree);
		FileTreeCellRenderer cellRenderer = new FileTreeCellRenderer();
		jTree.setCellRenderer(cellRenderer);
		jTree.setModel(treeModel);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.getRoot());
		JTreeUtils.fillJTreeRoot(root, tree);
		jTree = new JTree(root);
	}
}
