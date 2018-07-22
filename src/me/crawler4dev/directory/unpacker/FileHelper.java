package me.crawler4dev.directory.unpacker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileHelper {

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
