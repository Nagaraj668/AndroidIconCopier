package com.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageTransfer {

	public boolean transferImages(String target, String source) {
		try {
			File targetFolder = new File(target);
			File[] files = targetFolder.listFiles();
			ArrayList<String> targetFolders = new ArrayList<>();
			for (File name : files) {
				if (name.getName().contains("mipmap")) {
					System.out.println("hey");
					System.out.println(name.getAbsolutePath());
					targetFolders.add(name.getAbsolutePath() + "\\");
				}
			}

			File sourceFolder = new File(source);
			System.out.println(sourceFolder);
			List<String> sourceFolders = Arrays.asList(sourceFolder.list());

			List<String> filteredsourceFolders = new ArrayList<>();
			for (String s : sourceFolders) {
				if (s.contains("ldrtl")) {
					continue;
				}
				filteredsourceFolders.add(s);
			}

			for (int i = 0; i < filteredsourceFolders.size(); i++) {
				System.out.println(filteredsourceFolders.get(i));
				File[] Files = new File(source.concat("\\").concat(filteredsourceFolders.get(i))).listFiles();

				for (File file : Files) {
					System.out.println(file.getName());
				}

				for (File file : Files) {
					moveFile(file, new File(targetFolders.get(i).concat(file.getName())));
				}
			}
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			Dialogs.alert("Error Occurred");
			return false;
		}
	}

	public static void moveFile(File s, File d) {

		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			File afile = s;
			File bfile = d;

			System.out.println(afile.getAbsolutePath());
			System.out.println(bfile.getAbsolutePath());

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();

			System.out.println("File is copied successful!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
