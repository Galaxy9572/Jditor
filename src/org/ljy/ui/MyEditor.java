package org.ljy.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ljy.common.MyMouseAdapter;
import org.ljy.common.MyWindowAdapter;
import org.ljy.domain.ConfigBean;
import org.ljy.util.IOUtils;
import org.ljy.util.SundriesUtils;

public class MyEditor extends JFrame implements ActionListener {
	/**
	 * @author LJY 已知BUG：换行有问题 待完成：文件加密模块
	 */
	private static final long serialVersionUID = 1L;
	private static MyEditor edp = new MyEditor();
	private static final JFileChooser jfc = new JFileChooser();
	private static FileNameExtensionFilter txtFilter, edtFilter, javaFilter;
	private ConfigBean configBean;
	private JMenuBar jmb;// 菜单条
	private JMenu jmFile, jmEdit, jmHelp;// 菜单
	private JMenuItem jmtAbout, jmtFiles[], jmtEdits[];// 菜单项
	private JMenuItem popMenuItems[];
	private JToolBar jtb;// 工具条
	private JTextArea jta;// 文本域
	private JPopupMenu popMenu;// 弹出式菜单
	private JComboBox<String> jcbFont, jcbSize;// 组合框
	private JButton jb_fontColor, jb_clearAll;// 按钮
	private Color color;// 颜色
	private JLabel jlFont, jlSize, jlStyle, jlColor, jlStastic;// 标签
	private Font font;
	private JCheckBox jcbBold, jcbItalic;// 复选框
	private JCheckBoxMenuItem jmtLinewrap;// 复选菜单项
	private JPanel p1, p2, p3;
	private Dimension dim;
	private String path;
	private int stastic;
	private long fileSize;
	private boolean isSaved = false, isUpdated = false;
	private int fontSize;//字体大小
	private int[] fontColor;//字体颜色
	private int[] fontStyle;//字体风格：粗体、斜体、正常
	private boolean lineWrap;//自动换行
	private int style;

