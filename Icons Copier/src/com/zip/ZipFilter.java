package com.zip;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ZipFilter extends FileFilter {
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String extension = Utils.getExtension(f);
		if (extension != null) {
			if (extension.equals(Utils.zip)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return null;
	}
}
