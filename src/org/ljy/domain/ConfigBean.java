package org.ljy.domain;

import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 与config.xml对应的JavaBean
 * @author 廖俊瑶
 *
 */
public class ConfigBean {
	private final String CONFIG_PATH ="src/org/ljy/config/config.xml";
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
		writeToConfig();
	}

	public void setFontColor(int[] fontColor) {
		Node node=document.selectSingleNode("//font-color");
		node.setText(fontColor[0]+","+fontColor[1]+","+fontColor[2]);
		writeToConfig();
	}

	public void setFontStyle(int[] fontStyle) {
		Node node=document.selectSingleNode("//font-style");
		node.setText(fontStyle[0]+","+fontStyle[1]);
		writeToConfig();
	}

	public void setLineWrap(boolean isLineWrap) {
		Node node=document.selectSingleNode("//line-wrap");
		node.setText(isLineWrap+"");
		writeToConfig();
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
		}
		return fontColor;
	}

	public int[] getFontStyle() {
		Node node=document.selectSingleNode("//font-style");
		String[] arr= node.getText().split(",");
		return new int[]{Integer.parseInt(arr[0]),Integer.parseInt(arr[1])};
	}

	public boolean getLineWrap() {
		Node node=document.selectSingleNode("//line-wrap");
		if("true".equals(node.getText())){
			return true;
		}
		return false;
	}
	
	public void writeToConfig(){
		try {
			OutputFormat format=OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileWriter(CONFIG_PATH),format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
