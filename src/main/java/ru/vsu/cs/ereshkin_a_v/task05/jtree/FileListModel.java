package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import ru.vsu.cs.ereshkin_a_v.task05.FileTree.FileTreeNode;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListModel extends AbstractListModel<File> {

	private final List<File> listElements;

	public FileListModel(FileTreeNode directory) {
		this.listElements = new ArrayList<>();
		setDirectory(directory);
	}

	public void setDirectory(FileTreeNode directory){
		listElements.clear();
		for (FileTreeNode dir: directory.getChildNodes()){
			listElements.add(dir.getValue());
		}
		listElements.addAll(directory.getChildValues());
	}

	@Override
	public int getSize() {
		return listElements.size();
	}

	@Override
	public File getElementAt(int index) {
		return listElements.get(index);
	}
}