package fr.upem.deadhal.save;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;

public class SaveTask extends AsyncTask<Level, Integer, Integer> {

	private Activity activity;

	private String m_fileName;
	private File m_directory = null;
	private static final String FTYPE = ".xml";

	private String m_error;

	public SaveTask(Activity activity, String fileName) {
		this.activity = activity;
		m_fileName = fileName;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (!isExternalStorageWritable()) {
			cancel(true);
		}

		m_directory = getDeadHalDir();
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public File getDeadHalDir() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "deadhal");
		if (!file.mkdirs()) {
			Log.e("dir", "Directory not created");
		}
		return file;
	}

	@Override
	protected Integer doInBackground(Level... params) {
		Level m_level = params[0];

		File m_file = null;
		FileOutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
			m_file = new File(m_directory.getAbsolutePath() + File.separator
					+ m_fileName + FTYPE);
			m_file.createNewFile();

			outputStream = new FileOutputStream(m_file);
			outputStreamWriter = new OutputStreamWriter(outputStream);
			outputStreamWriter.append(CreateXMLString(m_level));
		} catch (Exception e) {
			m_error = e.getMessage();
			return -1;
		} finally {
			if (outputStreamWriter != null) {
				try {
					outputStreamWriter.close();
				} catch (IOException e) {
					m_error = e.getMessage();
					return -1;
				}
			}

			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					m_error = e.getMessage();
					return -1;
				}
			}
		}

		return 1;
	}

	public static String CreateXMLString(Level level)
			throws IllegalArgumentException, IllegalStateException, IOException {
		Map<UUID, Room> rooms = level.getRooms();
		XmlSerializer xmlSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		xmlSerializer.setOutput(writer);

		xmlSerializer.startDocument("UTF-8", null);
		xmlSerializer.setFeature(
				"http://xmlpull.org/v1/doc/features.html#indent-output", true);

		xmlSerializer.startTag("", "level");
		xmlSerializer.attribute("", "name", level.getTitle());
		for (Entry<UUID, Room> entry : rooms.entrySet()) {
			Room room = entry.getValue();
			RectF rect = room.getRect();

			xmlSerializer.startTag("", "room");
			xmlSerializer.attribute("", "id", room.getId().toString());
			xmlSerializer.attribute("", "name", room.getTitle());
			xmlSerializer.attribute("", "left", String.valueOf(rect.left));
			xmlSerializer.attribute("", "right", String.valueOf(rect.right));
			xmlSerializer.attribute("", "top", String.valueOf(rect.top));
			xmlSerializer.attribute("", "bottom", String.valueOf(rect.bottom));
			xmlSerializer.endTag("", "room");
		}
		xmlSerializer.endTag("", "level");
		xmlSerializer.endDocument();

		return writer.toString();
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (result == 1) {
			Toast.makeText(activity, "Sauvegarde effectuée", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(activity, m_error, Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}

}
