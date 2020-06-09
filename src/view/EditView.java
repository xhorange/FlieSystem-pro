package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.MainController;
import model.sys.FCB;

// TODO - layout views
public class EditView extends JFrame implements DocumentListener {

	//private static final long serialVersionUID = 5359647733388619559L;
	private JTextPane textPane;

	private FCB dataFCB;
	
	public boolean saveOnExit = true;
	public boolean edited = false;

	public EditView(FCB fcb, String content) {
		super();

		// initialize
		this.dataFCB = fcb;
		this.textPane = new JTextPane();

		this.configureMenuBar();
		this.configureTextPane(content);

		// Main View
		this.configureJFrame();
	}

	// UI Method
	private void configureJFrame() {
		//this.setTitle(this.dataFCB.filename + " - 可编辑");// Set title
		this.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);// Set size of
																// window
		this.setResizable(false);// Can't change the size
		this.setLocationRelativeTo(null);// Set the position of the window -
											// Screen's Center
		this.setBackground(Color.WHITE);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void configureMenuBar() {
		// Components
		JMenuBar menuBar;
		JMenu fileMenu;
		JMenu helpMenu;
		JMenuItem helpMenuItem;

		// Create the Menu Bar
		menuBar = new JMenuBar();

		// Build File Menu
		fileMenu = new JMenu("开始");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.setEnabled(false);
		
		// Build About Menu
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		// Add Menu Items to Menu "About"
		helpMenuItem = new JMenuItem("Help", KeyEvent.VK_H);
		helpMenu.add(helpMenuItem);

		// Add Menus "File" to Menu Bar
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		// Add Components
		this.setJMenuBar(menuBar);
	}
	/**
	 * 权限选择，管理员对于所有都可编辑，创建者需要根据自己的权限做出判定
	 */
	private void configureTextPane(String content) {
		int user= MainController.currentID;
		int creat_Authority=(this.dataFCB.Authority%100)/10;
		int user_Authority=this.dataFCB.Authority%10;
		if(user==0){
			this.setTitle(this.dataFCB.filename + " - 可编辑");
			System.out.println("管理员打开");
			this.textPane.setText(content);
			this.textPane.getDocument().addDocumentListener(this);
			this.add(this.textPane, BorderLayout.CENTER);
		}
		else if(user==this.dataFCB.CreateID){
			System.out.println("文件所有者打开,权限"+creat_Authority);
			if(creat_Authority==6){
				this.setTitle(this.dataFCB.filename + " - 可编辑");
				this.textPane.setText(content);
				this.textPane.getDocument().addDocumentListener(this);
				this.add(this.textPane, BorderLayout.CENTER);
			}
			else if(creat_Authority==4){
				this.setTitle(this.dataFCB.filename + " - 不可编辑");
				this.textPane.setText(content);
				this.textPane.getDocument().addDocumentListener(this);
				this.textPane.setEnabled(false);
				this.add(this.textPane, BorderLayout.CENTER);

			}
		}
		else if(user!=this.dataFCB.CreateID){
			System.out.println("其他用户打开,权限"+user_Authority);
			if(user_Authority==6){
				this.textPane.setText(content);
				this.textPane.getDocument().addDocumentListener(this);
				this.add(this.textPane, BorderLayout.CENTER);
			}
			else if(user_Authority==4){
				this.setTitle(this.dataFCB.filename + " - 不可编辑");
				this.textPane.setText(content);
				this.textPane.getDocument().addDocumentListener(this);
				this.textPane.setEnabled(false);
				this.add(this.textPane, BorderLayout.CENTER);

			}
		}



	}

	/**
	 * 获取当前TextPane的内容
	 * 
	 * @return TextPane中所有文字内容
	 */
	public String getContent() {
		return this.textPane.getText();
	}
	
	public FCB getDataFCB() {
		return dataFCB;
	}

	// Document Listener
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.edited = true;
		
		// 改变窗口标题
		this.setTitle(this.dataFCB.filename + " - *未保存");
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		this.edited = true;
		
		// 改变窗口标题
		this.setTitle(this.dataFCB.filename + " - *未保存");
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}

}
