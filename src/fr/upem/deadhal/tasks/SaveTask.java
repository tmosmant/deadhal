package fr.upem.deadhal.tasks;

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
import android.util.Xml;
import android.widget.Toast;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;

public class SaveTask extends AsyncTask<Level, Integer, Integer> {

	private Activity activity;

	private String m_error;
	private File m_file = null;

	public SaveTask(Activity activity, File file) {
		this.activity = activity;
		m_file = file;
	}

	@Override
	protected Integer doInBackground(Level... params) {
		Level m_level = params[0];

		FileOutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
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
		Map<UUID, Corridor> corridors = level.getCorridors();

		XmlSerializer xmlSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		xmlSerializer.setOutput(writer);

		xmlSerializer.startDocument("UTF-8", null);
		xmlSerializer.setFeature(
				"http://xmlpull.org/v1/doc/features.html#indent-output", true);

		xmlSerializer.startTag("", "level");
		xmlSerializer.attribute("", "title", level.getTitle());
		for (Entry<UUID, Room> entry : rooms.entrySet()) {
			Room room = entry.getValue();
			RectF rect = room.getRect();

			xmlSerializer.startTag("", "room");
			xmlSerializer.attribute("", "id", room.getId().toString());
			xmlSerializer.attribute("", "title", room.getTitle());
			xmlSerializer.attribute("", "left", String.valueOf(rect.left));
			xmlSerializer.attribute("", "right", String.valueOf(rect.right));
			xmlSerializer.attribute("", "top", String.valueOf(rect.top));
			xmlSerializer.attribute("", "bottom", String.valueOf(rect.bottom));
			xmlSerializer.endTag("", "room");
		}
		for (Entry<UUID, Corridor> entry : corridors.entrySet()) {
			Corridor corridor = entry.getValue();

			xmlSerializer.startTag("", "corridor");
			xmlSerializer.attribute("", "src", corridor.getSrc().toString());
			xmlSerializer.attribute("", "dst", corridor.getDst().toString());
			xmlSerializer.attribute("", "directed",
					String.valueOf(corridor.isDirected()));
			xmlSerializer.endTag("", "corridor");
		}
		xmlSerializer.endTag("", "level");
		xmlSerializer.endDocument();

		return writer.toString();
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (!m_file.isHidden()) {
			if (result == 1) {
				Toast.makeText(activity, "Sauvegarde effectuée",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity, m_error, Toast.LENGTH_LONG).show();
			}
		}
		super.onPostExecute(result);
	}

}
