package dkeep.graphic;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;

public class LevelEditor extends JFrame {
	
	JFrame parent;

	private int noColumns, noRows;
	private String filename;
	protected BufferedWriter out;
	private int xSize=0,ySize=0;
	private char[][] editorBoard;
	private char selectedTile;
	
	//:::::::::::::::::::PANEL ELEMENTS::::::::::::::::::::
	private JComboBox rows, columns;
	private JTextField name;
	private JPanel editorPanel, editorPart, intro;
	private JButton hero,wall,key,ogre,floor,door;
	private final ButtonGroup tileButton = new ButtonGroup();
	//:::::::::::::::::::::::::::::::::::::::::::::::::::::

	public LevelEditor(JFrame parent) throws HeadlessException {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setTitle("Level Editor");
		setBounds(100, 100, 850, 800);
		this.parent=parent;
		
		// ::::::::::::::::::MASTER:::::::::::::::::::::::::
		hero = new JButton("Hero");
		tileButton.add(hero);
		hero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectedTile = 'A';
			}
		});
		hero.setEnabled(false);
		hero.setBounds(620, 65, 130, 30);
		getContentPane().add(hero);

		ogre = new JButton("Ogre");
		tileButton.add(ogre);
		ogre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedTile = 'O';
			}
		});
		ogre.setEnabled(false);
		ogre.setBounds(620, 115, 130, 30);
		getContentPane().add(ogre);

		wall = new JButton("Wall");
		tileButton.add(wall);
		wall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedTile = 'x';
			}
		});
		wall.setEnabled(false);
		wall.setBounds(620, 215, 130, 30);
		getContentPane().add(wall);

		door = new JButton("Door");
		tileButton.add(door);
		door.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedTile = 'i';
			}
		});
		door.setEnabled(false);
		door.setBounds(620, 165, 130, 30);
		getContentPane().add(door);

		key = new JButton("Key");
		tileButton.add(key);
		key.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedTile = 'k';
			}
		});
		key.setEnabled(false);
		key.setBounds(620, 265, 130, 30);
		getContentPane().add(key);

		floor = new JButton("Floor");
		tileButton.add(floor);
		floor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedTile = ' ';
			}
		});
		floor.setEnabled(false);
		floor.setBounds(620, 314, 131, 31);
		getContentPane().add(floor);
		// :::::::::::::::::::::::::::::::::::::::::::::::::

		
		// ::::::::::::::::::INTRO PANEL::::::::::::::::::::
		intro = new JPanel();
		intro.setBorder(new LineBorder(new Color(0, 0, 0)));
		intro.setBounds(30, 65, 500, 200);
		getContentPane().add(intro);
		intro.setLayout(null);

		JLabel labelRows = new JLabel("Rows");
		labelRows.setHorizontalAlignment(SwingConstants.CENTER);
		labelRows.setBounds(324, 0, 159, 64);
		intro.add(labelRows);

		JLabel labelColumns = new JLabel("Columns");
		labelColumns.setHorizontalAlignment(SwingConstants.CENTER);
		labelColumns.setBounds(17, 0, 159, 64);
		intro.add(labelColumns);

		columns = new JComboBox();
		columns.setModel(new DefaultComboBoxModel(new String[] { "3", "4", "5", "6", "7", "8", "9", "10" }));
		columns.setBounds(17, 83, 159, 29);
		intro.add(columns);

		rows = new JComboBox();
		rows.setModel(new DefaultComboBoxModel(new String[] { "3", "4", "5", "6", "7", "8", "9", "10" }));
		rows.setBounds(324, 83, 159, 29);
		intro.add(rows);

		JLabel labelName = new JLabel("Level Name");
		labelName.setHorizontalAlignment(SwingConstants.CENTER);
		labelName.setBounds(193, 0, 114, 64);
		intro.add(labelName);

		name = new JTextField();
		name.setBounds(175, 83, 151, 29);
		name.setColumns(10);
		intro.add(name);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}
		});
		btnExit.setBounds(363, 150, 120, 30);
		intro.add(btnExit);

		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name.getText()), "utf-16"));
				} catch (UnsupportedEncodingException | FileNotFoundException e1) {
					System.out.println("erro a abrir a file");
					JOptionPane.showMessageDialog(parent, "That Level Name is not valid", "Invalid Level Name!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				start();
			}
		});
		start.setBounds(17, 150, 120, 31);
		intro.add(start);
		intro.setVisible(true);
		// ::::::::::::::::::::::::::::::::::::::::::::::::
		
		
		//:::::::::::::::::EDITOR PANEL::::::::::::::::::::

		editorPanel = new JPanel();
		editorPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = 0, j = 0;
				i = e.getX() / xSize;
				j = e.getY() / ySize;
				System.out.println("j=" + j);
				System.out.println("i=" + i);
				System.out.println("tile=" + selectedTile);
				editorPanel.getComponents()[(i+(j*noColumns))].getGraphics().drawImage(getImage(selectedTile), 0, 0, xSize-1,ySize-1, editorPanel);
				editorBoard[j][i] = selectedTile;
			}
		});
		editorPanel.setBorder(null);
		editorPanel.setBounds(30, 65, 500, 500);
		getContentPane().add(editorPanel);
		editorPanel.setLayout(null);
		
		//:::::::::::::::::::::::::::::::::::::::::::::::::
		
		
		JButton btnExitAndSave = new JButton("Exit and Save");
		btnExitAndSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		btnExitAndSave.setBounds(30, 584, 212, 43);
		getContentPane().add(btnExitAndSave);
		
		JButton btnExitWithoutSaving = new JButton("Exit without Saving");
		btnExitWithoutSaving.setBounds(30, 646, 212, 43);
		getContentPane().add(btnExitWithoutSaving);
		editorPanel.setVisible(false);
	}

	//:::::::::::::::::::::::EXIT AND START::::::::::::::::
	@Override
	public void dispose() {
		super.dispose();
		try {
			out.close();
		} catch (IOException e) {
			System.out.println("file nao fechou");
			e.printStackTrace();
		}
		parent.setVisible(true);
	}
	
	protected void exit() {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.setVisible(false);
		dispose();
	}

	public void start() {
		noRows=Integer.parseInt((String) rows.getSelectedItem());
		noColumns=Integer.parseInt((String)columns.getSelectedItem());
		System.out.print(noRows);
		System.out.print(noColumns);
		try {
			out.write((String) rows.getSelectedItem());
			out.write(' ');
			out.write((String) columns.getSelectedItem());
			out.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
		xSize=500/noColumns;
		ySize=500/noRows;
		loadEditor();
		editorBoard = new char[noRows][noColumns];
		hero.setEnabled(true);
		ogre.setEnabled(true);
		door.setEnabled(true);
		wall.setEnabled(true);
		floor.setEnabled(true);
		key.setEnabled(true);
		
	}
	//:::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	public void loadEditor(){
		for(int j = 0; j<noRows;j++){
			for(int i = 0; i<noColumns;i++){
				editorPart = new JPanel();
				editorPart.setBorder(new LineBorder(new Color(0, 0, 0)));
				editorPart.setBounds(i*xSize,j*ySize,xSize,ySize);
				editorPanel.add(editorPart);
			}
		}
		intro.setVisible(false);
		editorPanel.setVisible(true);
	}
	
	protected Image getImage(char c) {
		try {
			switch (c) {
			case 'A':return ImageIO.read(new File("src/resources/armed.jpg"));
			case 'x':return ImageIO.read(new File("src/resources/wall.jpg"));
			case 'i':return ImageIO.read(new File("src/resources/door.jpg"));
			case 'O':return ImageIO.read(new File("src/resources/ogre.jpg"));
			case 'k':return ImageIO.read(new File("src/resources/DD-Transparent.png"));
			case 'K':return ImageIO.read(new File("src/resources/DD-Transparent.png"));
			case '*':return ImageIO.read(new File("src/resources/attack.jpg"));
			case '8':return ImageIO.read(new File("src/resources/sleepogre.jpg"));
			case ' ':return ImageIO.read(new File("src/resources/floor.jpg"));
			default:return ImageIO.read(new File("src/resources/default.jpg"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
