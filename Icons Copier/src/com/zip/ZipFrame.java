package com.zip;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class ZipFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	ZipUnzipController controller = new ZipUnzipController();
	JLabel projectLbl = new JLabel("Choose your project folder");
	JTextField projectTF = new JTextField();
	JLabel imagesZipLbl = new JLabel("Choose zip file downloaded from design.google.com/icons");
	JTextField imagesZipTF = new JTextField();
	JButton projectBtn = generateNimbusButton("Browse");
	JButton imagesBtn = generateNimbusButton("Browse");
	JButton copyImages = generateNimbusButton("Copy Images");
	JButton samplePaths = generateNimbusButton("Show Sample Paths");
	String imagesPath;
	String projectDirPath;

	public ZipFrame() {
		super("Image Transfer");
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(550, 300);
		setLocationRelativeTo(null);
		setLookAndFeel();

		addComp(projectLbl, 10, 10, 400, 25);
		addComp(projectTF, 10, 40, 400, 25);
		addComp(imagesZipLbl, 10, 80, 400, 25);
		addComp(imagesZipTF, 10, 110, 400, 25);

		addComp(projectBtn, 420, 40, 100, 25);
		projectBtn.setActionCommand("projectBtn");
		imagesBtn.setActionCommand("imagesBtn");
		copyImages.setActionCommand("copyImages");
		samplePaths.setActionCommand("samplePaths");

		projectBtn.addActionListener(this);
		imagesBtn.addActionListener(this);
		copyImages.addActionListener(this);
		samplePaths.addActionListener(this);

		addComp(imagesBtn, 420, 110, 100, 25);
		addComp(copyImages, 250, 170, 150, 25);
		addComp(samplePaths, 50, 170, 150, 25);
	}

	private void setLookAndFeel() {
		try {
			// Set cross-platform Java L&F (also called "Metal")
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}

	public JButton generateNimbusButton(String btnName) {
		LookAndFeel nimbus = null;
		try {
			LookAndFeel current = UIManager.getLookAndFeel(); // capture the
			if (nimbus == null) { // only initialize Nimbus once
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
				nimbus = UIManager.getLookAndFeel();
			} else
				UIManager.setLookAndFeel(nimbus); // set look and feel to nimbus
			JButton button = new JButton(); // create the button
			UIManager.setLookAndFeel(current); // return the look and feel to
												// its original state
			button.setText(btnName);
			return button;
		} catch (Exception e) {
			e.printStackTrace();
			return new JButton();
		}
	}

	public void addComp(Component component, int l, int t, int w, int h) {
		component.setBounds(l, t, w, h);
		add(component);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "projectBtn": {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				projectTF.setText(selectedFile.getAbsolutePath().toString() + "\\app\\src\\main\\res");
				projectDirPath = selectedFile.getAbsolutePath().toString();
			}
			break;
		}
		case "imagesBtn": {
			JFileChooser chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.addChoosableFileFilter(new ZipFilter());
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				imagesPath = selectedFile.getAbsolutePath().toString();
				imagesZipTF.setText(imagesPath);
				if (controller.unZip(selectedFile)) {
					imagesPath = selectedFile.getParent() + "\\" + selectedFile.getName().replaceFirst("[.][^.]+$", "")
							+ "\\" + selectedFile.getName().replaceFirst("[.][^.]+$", "") + "\\android";
					imagesZipTF.setText(imagesPath);
				} else {
					Dialogs.alert("Unzip failed");
				}
			}
			break;
		}
		case "copyImages": {
			projectDirPath = projectTF.getText().toString();
			imagesPath = imagesZipTF.getText().toString();
			if (projectDirPath == null || projectDirPath.equalsIgnoreCase("")) {
				Dialogs.alert("Please choose Project directory");
				return;
			}
			if (imagesPath == null || imagesPath.equalsIgnoreCase("")) {
				Dialogs.alert("Please choose Images directory");
				return;
			}
			ImageTransfer imageTransfer = new ImageTransfer();
			if (imageTransfer.transferImages(projectDirPath, imagesPath)) {
				Dialogs.alert("Images copied to project folder successfully");
			}
			break;
		}
		case "samplePaths": {
			Dialogs.alert("Project Path:\n\nD:\\Workspace\\<YOUR_PROJECT_NAME>\n\n"
					+ "Images Path:\n\nD:\\Downloads\\ic_backup_black_24dp.zip");
			break;
		}
		default: {
			break;
		}
		}
	}
}
