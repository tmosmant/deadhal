package fr.upem.deadhal.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.RectF;
import android.os.AsyncTask;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;

public class OpenTask extends AsyncTask<File, Integer, Level> {

	private Level m_level = new Level();

	@Override
	protected Level doInBackground(File... params) {
		File m_file = params[0];
		String m_content = getContent(m_file);

		if (m_content == null) {
			return null;
		}

		try {
			parseContent(m_content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return m_level;
	}

	private String getContent(File file) {
		FileInputStream inputStream = null;
		InputStreamReader inputStreamReader = null;

		try {
			inputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(inputStream);

			int read = 0;
			byte[] buffer = new byte[1024];
			StringBuilder stringBuilder = new StringBuilder();
			while ((read = inputStream.read(buffer)) != -1) {
				stringBuilder.append(new String(buffer, 0, read));
			}

			return stringBuilder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private void parseContent(String content) throws Exception {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(content));

		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equals("level")) {
					levelTag(xpp);
				}

				else if (xpp.getName().equals("room")) {
					roomTag(xpp);
				}
			}
			eventType = xpp.next();
		}
	}

	private void levelTag(XmlPullParser xpp) {
		String title = xpp.getAttributeValue(0);
		m_level.setTitle(title);
	}

	private void roomTag(XmlPullParser xpp) {
		UUID id = UUID.fromString(xpp.getAttributeValue(0));
		String title = xpp.getAttributeValue(1);
		float left = Float.valueOf(xpp.getAttributeValue(2));
		float right = Float.valueOf(xpp.getAttributeValue(3));
		float top = Float.valueOf(xpp.getAttributeValue(4));
		float bottom = Float.valueOf(xpp.getAttributeValue(5));
		RectF rect = new RectF(left, top, right, bottom);

		Room room = new Room(id, title, rect);
		m_level.addRoom(room);
	}

}