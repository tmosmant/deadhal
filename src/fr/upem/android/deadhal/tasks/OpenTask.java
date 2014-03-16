package fr.upem.android.deadhal.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;

/**
 * This class is in charge of the construction of a level by reading it in a
 * file.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
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

			int read;
			byte[] buffer = new byte[1024];
			StringBuilder stringBuilder = new StringBuilder();
			while ((read = inputStream.read(buffer)) != -1) {
				stringBuilder.append(new String(buffer, 0, read));
			}

			return stringBuilder.toString();
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
				} else if (xpp.getName().equals("room")) {
					roomTag(xpp);
				} else if (xpp.getName().equals("corridor")) {
					corridorTag(xpp);
				}
			}
			eventType = xpp.next();
		}
	}

	private void levelTag(XmlPullParser xpp) {
		String title = xpp.getAttributeValue(null, "title");
		m_level.setTitle(title);
	}

	private void roomTag(XmlPullParser xpp) {
		UUID id = UUID.fromString(xpp.getAttributeValue(null, "id"));
		String name = xpp.getAttributeValue(null, "name");
		float left = Float.valueOf(xpp.getAttributeValue(null, "left"));
		float right = Float.valueOf(xpp.getAttributeValue(null, "right"));
		float top = Float.valueOf(xpp.getAttributeValue(null, "top"));
		float bottom = Float.valueOf(xpp.getAttributeValue(null, "bottom"));
		RectF rect = new RectF(left, top, right, bottom);

		Room room = new Room(id, name, rect);
		m_level.addRoom(room);
	}

	private void corridorTag(XmlPullParser xpp) {
		UUID corridorId = UUID.fromString(xpp.getAttributeValue(null, "id"));
		UUID src = UUID.fromString(xpp.getAttributeValue(null, "src"));
		UUID dst = UUID.fromString(xpp.getAttributeValue(null, "dst"));
		boolean directed = Boolean.valueOf(xpp.getAttributeValue(null,
				"directed"));
		String[] split = xpp.getAttributeValue(null, "srcPoint").split(",");
		PointF srcPoint = new PointF(Float.valueOf(split[0]),
				Float.valueOf(split[1]));
		split = xpp.getAttributeValue(null, "dstPoint").split(",");
		PointF dstPoint = new PointF(Float.valueOf(split[0]),
				Float.valueOf(split[1]));
		Corridor corridor = new Corridor(corridorId, src, dst, directed,
				srcPoint, dstPoint);
		m_level.addCorridor(corridor);
	}
}
