package org.ljy.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class WordSearch extends JFrame {
	/**
	 * @author LJY
	 */
	private static final long serialVersionUID = 1L;
	private static final WordSearch ws=new WordSearch();
	private Dimension dim;
	private JPanel p1, p2, p3;
	private JLabel search, replace;
	private JTextField jtf1, jtf2;
	private JButton confirm;
	private String source, target;
	private MyEditor edp=MyEditor.getInstance();

	public WordSearch() {
		super("查找/替换");
		dim = this.getToolkit().getScreenSize();
		this.setSize(dim.width / 4, dim.height / 4);
		this.setLayout(new GridLayout(3, 1));
		this.initComponents();
		this.addActionListeners();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setVisible(true);
	}

	private void initComponents() {
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		search = new JLabel("查找：");
		replace = new JLabel("替换：");
		jtf1 = new JTextField(30);
		jtf2 = new JTextField(30);
		confirm = new JButton("确定");
		p1.setLayout(new FlowLayout());
		p2.setLayout(new FlowLayout());
		p3.setLayout(new FlowLayout());
		p1.add(search);
		p1.add(jtf1);
		p2.add(replace);
		p2.add(jtf2);
		p3.add(confirm);
		this.getContentPane().add(p1);
		this.getContentPane().add(p2);
		this.getContentPane().add(p3);
	}

	private void addActionListeners() {
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				source = jtf1.getText();
				target = jtf2.getText();
				if (edp.getJta().getText().contains(source)) {
					String result =edp.getJta().getText().replace(source, target);
					edp.setJta(result);
				}
			}
		});
	}

	public static WordSearch getInstance(){
		return ws;
	}
}
