package fr.upem.android.deadhal.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class Storage {

	private static final String FILE_EXTENSION = ".dh";

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/* Create a filter on files names */
	private static FilenameFilter filter() {
		return new FilenameFilter() {

			@SuppressLint("DefaultLocale")
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(FILE_EXTENSION);
			}
		};
	}

	/* Return the number of file to open */
	public static int getNbFiles() {
		File[] files;
		File directory = getDeadHalDir();
		if (directory.isDirectory()) {
			files = directory.listFiles(filter());
			return files.length;
		}
		return 0;
	}

	/* List all the files in the deadhal directory */
	// public static List<String> getFilesList() {
	// File directory = getDeadHalDir();
	// List<String> list = new ArrayList<String>();
	//
	// if (directory.isDirectory()) {
	// File files[] = directory.listFiles(filter());
	// for (File file : files) {
	// String name = file.getName();
	// name = name.replace(FILE_EXTENSION, "");
	// list.add(name);
	// }
	// Collections.sort(list);
	// }
	// return list;
	// }

	/* List all the files in the deadhal directory */
	public static ArrayList<File> getFilesList() {
		ArrayList<File> fileList = null;
		File directory = getDeadHalDir();

		if (directory.isDirectory()) {
			File files[] = directory.listFiles(filter());
			fileList = new ArrayList<File>(Arrays.asList(files));
			Collections.sort(fileList);
		}
		return fileList;
	}

	/* Return true if the file exists */
	public static boolean fileExists(String name) {
		return new File(getDeadHalDir().getAbsolutePath() + File.separator + name + FILE_EXTENSION).exists();
	}

	/* Return a file */
	public static File openFile(String name) {
		return new File(getDeadHalDir().getAbsolutePath() + File.separator + name + FILE_EXTENSION);
	}

	/* Return a new file */
	public static File createFile(String name) {
		if (isExternalStorageWritable()) {
			File file = new File(getDeadHalDir().getAbsolutePath() + File.separator + name + FILE_EXTENSION);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
		}
		return null;
	}

	/* Rename a file */
	public static boolean renameFile(File from, String newName) {
		File to = openFile(newName);
		return from.renameTo(to);
	}

	/* Return the deadhal directory (create it if doesn't exists) */
	private static File getDeadHalDir() {
		File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Deadhal");
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				Log.e("dir", "Directory not created");
			}
		}
		return directory;
	}
}
