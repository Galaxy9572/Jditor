package org.ljy.domain;

import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

/**
 * 与config.xml对应的JavaBean
 * @author 廖俊瑶
 *
 */
public class ConfigBean {
	private final String CONFIG_PATH = "src/org/ljy/config/config.xml";
	private Document document;

	public ConfigBean() {
		try {
			document=new SAXReader().read(CONFIG_PATH);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void setFontSize(int fontSize) {
		Node node=document.selectSingleNode("//font-size");
		node.setText(fontSize+"");
	}

	public void setFontColor(int[] fontColor) {
		Node node=document.selectSingleNode("//font-color");
		node.setText(fontColor[0]+","+fontColor[1]+","+fontColor[2]);
	}

	public void setFontStyle(int fontStyle) {
		Node node=document.selectSingleNode("//font-style");
		node.setText(fontStyle+"");
	}

	public void setLineWrap(boolean isLineWrap) {
		Node node=document.selectSingleNode("//line-wrap");
		node.setText(isLineWrap+"");
	}

	public int getFontSize() {
		Node node=document.selectSingleNode("//font-size");
		return Integer.parseInt(node.getText());
	}

	public int[] getFontColor() {
		Node node=document.selectSingleNode("//font-color");
		String[] color=node.getText().split(",");
		int[] fontColor=new int[3];
		for (int i = 0; i < color.length; i++) {
			fontColor[i]=Integer.parseInt(color[i]);
//			System.out.println(color[i]);
//			System.out.println(fontColor[i]);
		}
		return fontColor;
	}

	public int getFontStyle() {
		Node node=document.selectSingleNode("//font-style");
		return Integer.parseInt(node.getText());
	}

	public boolean getLineWrap() {
		Node node=document.selectSingleNode("//line-wrap");
		if("true".equals(node.getText())){
			return true;
		}
		return false;
	}
	
	public void writeToConfig(){
		OutputFormat format=OutputFormat.createPrettyPrint();
		try {
			XMLWriter writer=new XMLWriter(new FileWriter(CONFIG_PATH),format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
