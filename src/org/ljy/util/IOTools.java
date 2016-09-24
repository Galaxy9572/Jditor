package org.ljy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.ljy.ui.MyEditor;

public class IOTools {
	private static IOTools iot=new IOTools();
	private FileOutputStream fos;//文件输出流
	private OutputStreamWriter osr;
	private FileInputStream fis;//文件输入流
	private InputStreamReader isr;
	private MyEditor edp=MyEditor.getInstance();
	private JFileChooser jfc=MyEditor.getJfc();
	private String ext="";
	
	public void newFile(String path) {
		try {
			jfc.showDialog(null, "新建");
			if(jfc.getFileFilter()==MyEditor.getTxtFilter()){
				ext=".txt";
			}else{
				ext=".edt";
			}
			path = jfc.getSelectedFile().getAbsolutePath() + ext;
			fos=new FileOutputStream(path);
			osr=new OutputStreamWriter(fos);
			edp.getJmtFiles(2).setEnabled(true);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "文件新建错误");
		} catch (Exception e2) {
		}
	}
	
	public void openFile() {
		fis=null;
		isr=null;
		char[] buffer = new char[1024];
		StringBuffer sb=new StringBuffer();
		int num = 0;
		try {
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.showOpenDialog(null);
			fis = new FileInputStream(jfc.getSelectedFile().getAbsolutePath());
			isr=new InputStreamReader(fis);
			while((num=isr.read(buffer))>0){
				for (int i = 0; i < num; i++) {
					sb.append(buffer[i]);
				}
			}
			edp.getJta().setText(sb.toString());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "找不到该文件");
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "文件读取错误");
		} catch (Exception e2) {
		} finally {
			try {
				fis.close();
				isr.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "文件无法关闭");
			} catch (Exception e2) {
			}
		}
	}

	public void saveFile(String path) {
		try {
			fos=new FileOutputStream(path);
			osr=new OutputStreamWriter(fos);
			osr.write(edp.getJta().getText());
			osr.flush();
			edp.setIsSaved(true);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "文件保存错误");
		} catch (Exception e) {
		} finally {
			try {
				fos.close();
				osr.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "文件无法关闭");
			} catch (Exception e2) {
			}
		}
	}

	public void saveFileAs() {
		try {
			jfc.showDialog(null, "另存为");
			if(jfc.getFileFilter()==MyEditor.getTxtFilter()){
				ext=".txt";
				fos=new FileOutputStream(jfc.getSelectedFile().getAbsolutePath()+ ext);
			}else if(jfc.getFileFilter()==MyEditor.getEdtFilter()){
				ext=".edt";
				fos=fileEncrypt(new File(jfc.getSelectedFile().getAbsolutePath()+ ext));
			}else{
				ext=".java";
				fos=new FileOutputStream(jfc.getSelectedFile().getAbsolutePath()+ ext);
			}
			osr=new OutputStreamWriter(fos);
			osr.write(edp.getJta().getText());
			osr.flush();
			edp.setIsSaved(true);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "文件保存错误");
		} catch (Exception e) {
		} finally {
			try {
				fos.close();
				osr.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "文件无法关闭");
			} catch (Exception e2) {
			}
		}
	}
	
	public FileOutputStream fileEncrypt(File file){
		//TODO：写加密算法
		return fos;
	}

	public static IOTools getInstance(){
		return iot;
	}
}
