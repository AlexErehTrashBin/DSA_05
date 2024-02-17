package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import ru.vsu.cs.ereshkin_a_v.task05.FileTree;
import ru.vsu.cs.ereshkin_a_v.task05.FileTree.FileTreeNode;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTreeModel extends DefaultTreeModel {
	private final List<TreeModelListener> listeners = new ArrayList<>();
	private final FileTree fileTree;

	public FileTreeModel(FileTree tree) {
		super(new DefaultMutableTreeNode(tree.getRoot()));
		this.fileTree = tree;
	}

	public void setRootFileTree(File root) {
		fileTree.setRoot(root);
		super.setRoot(new DefaultMutableTreeNode(root));
	}

	@Override
	public Object getRoot() {
		return fileTree.getRoot();
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent == null) return null;
		if (parent instanceof FileTreeNode){
			FileTreeNode fileTreeNode = (FileTreeNode) parent;
			return fileTreeNode.getChild(index);
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof FileTreeNode) {
			FileTreeNode fileTreeNode = (FileTreeNode) parent;
			return fileTreeNode.numberOfChildren();
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		FileTreeNode treeNode = (FileTreeNode) node;
		return !treeNode.isDirectory();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (!(parent instanceof FileTreeNode)) return -1;
		if (!(child instanceof FileTreeNode)) return -1;
		FileTreeNode parentNode = (FileTreeNode) parent;
		FileTreeNode childNode = (FileTreeNode) child;
		parentNode.getChildren().indexOf(childNode);
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		if (l != null && !listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		if (l != null) {
			listeners.remove(l);
		}
	}
}