	/**
	 * 构造方法
	 */
	private MyEditor() {
		super("MyEditor");// 设置窗口标题
		dim = this.getToolkit().getScreenSize();// 获取屏幕分辨率
		this.setSize(dim.width * 2 / 3, dim.height * 2 / 3);// 设置窗口大小
		SundriesUtils.setFont();// 把各组件的字体设置为menufont
		this.initComponents();
		this.initDefaultConfig();
		this.addListeners();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void initDefaultConfig(){
		configBean=new ConfigBean();
		fontSize=configBean.getFontSize();//字体大小
		fontColor=configBean.getFontColor();//字体颜色
		fontStyle=configBean.getFontStyle();//字体风格：粗体、斜体、正常
		lineWrap=configBean.getLineWrap();//自动换行
		style = font.getStyle();
		Color color=new Color(fontColor[0],fontColor[1],fontColor[2]);
		jta.setForeground(color);
		jlColor.setForeground(color);
		jta.setLineWrap(lineWrap);
		if(fontStyle[0]==0 && fontStyle[1]==0){
			jcbBold.setSelected(false);
			jcbItalic.setSelected(false);
			style=0;
		}else if(fontStyle[0]==1 && fontStyle[1]==0){
			jcbBold.setSelected(true);
			jcbItalic.setSelected(false);
			style=2;
		}else if(fontStyle[0]==0 && fontStyle[1]==1){
			jcbBold.setSelected(false);
			jcbItalic.setSelected(true);
			style=1;
		}else if(fontStyle[0]==1 && fontStyle[1]==1){
			jcbBold.setSelected(true);
			jcbItalic.setSelected(true);
			style=3;
		}
		jta.setFont(new Font("微软雅黑",style, fontSize));
		jcbSize.setSelectedIndex(fontSize-1);
		jmtLinewrap.setSelected(lineWrap);
	}
	
	/**
	 * 初始化主界面
	 */
	private void initComponents() {
		jmb = new JMenuBar();
		this.setJMenuBar(jmb);
		// 创建菜单
		jmFile = new JMenu("文件");
		jmEdit = new JMenu("编辑");
		jmHelp = new JMenu("帮助");
		// 创建菜单项
		String[] jmfileStr = { "新建", "打开", "保存", "另存为", "退出" };
		jmtFiles = new JMenuItem[jmfileStr.length];
		for (int i = 0; i < jmfileStr.length; i++) {
			jmtFiles[i] = new JMenuItem(jmfileStr[i]);
			jmtFiles[i].addActionListener(this);
			jmFile.add(jmtFiles[i]);
			if (i == 3) {
				jmFile.addSeparator();
			}
		}
		jmtFiles[2].setEnabled(false);
		String[] jmeditStr = { "撤销", "剪切 ", "复制 ", "粘贴 ", "时间", "清空文本", "查找" };
		jmtEdits = new JMenuItem[jmeditStr.length];
		for (int i = 0; i < jmeditStr.length; i++) {
			jmtEdits[i] = new JMenuItem(jmeditStr[i]);
			jmtEdits[i].addActionListener(this);
			jmEdit.add(jmtEdits[i]);
			if (i == 3 || i == 4) {
				jmEdit.addSeparator();
			}
		}
		jmtLinewrap = new JCheckBoxMenuItem("自动换行");
		jmtAbout = new JMenuItem("关于");

		// 将菜单添加到菜单栏中
		jmb.add(jmFile);
		jmb.add(jmEdit);
		jmb.add(jmHelp);
		jmEdit.add(jmtLinewrap);
		jmHelp.add(jmtAbout);

		// 创建JToolBar
		jtb = new JToolBar();
		jtb.setLayout(new FlowLayout(FlowLayout.LEFT));

		// 创建JTextArea
		jta = new JTextArea();
		font = jta.getFont();

		// 创建面板1
		p1 = new JPanel();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();// 获取本地绘图环境
		String[] fontsName = ge.getAvailableFontFamilyNames();// 获取系统中的字体
		jcbFont = new JComboBox<String>(fontsName);
		for (int i = 0; i < fontsName.length; i++) {
			if (fontsName[i].equals("宋体")) {
				jcbFont.setSelectedIndex(i);
				break;
			}
		}
		jlFont = new JLabel("字体: ");
		p1.setOpaque(false);// 设置面板变透明
		jcbFont.addActionListener(this);
		p1.add(jlFont);
		p1.add(jcbFont);
		jtb.add(p1);

		// 创建面板2
		p2 = new JPanel();
		String sizeStrs[] = new String[60];
		for (int i = 0; i < 60; i++) {
			sizeStrs[i] = String.valueOf(i + 1);
		}
		jlSize = new JLabel(" 字号:");
		jcbSize = new JComboBox<String>(sizeStrs);
		p2.setOpaque(false);
		p2.add(jlSize);
		jcbSize.setEditable(false);
		jcbSize.setSelectedIndex(15);// 设置默认选中项为第15项
		jcbSize.setEditable(false);
		jcbSize.addActionListener(this);
		p2.add(jcbSize);
		jtb.add(p2);

		// 创建面板3
		p3 = new JPanel();
		jb_fontColor = new JButton("字体颜色");
		jcbBold = new JCheckBox("粗体");
		jcbItalic = new JCheckBox("斜体");
		jlStyle = new JLabel(" 字型 :");
		jlColor = new JLabel("▇ ");
		jb_clearAll = new JButton("清空文本");
		p3.setOpaque(false);// 设置面板为透明
		jcbItalic.setOpaque(false);
		jcbBold.setOpaque(false);
		jb_fontColor.setForeground(color);

		jcbBold.addActionListener(this);
		jcbItalic.addActionListener(this);

		p3.add(jb_fontColor);
		p3.add(jlColor);
		p3.add(jlStyle);
		p3.add(jcbBold);
		p3.add(jcbItalic);
		p3.add(jb_clearAll);
		jtb.add(p3);
		this.getContentPane().add(jtb, "North");// 把JToolBar添加到面板的北边

		// 创建统计字数和文件大小的JLabel
		jlStastic = new JLabel("字数：" + stastic + " " + "文件大小：" + fileSize + " B");
		jlStastic.setBackground(Color.WHITE);
		jlStastic.setOpaque(true);
		this.getContentPane().add(jlStastic, "South");

		// 创建JPopupMenu
		popMenu = new JPopupMenu();
		String menuItemStrs[] = { "剪切 ", "复制 ", "粘贴 " };
		popMenuItems = new JMenuItem[menuItemStrs.length];
		for (int i = 0; i < popMenuItems.length; i++) {
			popMenuItems[i] = new JMenuItem(menuItemStrs[i]);
			popMenu.add(popMenuItems[i]);
			if (i == 1) {
				popMenu.addSeparator();
			}
			popMenuItems[i].addActionListener(this);
		}
		jta.add(popMenu);
		this.getContentPane().add(new JScrollPane(jta));// 添加带滚动条的JTextArea

	}

	/**
	 * 添加监听器
	 */
	private void addListeners() {
		// 给菜单项添加ActionListener监听器
		jmtLinewrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jmtLinewrap.isSelected()) {
					jta.setLineWrap(true);// 设置为自动换行
				} else {
					jta.setLineWrap(false);// 设置为不自动换行
				}
			}
		});
		jmtAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == jmtAbout) {// “关于”信息
					JOptionPane.showMessageDialog(null, "MyEditor\n版本：1.0 \n设计：LJY", "关于",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		jb_fontColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == jb_fontColor) {// 设置字体颜色事件
					color = JColorChooser.showDialog(null, "选择字体颜色", color);
					jta.setForeground(color);// 设置文本域的颜色
					jlColor.setForeground(color);// 显示字体的颜色
					int[] fontColor={color.getRed(),color.getGreen(),color.getBlue()};
					configBean.setFontColor(fontColor);
				}
			}
		});
		jb_clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == jmtEdits[5] || e.getSource() == jb_clearAll) {// "清空"事件
					jta.setText("");
				}
			}
		});
		jta.addMouseListener(new MyMouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
					popMenu.show(jta, e.getX(), e.getY());// 鼠标右键在文本域中单击，弹出菜单
				}
			}

			public void mouseEntered(MouseEvent e) {
				MyEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));// 设置鼠标样式为文本编辑样式
			}

			public void mouseExited(MouseEvent e) {
				MyEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));// 设置鼠标样式为默认样式
			}
		});
		jta.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				isUpdated = true;
				stastic = SundriesUtils.replaceBlank(jta.getText());// 除去空格和换行符都计数
				try {
					fileSize = jfc.getSelectedFile().length();// TODO:编写文本时不能获得文件大小
				} catch (Exception e1) {
				}
				jlStastic.setText("字数：" + stastic + " " + "文件大小：" + fileSize + " B");
			}
		});
		this.addWindowListener(new MyWindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isSaved == false && isUpdated) {
					if (JOptionPane.showConfirmDialog(MyEditor.this, "文件尚未保存，需要保存吗？", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
						IOUtils.getInstance().saveFileAs();
					}
				}
				if (JOptionPane.showConfirmDialog(null, "确定要提出MyEditor吗？", "退出", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else {
					MyEditor.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}
		});
	}

	/**
	 * ActionListener事件
	 */
	public void actionPerformed(ActionEvent e) {
		String fontName = jcbFont.getSelectedItem().toString();
		int size = Integer.parseInt(jcbSize.getSelectedItem().toString());
		if (jcbBold.isSelected() && (!jcbItalic.isSelected())) {
			fontStyle[0] = 1;
			fontStyle[1]=0;
			style = 2;
			configBean.setFontStyle(fontStyle);
		}
		if(jcbBold.isSelected() && jcbItalic.isSelected()){
			fontStyle[0] = 1;
			fontStyle[1]=1;
			style = 3;
			configBean.setFontStyle(fontStyle);
		}
		if ((!jcbBold.isSelected()) && jcbItalic.isSelected()) {
			fontStyle[0] = 0;
			fontStyle[1]=1;
			style = 1;
			configBean.setFontStyle(fontStyle);
		}
		if((!jcbBold.isSelected()) && (!jcbItalic.isSelected())){
			fontStyle[0] = 0;
			fontStyle[1]=0;
			style = 0;
			configBean.setFontStyle(fontStyle);
		}
		jta.setFont(new Font(fontName, style, size));
		if (e.getSource() == jmtFiles[0]) {// “新建”事件
			IOUtils.getInstance().newFile(path);
		}
		if (e.getSource() == jmtFiles[1]) {// “打开”事件
			IOUtils.getInstance().openFile();
		}
		if (e.getSource() == jmtFiles[2]) {// "保存“事件
			IOUtils.getInstance().saveFile(path);
		}
		if (e.getSource() == jmtFiles[3]) {// "另存为“事件
			IOUtils.getInstance().saveFileAs();
		}
		if (e.getSource() == jmtFiles[4]) {// “退出”事件
			if (isSaved == false && isUpdated) {
				if (JOptionPane.showConfirmDialog(this, "文件尚未保存，需要保存吗？", "提示", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
					IOUtils.getInstance().saveFileAs();
				}
			}
			if (JOptionPane.showConfirmDialog(this, "确定要提出Edit++吗？", "退出", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(0);
			}
		}
		if (e.getSource() == jmtEdits[0]) {// "撤销“事件
			// TODO
		}
		if (e.getSource() == jmtEdits[4]) {// "时间“事件
			SundriesUtils.setTime();
		}
		if (e.getSource() == jmtEdits[6]) {// "查找"事件
			WordSearch.getInstance();
		}

		if (e.getSource() == popMenuItems[0] || e.getSource() == jmtEdits[1]) {// 剪切按钮
			jta.cut();
		}
		if (e.getSource() == popMenuItems[1] || e.getSource() == jmtEdits[2]) {// 复制按钮
			jta.copy();
		}
		if (e.getSource() == popMenuItems[2] || e.getSource() == jmtEdits[3]) {// 粘贴按钮
			jta.paste();
		}
	}

	public JTextArea getJta() {
		return jta;
	}

	public void setJta(String jta) {
		this.jta.setText(jta);
	}

	public static MyEditor getInstance() {
		return edp;
	}

	public void setIsSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public static JFileChooser getJfc() {
		txtFilter = new FileNameExtensionFilter("txt(文本文件)", "txt");
		edtFilter = new FileNameExtensionFilter("edt(加密的文本文件)", "edt");
		javaFilter = new FileNameExtensionFilter("java(Java源代码)", "java");
		jfc.setFileFilter(javaFilter);
		jfc.setFileFilter(edtFilter);
		jfc.setFileFilter(txtFilter);
		jfc.setAcceptAllFileFilterUsed(false);
		return jfc;
	}

	public JMenuItem getJmtFiles(int i) {
		return jmtFiles[i];
	}

	public static FileNameExtensionFilter getTxtFilter() {
		return txtFilter;
	}

	public static FileNameExtensionFilter getEdtFilter() {
		return edtFilter;
	}

	public static void main(String[] args) {
		getInstance();
	}
}