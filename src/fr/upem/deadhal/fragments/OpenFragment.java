package fr.upem.deadhal.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.deadhal.fragments.dialogs.ConfirmDialogFragment;
import fr.upem.deadhal.fragments.dialogs.InputDialogFragment;
import fr.upem.deadhal.tasks.OpenTask;
import fr.upem.deadhal.utils.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OpenFragment extends Fragment {

	private DrawerMainListener m_callback;
	public static final int RENAME_DIALOG = 1;
	public static final int REMOVE_DIALOG = 2;

	private int m_selection = -1;
	private String m_fileName = null;
	private ArrayAdapter<String> m_arrayAdapter = null;
	private List<String> m_list = new ArrayList<>();
	private ListView m_listView = null;

	public OpenFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			m_callback = (DrawerMainListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnDataPass");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			m_fileName = savedInstanceState.getString("file");
			m_selection = savedInstanceState.getInt("selection");
		}
		setHasOptionsMenu(true);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		boolean isActive = m_fileName != null;
		menu.setGroupVisible(R.id.group_open, isActive);
		menu.findItem(R.id.action_share).setVisible(isActive);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		File file = Storage.openFile(m_fileName);
		if (file.exists() && file.canRead()) {
			ShareActionProvider shareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share)
					.getActionProvider();

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			intent.putExtra(Intent.EXTRA_SUBJECT, "Share " + file.getName());
			shareActionProvider.setShareIntent(intent);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_open, container, false);

		getActivity().setTitle(R.string.open);

		if (Storage.isExternalStorageReadable()) {
			m_list = Storage.getFilesList();
			m_listView = (ListView) rootView.findViewById(R.id.listFile);
			m_arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, m_list);
			m_listView.setAdapter(m_arrayAdapter);
			m_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			m_listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (m_selection != position) {
						m_selection = position;
						m_fileName = (String) parent.getItemAtPosition(position);
					} else {
						m_selection = -1;
						m_fileName = null;
						m_listView.clearChoices();
						m_listView.bringToFront();
					}
					getActivity().invalidateOptionsMenu();
				}
			});
		} else {
			Toast.makeText(getActivity(), "Erreur: impossible d'acc�der a la m�moire externe", Toast.LENGTH_SHORT)
					.show();
		}

		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_navigation:
				return open(FragmentType.NAVIGATION);
			case R.id.action_edition:
				return open(FragmentType.EDITION);
			case R.id.action_rename:
				showRenameDialog();
				return true;
			case R.id.action_remove:
				showRemoveDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private boolean open(FragmentType dest) {
		File m_file = Storage.openFile(m_fileName);
		if (m_file == null) {
			return false;
		}

		OpenTask openTask = new OpenTask();
		openTask.execute(m_file);

		try {
			Level m_level = openTask.get();
			SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
			SharedPreferences.Editor ed = preferences.edit();
			ed.clear();
			ed.commit();

			m_fileName = null;
			m_listView.clearChoices();
			m_callback.onLevelChange(m_level);
			m_callback.onFragmentChange(dest);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void showRenameDialog() {
		int title = R.string.action_rename;

		DialogFragment dialogFragment = InputDialogFragment.newInstance(title);
		dialogFragment.setTargetFragment(this, RENAME_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(), "renameDialog");
	}

	private void showRemoveDialog() {
		int title = R.string.action_remove;
		int message = R.string.remove_warning;

		DialogFragment dialogFragment = ConfirmDialogFragment.newInstance(title, message);
		dialogFragment.setTargetFragment(this, REMOVE_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(), "removeDialog");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case RENAME_DIALOG:
				if (resultCode == Activity.RESULT_OK) {
					String fileName = data.getStringExtra("inputText");
					rename(fileName);
				}
				break;
			case REMOVE_DIALOG:
				if (resultCode == Activity.RESULT_OK) {
					delete();
				}
				break;
		}
	}

	private void rename(String fileName) {
		Storage.renameFile(m_fileName, fileName);
		m_arrayAdapter.remove(m_fileName);
		m_arrayAdapter.add(fileName);
		m_listView.clearChoices();

		m_fileName = null;
		m_list = Storage.getFilesList();
	}

	private void delete() {
		File m_file = Storage.openFile(m_fileName);
		if (m_file.delete()) {
			m_callback.onFileNumberChange();
			m_arrayAdapter.remove(m_fileName);

			clearChoice();
			Toast.makeText(getActivity(), R.string.deleted_file, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), R.string.deleted_file_error, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		clearChoice();
	}

	public void clearChoice() {
		m_fileName = null;
		m_listView.clearChoices();
		getActivity().invalidateOptionsMenu();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("file", m_fileName);
		outState.putInt("selection", m_selection);
	}
}