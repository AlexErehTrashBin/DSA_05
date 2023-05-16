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
	private FileTree fileTree;

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
			int childNodesLength = fileTreeNode.numberOfChildrenNodes();
			if (index < 0 || index > getChildCount(parent)) return null;
			if (index > (childNodesLength - 1)){
				// Перебор по файлам
				return fileTreeNode.getChildValue(index - childNodesLength);
			} else {
				// Перебор по поддиректориям
				return fileTreeNode.getChildNode(index);
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof FileTreeNode) {
			FileTreeNode fileTreeNode = (FileTreeNode) parent;
			int childNodesLength = fileTreeNode.numberOfChildrenNodes();
			int childValuesLength = fileTreeNode.numberOfChildrenValues();
			return childNodesLength + childValuesLength;
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		return node instanceof File;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (!(parent instanceof FileTreeNode)) return -1;
		FileTreeNode parentNode = (FileTreeNode) parent;
		boolean isChildSubDir = child instanceof FileTreeNode;
		boolean isChildFile = child instanceof File;
		if (!isChildFile && !isChildSubDir) return -1;
		int indexOffset = isChildSubDir ? 0 : parentNode.numberOfChildrenNodes();
		if (child instanceof FileTreeNode) {
			FileTreeNode node = (FileTreeNode) child;
			for (int i = 0; i < parentNode.numberOfChildrenNodes(); i++) {
				if (parentNode.getChildNodes().get(i) == node) {
					return i + indexOffset;
				}
			}
		}
		else {
			File file = (File) child;
			for (int i = 0; i < parentNode.numberOfChildrenValues(); i++) {
				if (parentNode.getChildValues().get(i) == file) {
					return i + indexOffset;
				}
			}
		}

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
