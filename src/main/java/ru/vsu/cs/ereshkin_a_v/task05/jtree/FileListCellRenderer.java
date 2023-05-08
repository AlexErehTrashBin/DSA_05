package ru.vsu.cs.ereshkin_a_v.task05.jtree;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileListCellRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(
			JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component before = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof File && before instanceof ListCellRenderer){
			((DefaultListCellRenderer) before).setText(((File) value).getName());
		}
		return before;
	}
}
