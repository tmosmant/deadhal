package fr.upem.deadhal.save;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.app.Activity;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;

public class SaveTask extends AsyncTask<Level, Integer, Integer> {

	private Activity activity;

	private String m_fileName;
	private File m_directory = new File(
			Environment.getExternalStorageDirectory() + File.separator
					+ "deadhal");
	private static final String FTYPE = ".xml";

	private String m_error;

	public SaveTask(Activity activity, String fileName) {
		this.activity = activity;
		m_fileName = fileName;
	}

	@Override
	protected void onPreExecute() {
		if (!m_directory.exists()) {
			m_directory.mkdirs();
		}
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(Level... params) {
		Level m_level = params[0];
		Map<UUID, Room> rooms = m_level.getRooms();

		try {
			File m_file = new File(m_directory.getAbsolutePath()
					+ File.separator + m_fileName + FTYPE);
			m_file.createNewFile();

			FileOutputStream outputStream = new FileOutputStream(m_file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					outputStream);

			outputStreamWriter
					.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			outputStreamWriter.append("<level name=\"" + m_level.getTitle() + "\">");
			for (Entry<UUID, Room> entry : rooms.entrySet()) {
				Room room = entry.getValue();
				RectF rect = room.getRect();
				outputStreamWriter.append("<room id=\"" + room.getId()
						+ "\" name=\"" + room.getTitle() + "\" left=\""
						+ rect.left + " right=\"" + rect.right + "\" top=\""
						+ rect.top + "\" bottom=\"" + rect.bottom + "\" />");
			}
			outputStreamWriter.append("</level>");

			outputStreamWriter.close();
			outputStream.close();
		} catch (Exception e) {
			m_error = e.getMessage();
		}
		return 1;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
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
