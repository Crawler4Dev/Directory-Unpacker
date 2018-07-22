package me.crawler4dev.directory.unpacker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Directory-Unpacker");
		JTextField path = new JTextField();
		JButton open = new JButton("Open directory");
		JButton unpack = new JButton("Unpack directories");
		JFileChooser chooser = new JFileChooser();
		JLabel status = new JLabel("Select a directory whose subdirectories are unpacked");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 360);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setIconImage(new ImageIcon(Main.class.getResource("icon.png").getPath()).getImage());
		unpack.setForeground(Color.DARK_GRAY);
		open.setForeground(Color.DARK_GRAY);
		status.setForeground(Color.RED);
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(chooser.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					if(f != null && f.isDirectory()) {
						path.setText(f.getAbsolutePath());
						setStatus(frame, status, DIRECTORY_IS_SELECTED);
					} else {
						setStatus(frame, status, SELECT_A_DIRECTORY);
					}
				} else {
					File f = new File(path.getText());
					if(f != null && f.isDirectory()) {
						path.setText(f.getAbsolutePath());
						setStatus(frame, status, DIRECTORY_IS_SELECTED);
					} else {
						setStatus(frame, status, SELECT_A_DIRECTORY);
					}
				}
			}
		});
		unpack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooser.getSelectedFile() != null && chooser.getSelectedFile().isDirectory()) {
					File chooseFile = chooser.getSelectedFile();
					for(File f : chooseFile.listFiles()) {
						if(f.isDirectory()) {
							try {
								FileHelper.copyFiles(f, chooseFile);
								FileHelper.deleteFile(f);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					setStatus(frame, status, DIRECTORY_UNPACKED);
				} else {
					setStatus(frame, status, SELECT_A_DIRECTORY);
				}
			}
		});
		path.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				File f = new File(path.getText());
				chooser.setSelectedFile(f);
				if(f != null && f.isDirectory()) {
					setStatus(frame, status, DIRECTORY_IS_SELECTED);
				} else {
					setStatus(frame, status, SELECT_A_DIRECTORY);
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
		path.setBounds(40, 70, 420, 30);
		open.setBounds(500, 70, 100, 30);
		unpack.setBounds(40, 200, 560, 30);
		frame.add(status, BorderLayout.CENTER);
		frame.add(path, BorderLayout.CENTER);
		frame.add(open, BorderLayout.CENTER);
		frame.add(unpack, BorderLayout.CENTER);
		frame.add(new JLabel(""), BorderLayout.CENTER);
	}
	
	private static int SELECT_A_DIRECTORY = 1;
	private static int DIRECTORY_UNPACKED = 2;
	private static int DIRECTORY_IS_SELECTED = 3;
	
	private static void setStatus(JFrame frame, JLabel status, int TYPE) {
		if(TYPE == SELECT_A_DIRECTORY) {
			status.setText("Select a directory whose subdirectories are unpacked");
			status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
			status.setForeground(Color.RED);
		} else if(TYPE == DIRECTORY_UNPACKED) {
			status.setText("All directories has been unpacked");
			status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
			status.setForeground(Color.GREEN);
		} else if(TYPE == DIRECTORY_IS_SELECTED) {
			status.setText("A directory is selected");
			status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
			status.setForeground(Color.DARK_GRAY);
		}
	}

}
