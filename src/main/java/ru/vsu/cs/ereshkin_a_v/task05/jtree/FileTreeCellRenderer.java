package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import ru.vsu.cs.ereshkin_a_v.task05.FileTree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(
				tree, value, sel,
				expanded, leaf, row,
				hasFocus);
		if (leaf) {
			if (value instanceof File) {
				setText(((File) value).getName());
			}
		} else {
			if (value instanceof FileTree.FileTreeNode) {
				// TODO МБ здесь что-то будет для исправления разворачивания пустой папки.
			}
		}
		return this;
	}
}
