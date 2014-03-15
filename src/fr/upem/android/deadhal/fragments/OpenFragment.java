package fr.upem.android.deadhal.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.android.deadhal.fragments.adapter.FileAdapter;
import fr.upem.android.deadhal.fragments.dialogs.ConfirmDialogFragment;
import fr.upem.android.deadhal.fragments.dialogs.InputDialogFragment;
import fr.upem.android.deadhal.fragments.dialogs.OptionsDialogFragment;
import fr.upem.android.deadhal.tasks.OpenTask;
import fr.upem.android.deadhal.utils.Storage;
import fr.upem.deadhal.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class OpenFragment extends Fragment implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

	private static final int OPTIONS_DIALOG = 1;
	private static final int RENAME_DIALOG = 2;
	private static final int REMOVE_DIALOG = 3;

	private File m_selectedFile;

	private DrawerMainListener m_callback;
	private FileAdapter m_filesAdapter;

	// private ActionMode m_actionMode;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			m_callback = (DrawerMainListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDataPass");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<File> m_files = Storage.getFilesList();
		m_filesAdapter = new FileAdapter(getActivity(), R.layout.list_file,
				m_files);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragmen_open_best, container,
				false);

		getActivity().setTitle(R.string.open);

		ListView m_listView = (ListView) rootView.findViewById(R.id.list_file);
		m_listView.setAdapter(m_filesAdapter);
		m_listView.setOnItemClickListener(this);
		m_listView.setOnItemLongClickListener(this);

		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// onClick(position);
		m_selectedFile = m_filesAdapter.getItem(position);
		open();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		m_selectedFile = m_filesAdapter.getItem(position);
		showOptionsDialog(m_selectedFile.getName());
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case OPTIONS_DIALOG:
			if (resultCode == Activity.RESULT_OK) {
				int option = data.getIntExtra("option", -1);
				switch (option) {
				case 0:
					showRenameDialog();
					break;
				case 1:
					share();
					break;
				case 2:
					showRemoveDialog();
					break;
				}
			}
			break;
		case RENAME_DIALOG:
			if (resultCode == Activity.RESULT_OK) {
				String fileName = data.getStringExtra("inputText");
				rename(fileName);
			}
			break;
		case REMOVE_DIALOG:
			if (resultCode == Activity.RESULT_OK) {
				delete(m_selectedFile);
			}
			break;
		}
	}

	private void showOptionsDialog(String title) {
		DialogFragment dialogFragment = OptionsDialogFragment
				.newInstance(title);
		dialogFragment.setTargetFragment(this, OPTIONS_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(),
				"renameDialog");
	}

	private boolean open() {
		OpenTask openTask = new OpenTask();
		openTask.execute(m_selectedFile);

		try {
			Level m_level = openTask.get();
			SharedPreferences preferences = getActivity().getSharedPreferences(
					"pref", Context.MODE_PRIVATE);
			SharedPreferences.Editor ed = preferences.edit();
			ed.clear();
			ed.commit();

			m_callback.onLevelChange(m_level);
			m_callback.onFragmentChange(FragmentType.NAVIGATION);
		} catch (InterruptedException e) {
			return false;
		} catch (ExecutionException e) {
			return false;
		}
		return true;
	}

	private void showRenameDialog() {
		String title = getActivity().getString(R.string.action_rename);

		DialogFragment dialogFragment = InputDialogFragment.newInstance(title,
				null);
		dialogFragment.setTargetFragment(this, RENAME_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(),
				"renameDialog");
	}

	private void rename(String fileName) {
		m_filesAdapter.remove(m_selectedFile);
		Storage.renameFile(m_selectedFile, fileName);
		m_filesAdapter.add(Storage.openFile(fileName));
		m_filesAdapter.sort(new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
	}

	private void showRemoveDialog() {
		int title = R.string.action_remove;
		int message = R.string.remove_warning;

		DialogFragment dialogFragment = ConfirmDialogFragment.newInstance(
				title, message);
		dialogFragment.setTargetFragment(this, REMOVE_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(),
				"removeDialog");
	}

	private void delete(File file) {
		if (file.delete()) {
			m_callback.onFileNumberChange();
			m_filesAdapter.remove(m_selectedFile);

			Toast.makeText(getActivity(), R.string.deleted_file,
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), R.string.deleted_file_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void share() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(m_selectedFile));
		intent.putExtra(Intent.EXTRA_SUBJECT,
				"Share " + m_selectedFile.getName());
		startActivity(intent);
	}

}
