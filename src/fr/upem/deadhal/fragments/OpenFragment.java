package fr.upem.deadhal.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.tasks.OpenTask;
import fr.upem.deadhal.utils.OnDataPass;
import fr.upem.deadhal.utils.Storage;

public class OpenFragment extends Fragment {

	private Level m_level;

	private OnDataPass m_callback;

	private List<String> m_list = new ArrayList<String>();

	public OpenFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			m_callback = (OnDataPass) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDataPass");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_open, container,
				false);

		getActivity().setTitle(R.string.open);

		if (!Storage.isExternalStorageReadable()) {
			Toast.makeText(getActivity(),
					"Erreur: impossible d'acc�der a la m�moire externe",
					Toast.LENGTH_SHORT).show();
		}

		else {
			m_list = Storage.getFilesList();
			ListView listView = (ListView) rootView.findViewById(R.id.listFile);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					getActivity(),
					android.R.layout.simple_list_item_activated_1, m_list);
			listView.setAdapter(arrayAdapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.setOnItemClickListener(openOnItemClickListener());
		}

		return rootView;
	}

	private OnItemClickListener openOnItemClickListener() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String fileName = (String) parent.getItemAtPosition(position);
				File m_file = Storage.openFile(fileName);

				OpenTask openTask = new OpenTask();
				openTask.execute(m_file);

				try {
					m_level = openTask.get();
					m_callback.onLevelPass(m_level);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}

		};
	}

}
