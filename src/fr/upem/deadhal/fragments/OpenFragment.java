package fr.upem.deadhal.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

	private OnDataPass m_callback;

	private Level m_level;
	private String m_fileName = null;
	private ArrayAdapter<String> m_arrayAdapter = null;
	private List<String> m_list = new ArrayList<String>();
	private ListView m_listView = null;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		boolean isActive = m_fileName != null;
		menu.findItem(R.id.action_share).setVisible(isActive);
		menu.findItem(R.id.action_accept).setVisible(isActive);
		menu.findItem(R.id.action_remove).setVisible(isActive);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_open, container,
				false);

		getActivity().setTitle(R.string.open);

		if (!Storage.isExternalStorageReadable()) {
			Toast.makeText(getActivity(),
					"Erreur: impossible d'accéder a la mémoire externe",
					Toast.LENGTH_SHORT).show();
		}

		else {
			m_list = Storage.getFilesList();
			m_listView = (ListView) rootView.findViewById(R.id.listFile);
			m_arrayAdapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_activated_1, m_list);
			m_listView.setAdapter(m_arrayAdapter);
			m_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			m_listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					m_fileName = (String) parent.getItemAtPosition(position);
					getActivity().invalidateOptionsMenu();
				}

			});
		}

		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			return open();
		case R.id.action_remove:
			delete();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean open() {
		File m_file = Storage.openFile(m_fileName);
		if (m_file == null) {
			return false;
		}

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
		return true;
	}

	private void delete() {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Supprimer ?")
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								File m_file = Storage.openFile(m_fileName);
								if (m_file.delete()) {
									m_callback.nbFilePass();
									m_listView.clearChoices();
									m_arrayAdapter.remove(m_fileName);
									m_fileName = null;
									getActivity().invalidateOptionsMenu();
									Toast.makeText(getActivity(),
											"Fichier supprimé",
											Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		dialog = builder.create();
		dialog.show();
	}

}
