package fr.upem.deadhal.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.open.OpenTask;

public class OpenFragment extends Fragment {

	private File m_selectedFile = null;

	private File m_directory = new File(
			Environment.getExternalStorageDirectory() + File.separator
					+ "deadhal");

	public OpenFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_open, container,
				false);

		getActivity().setTitle(R.string.open);

		if (m_directory.exists() && m_directory.isDirectory()) {
			File files[] = m_directory.listFiles();
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < files.length; i++) {
				list.add(files[i].getName());
			}
			Collections.sort(list);

			ListView listView = (ListView) rootView.findViewById(R.id.listFile);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1, list);
			listView.setAdapter(arrayAdapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.setSelector(android.R.color.holo_blue_dark);
			listView.setOnItemClickListener(openOnItemClickListener());
		}

		return rootView;
	}

	private OnItemClickListener openOnItemClickListener() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String m_fileName = (String) parent.getItemAtPosition(position);
				m_selectedFile = new File(m_directory.getAbsolutePath()
						+ File.separator + m_fileName);		
				
				OpenTask openTask = new OpenTask();
				openTask.execute(m_selectedFile);
			}

		};
	}

}
