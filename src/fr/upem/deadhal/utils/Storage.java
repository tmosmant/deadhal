package fr.upem.deadhal.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class Storage {

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Return the number of file to open */
	public static int getNbFiles() {
		File[] files = null;
		File directory = getDeadHalDir();
		if (directory.isDirectory()) {
			files = directory.listFiles(new FilenameFilter() {
				@SuppressLint("DefaultLocale")
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".xml");
				}
			});
			return files.length;
		}
		return 0;
	}

	/* List all the files in the deadhal directory */
	public static List<String> getFilesList() {
		File directory = getDeadHalDir();
		List<String> list = new ArrayList<String>();

		if (directory.isDirectory()) {
			File files[] = directory.listFiles(new FilenameFilter() {
				@SuppressLint("DefaultLocale")
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".xml");
				}
			});
			for (int i = 0; i < files.length; i++) {
				list.add(files[i].getName());
			}
			Collections.sort(list);
		}
		return list;
	}

	public static boolean fileExists(String name) {
		return new File(getDeadHalDir().getAbsolutePath() + File.separator
				+ name + ".xml").exists();
	}

	/* Return a file */
	public static File openFile(String name) {
		return new File(getDeadHalDir().getAbsolutePath() + File.separator
				+ name);
	}

	/* Return a new file */
	public static File createFile(String name) {
		File file = new File(getDeadHalDir().getAbsolutePath() + File.separator
				+ name + ".xml");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/* Return the deadhal directory (create it if doesn't exists) */
	private static File getDeadHalDir() {
		File directory = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Deadhal");
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				Log.e("dir", "Directory not created");
			}
		}
		return directory;
	}

}
