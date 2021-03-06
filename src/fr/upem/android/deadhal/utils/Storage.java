package fr.upem.android.deadhal.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Provides static storage methods.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class Storage {

	private static final String FILE_EXTENSION = ".dh";

	/**
	 * Checks if external storage is available to at least read.
	 * 
	 * @return true if external storage is available to at least read, false
	 *         otherwise
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	/**
	 * Checks if external storage is available for read and write.
	 * 
	 * @return true if external storage is available for read and write, false
	 *         otherwise
	 */
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

	/**
	 * Returns the number of files that can be opened.
	 * 
	 * @return the number of files that can be opened
	 */
	public static int getNbFiles() {
		File[] files;
		File directory = getDeadHalDir();
		if (directory.isDirectory()) {
			files = directory.listFiles(filter());
			return files.length;
		}
		return 0;
	}

	/**
	 * List all the files in the deadhal directory.
	 * 
	 * @return all the files in the deadhal directory
	 */
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

	/**
	 * Returns the existence of a file.
	 * 
	 * @param name
	 *            the file name
	 * @return true if it exists, false otherwise
	 */
	public static boolean fileExists(String name) {
		return new File(getDeadHalDir().getAbsolutePath() + File.separator
				+ name + FILE_EXTENSION).exists();
	}

	/**
	 * Returns a file in deadhal directory.
	 * 
	 * @param name
	 *            the file name
	 * @return a file
	 */
	public static File openFile(String name) {
		return new File(getDeadHalDir().getAbsolutePath() + File.separator
				+ name + FILE_EXTENSION);
	}

	/**
	 * Create a new file.
	 * 
	 * @param name
	 *            the file name
	 * @return the new file
	 */
	public static File createFile(String name) {
		if (isExternalStorageWritable()) {
			File file = new File(getDeadHalDir().getAbsolutePath()
					+ File.separator + name + FILE_EXTENSION);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
		}
		return null;
	}

	/**
	 * Rename a file.
	 * 
	 * @param from
	 *            the file to rename
	 * @param newName
	 *            the new name
	 * @return true if renamed, false otherwise
	 */
	public static boolean renameFile(File from, String newName) {
		File to = openFile(newName);
		return from.renameTo(to);
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

	/**
	 * Copy a file to deadhal directory.
	 * 
	 * @param src
	 *            the file source to copy
	 * @return true if copied, false otherwise
	 * @throws IOException
	 */
	public static boolean copyFile(File src) throws IOException {
		if (src.getParent().equals(getDeadHalDir().getAbsolutePath())) {
			return true;
		} else {
			File newFile;
			if (fileExists(getFileNameWithoutExtension(src))) {
				newFile = createFile(getFileNameWithoutExtension(src)
						+ System.currentTimeMillis());
			} else {
				newFile = createFile(getFileNameWithoutExtension(src));
			}

			InputStream is = new FileInputStream(src);
			OutputStream os = new FileOutputStream(newFile);
			byte[] buff = new byte[1024];
			int len;
			while ((len = is.read(buff)) > 0) {
				os.write(buff, 0, len);
			}
			is.close();
			os.close();
		}
		return true;
	}

	/**
	 * 
	 * Returns the file name without extension.
	 * @param file
	 *            the file to extract name
	 * @return the file name without extension
	 */
	public static String getFileNameWithoutExtension(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
			name = name.substring(0, pos);
		}
		return name;
	}
}
