package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import ru.vsu.cs.ereshkin_a_v.task05.FileTree;
import ru.vsu.cs.ereshkin_a_v.task05.FileTree.FileTreeNode;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class JTreeUtils {
	private static void fillJTreeRoot(DefaultMutableTreeNode root, FileTreeNode rootNode){
		for (FileTreeNode subdir : rootNode.getChildNodes()){
			DefaultMutableTreeNode subdirTreeNode = new DefaultMutableTreeNode(subdir);
			root.add(subdirTreeNode);
			fillJTreeRoot(subdirTreeNode, subdir);
		}
		for (File file : rootNode.getChildValues()){
			DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file);
			root.add(fileNode);
		}
	}
	public static void fillJTreeRoot(DefaultMutableTreeNode root, FileTree fileTree){
		fillJTreeRoot(root, fileTree.getRoot());
	}
}
