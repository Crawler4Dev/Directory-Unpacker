package me.crawler4dev.directory.unpacker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Directory-Unpacker");
		JTextField path = new JTextField();
		JButton open = new JButton("Open File");
		JButton unpack = new JButton("Unpack directory");
		JFileChooser chooser = new JFileChooser();
		JLabel status = new JLabel("Select a file");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 360);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);  
		frame.setVisible(true);
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
						status.setText("");
					} else {
						status.setText("Select a file");
						status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
						status.setForeground(Color.RED);
					}
				} else {
					File f = new File(path.getText());
					if(f != null && f.isDirectory()) {
						path.setText(f.getAbsolutePath());
						status.setText("");
					} else {
						status.setText("Select a file");
						status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
						status.setForeground(Color.RED);
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
								copyFiles(f, chooseFile);
								deleteFile(f);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					status.setText("Directory unpacked");
					status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
					status.setForeground(Color.GREEN);
				} else {
					status.setText("Select a file");
					status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
					status.setForeground(Color.RED);
				}
			}
		});
		path.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				File f = new File(path.getText());
				chooser.setSelectedFile(f);
				if(f != null && f.isDirectory()) {
					status.setText("");
				} else {
					status.setText("Select a file");
					status.setBounds(frame.getWidth() / 2 - status.getFontMetrics(status.getFont()).stringWidth(status.getText()) / 2, 160, frame.getWidth() / 2, 20);
					status.setForeground(Color.RED);
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
	
	public static void copyFiles(File from, File to) throws IOException {
		if (!to.exists()) {
			to.mkdirs();
		}
		for (File file : from.listFiles()) {
			if (file.isDirectory()) {
				copyFiles(file, new File(to.getAbsolutePath() + "/" + file.getName()));
			} else {
				File n = new File(to.getAbsolutePath() + "/" + file.getName());
				Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	public static void deleteFile(File files) {
		if (files.isDirectory()) {
			for (File file : files.listFiles()) {
				if (file.isDirectory()) {
					deleteFile(file);
					file.delete();
				} else {
					file.delete();
				}
			}
			files.delete();
		} else {
			files.delete();
		}
	}

}
