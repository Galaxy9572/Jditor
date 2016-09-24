package org.ljy.util;

import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.UIManager;

import org.ljy.ui.MyEditor;

public class Tools {
	private static Font menufont = new Font("΢���ź�", Font.PLAIN, 14);// UI����
	private static String time;
	private static MyEditor edp = MyEditor.getInstance();

	public static void setFont() {
		UIManager.put("OptionPane.font", menufont);
		UIManager.put("OptionPane.messageFont", menufont);
		UIManager.put("OptionPane.buttonFont", menufont);
		UIManager.put("MenuBar.font", menufont);
		UIManager.put("MenuItem.font", menufont);
		UIManager.put("Menu.font", menufont);
		UIManager.put("PopupMenu.font", menufont);
		UIManager.put("ToolBar.font", menufont);
		UIManager.put("ComboBox.font", menufont);
		UIManager.put("Button.font", menufont);
		UIManager.put("CheckBox.font", menufont);
		UIManager.put("CheckBoxMenuItem.font", menufont);
		UIManager.put("Dialog.font", menufont);
		UIManager.put("Panel.font", menufont);
		UIManager.put("Label.font", menufont);
	}

	public static void setTime() {
		Date date = new Date();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = sdf.format(date);
		if (edp.getJta().getText().equals("")) {
			edp.getJta().setText(edp.getJta().getText() + time);
		} else {
			edp.getJta().setText(edp.getJta().getText() + "\n" + time);
		}
	}

	public static int replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest.length();
	}
	
}
