package ru.vsu.cs.ereshkin_a_v.task05;

import ru.vsu.cs.ereshkin_a_v.task05.jtree.JTreeFrame;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import static java.awt.Frame.MAXIMIZED_BOTH;

public class Main {
	public static void main(String[] args) throws Exception {
		Locale.setDefault(Locale.ROOT);

		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		SwingUtils.setDefaultFont("Arial", 14);
		EventQueue.invokeLater(() -> {
			try {
				//JFrame frameMain = new TreeDemoFrame();
				JFrame frameMain = new TreeDemoFrame();
				frameMain.setVisible(true);
				frameMain.setExtendedState(MAXIMIZED_BOTH);
			} catch (Exception ex) {
				SwingUtils.showErrorMessageBox(ex);
			}
		});
	}
}