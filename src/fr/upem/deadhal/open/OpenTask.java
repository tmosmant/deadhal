package fr.upem.deadhal.open;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;

public class OpenTask extends AsyncTask<File, Integer, Integer> {

	@Override
	protected Integer doInBackground(File... params) {
		File m_file = params[0];
		String m_content = getContent(m_file);

		if (m_content == null) {
			return -1;
		}

		try {
			parseContent(m_content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 1;
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
			if (eventType == XmlPullParser.START_DOCUMENT) {
				System.out.println("Start document");
			} else if (eventType == XmlPullParser.START_TAG) {
				System.out.println("Start tag " + xpp.getName());
			} else if (eventType == XmlPullParser.END_TAG) {
				System.out.println("End tag " + xpp.getName());
			} else if (eventType == XmlPullParser.TEXT) {
				System.out.println("Text " + xpp.getText());
			}
			eventType = xpp.next();
		}
		System.out.println("End document");
	}

}
