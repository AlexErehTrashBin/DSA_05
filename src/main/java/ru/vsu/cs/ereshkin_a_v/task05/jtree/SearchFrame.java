package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchFrame extends JFrame {
	private JPanel panelMain;
	private JList<File> searchResultJList;
	private final List<File> searchList = new ArrayList<>();
	public SearchFrame(List<File> resultingList){
		searchList.addAll(resultingList);
		this.setTitle("Результаты поиска");
		this.setContentPane(panelMain);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		FileListModel listModel = new FileListModel(searchList);
		searchResultJList.setModel(listModel);
		FileListCellRenderer listRenderer = new FileListCellRenderer();
		searchResultJList.setCellRenderer(listRenderer);
	}
}
