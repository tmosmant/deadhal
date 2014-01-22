package fr.upem.deadhal.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.tasks.OpenTask;
import fr.upem.deadhal.utils.OnDataPass;
import fr.upem.deadhal.utils.Storage;

public class OpenFragment extends Fragment {

	private OnDataPass m_callback;
	public static final int DIALOG_FRAGMENT = 1;

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
		if (savedInstanceState != null) {
			m_fileName = savedInstanceState.getString("file");
		}
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		File file = Storage.openFile(m_fileName);
		if (file.exists() && file.canRead()) {
			ShareActionProvider mShareActionProvider = (ShareActionProvider) menu
					.findItem(R.id.action_share).getActionProvider();

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/xml");
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.parse("file://" + file.getAbsolutePath()));
			intent.putExtra(Intent.EXTRA_SUBJECT, "Share " + file.getName());
			mShareActionProvider.setShareIntent(intent);
		}
		super.onCreateOptionsMenu(menu, inflater);
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
			showDialog();
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

	private void showDialog() {
		int title = R.string.action_remove;
		int message = R.string.remove_warning;

		DialogFragment dialogFragment = CustomDialogFragment.newInstance(title,
				message);
		dialogFragment.setTargetFragment(this, DIALOG_FRAGMENT);
		dialogFragment.show(getFragmentManager().beginTransaction(), "dialog");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DIALOG_FRAGMENT:
			if (resultCode == Activity.RESULT_OK) {
				delete();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(getActivity(), "Supression annulée",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void delete() {
		File m_file = Storage.openFile(m_fileName);
		if (m_file.delete()) {
			m_callback.nbFilePass();
			m_listView.clearChoices();
			m_arrayAdapter.remove(m_fileName);
			m_fileName = null;
			getActivity().invalidateOptionsMenu();
			Toast.makeText(getActivity(), "Fichier supprimé",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("file", m_fileName);
	}

}