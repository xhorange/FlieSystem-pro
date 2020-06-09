package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import controller.MainController;
import model.sys.FCB;

/**
 * 
 * @author eason yang
 * 
 */
public class MainView extends JFrame {

	//private static final long serialVersionUID = 6313156717813295316L;

	private JButton backButton;
	public JTextField addressTextField;
	public JButton goButton;
	public JPanel contentPanel;
	final public JComboBox<String> comboBox=new JComboBox<String>(new String[]{"管理员", "用户1", "用户2", "用户3"});
	public JButton changeButton;
	// Constructor
	public MainView(FCB[] fcbDir) {
		super();

		// initialize
		this.contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.backButton = new JButton("后退");
		this.addressTextField = new JTextField("test");
		this.goButton = new JButton("跳转");

		// UI Methods
		this.configureMenuBar();
		this.configureToolPanel();
		this.configureContentScrollPane(fcbDir);

		// Main View
		this.configureJFrame();
		this.setIconImage(new ImageIcon("resource/titleIcon.png").getImage());
	}

	// UI Method
	private void configureJFrame() {
		this.setTitle("File System Simulator");// Set title
		this.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);// Set size of
																// window
		this.setResizable(false);// Can't change the size
		this.setLocationRelativeTo(null);// Set the position of the window -
											// Screen's Center
		this.setBackground(Color.WHITE);
	}

	private void configureMenuBar() {
		// Components
		JMenuBar menuBar;
		JMenu fileSystemMenu;
		JMenu aboutMenu;
		JMenuItem aboutMenuItem;

		// Create the Menu Bar
		menuBar = new JMenuBar();

		// Build Elevator Menu
		fileSystemMenu = new JMenu("文件系统");
		fileSystemMenu.setMnemonic(KeyEvent.VK_F);
		fileSystemMenu.setEnabled(false);

		// Build About Menu
		aboutMenu = new JMenu("关于");
		aboutMenu.setMnemonic(KeyEvent.VK_A);

		// Add Menu Items to Menu "About"
		aboutMenuItem = new JMenuItem("无", KeyEvent.VK_A);
		aboutMenu.add(aboutMenuItem);
		aboutMenu.setEnabled(false);

		// Add Menus "File" and "Help" to Menu Bar
		menuBar.add(fileSystemMenu);
		menuBar.add(aboutMenu);
		menuBar.add(comboBox);
		changeButton=new JButton("切换");
		menuBar.add(changeButton);

		// Add Components
		this.setJMenuBar(menuBar);
	}

	private void configureToolPanel() {
		// initialize toolPanel
		JPanel toolPanel = new JPanel();

		// for debug
		toolPanel.setBackground(Color.WHITE);

		// Set Layout
		toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.X_AXIS));
		toolPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

		// Add to toolPanel
		toolPanel.add(this.backButton);
		toolPanel.add(this.addressTextField);
		toolPanel.add(this.goButton);

		// Add to mainView
		this.add(toolPanel, BorderLayout.PAGE_START);
	}

	private void configureContentScrollPane(FCB[] fcbDir) {
		// Clear all children components
		this.contentPanel.removeAll();

		// Set background color
		this.contentPanel.setBackground(Color.WHITE);

		// Add component listener for contentPanel
		this.contentPanel.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println("resize");

				Dimension d = MainView.this.contentPanel.getPreferredSize();
				int con = MainView.this.contentPanel.getComponents().length;
				int col = Config.WINDOW_WIDTH
						/ (Config.FILE_ICON_PANEL_SIZE + 5);
				int row = con / col + 1;
				int newHeight = row * (Config.FILE_ICON_PANEL_SIZE + 5) + 5;
				d.height = newHeight;
				d.width = Config.WINDOW_WIDTH;
				MainView.this.contentPanel.setPreferredSize(d);

			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		// For test
		for (int i = 0; i < fcbDir.length; i++) {
			if (fcbDir[i] == null) {
				break;
			}

			DocumentIconPanel t = new DocumentIconPanel(fcbDir[i].type,
					fcbDir[i].filename);

			this.contentPanel.add(t);
		}

		// initialize content scroll pane
		System.out.println("initialize contentScrollPane");
		JScrollPane contentScrollPane = new JScrollPane(this.contentPanel);
		contentScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// Add to mainView
		System.out.println("add to mainView");
		this.add(contentScrollPane, BorderLayout.CENTER);
	}

	// Show view
	public void showView() {
		System.out.println("show view");

		// Show View
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	// Actions
	public void addDocument(DocumentIconPanel documentIconPanel) {
		// For test
		this.contentPanel.add(documentIconPanel);
		this.contentPanel.revalidate();
	}

	public void addRightClickListener(MouseListener rightClickListener) {
		this.contentPanel.addMouseListener(rightClickListener);
	}

	public void addBackButtonActionListener(ActionListener actionListener) {
		this.backButton.addActionListener(actionListener);
	}

	public void addGoButtonActionListener(ActionListener actionListener) {
		this.goButton.addActionListener(actionListener);
	}
	public void addchangeButtonActionListener(ActionListener actionListener) {
		this.changeButton.addActionListener(actionListener);
	}

	public void addDocumentIconPanelMouseListener(
			MouseListener documentIconPanelMouseListener) {
		System.out.println("add document listener");
		for (Component item : this.contentPanel.getComponents()) {
			((DocumentIconPanel) item)
					.addMouseListener(documentIconPanelMouseListener);
		}
	}

	public void deselectDocuments() {
		for (Component item : this.contentPanel.getComponents()) {
			((DocumentIconPanel) item).setSelected(false);
		}
	}

	public void reloadContent(FCB[] fcbDir) {
		// 移除所有components
		this.contentPanel.removeAll();

		// 重新添加
		for (int i = 0; i < fcbDir.length; i++) {
			if (fcbDir[i] == null) {
				break;
			}
			if (fcbDir[i].CreateID== MainController.currentID||fcbDir[i].Authority%10>0||MainController.currentID==0){//权限判定
				DocumentIconPanel t = new DocumentIconPanel(fcbDir[i].type,
						fcbDir[i].filename);
				this.contentPanel.add(t);
			}



		}

		this.contentPanel.repaint();
		this.contentPanel.revalidate();
	}
}
