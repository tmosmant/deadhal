package fr.upem.deadhal.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.deadhal.fragments.adapter.FileAdapter;
import fr.upem.deadhal.fragments.dialogs.ConfirmDialogFragment;
import fr.upem.deadhal.fragments.dialogs.InputDialogFragment;
import fr.upem.deadhal.tasks.OpenTask;
import fr.upem.deadhal.utils.Storage;

public class OpenFragment extends Fragment implements
		AdapterView.OnItemClickListener {

	private static final int RENAME_DIALOG = 1;
	private static final int REMOVE_DIALOG = 2;

	private File m_selectedFile;

	private DrawerMainListener m_callback;
	private FileAdapter m_filesAdapter;
	private ActionMode m_actionMode;

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
		ArrayList<File> files = Storage.getFilesList();
		m_filesAdapter = new FileAdapter(getActivity(), R.layout.list_file,
				files);
		if (savedInstanceState != null) {
			int position = savedInstanceState.getInt("position");
			if (position != -1) {
				onClick(position);
			}
		}
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

		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		onClick(position);
	}

	private void onClick(int position) {
		m_filesAdapter.toggleSelection(position);
		m_selectedFile = m_filesAdapter.getItem(position);
		boolean hasCheckedItem = m_filesAdapter.hasCheckedItem();

		if (hasCheckedItem && m_actionMode == null) {
			// there are some selected items, start the actionMode
			m_actionMode = getActivity().startActionMode(
					new ActionModeCallback());
		} else if (!hasCheckedItem && m_actionMode != null) {
			// there no selected items, finish the actionMode
			m_actionMode.finish();
		}

		if (m_actionMode != null) {
			m_actionMode.setTitle(m_selectedFile.getName());

			int doneButtonId = Resources.getSystem().getIdentifier(
					"action_mode_close_button", "id", "android");
			LinearLayout layout = (LinearLayout) getActivity().findViewById(
					doneButtonId);
			layout.setOnClickListener(buildDoneClickListener(m_selectedFile));
		}
	}

	private View.OnClickListener buildDoneClickListener(final File file) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				m_actionMode.finish();
				open(file);
			}
		};
	}

	private boolean open(File file) {
		OpenTask openTask = new OpenTask();
		openTask.execute(file);

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
		int title = R.string.action_rename;

		DialogFragment dialogFragment = InputDialogFragment.newInstance(title);
		dialogFragment.setTargetFragment(this, RENAME_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(),
				"renameDialog");
	}

	private void rename(String fileName) {
		m_actionMode.finish();
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
			m_actionMode.finish();
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
				delete(m_selectedFile);
			}
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (m_actionMode != null) {
			m_actionMode.finish();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("position", m_filesAdapter.getSelectedId());
	}

	private class ActionModeCallback implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// inflate contextual menu
			mode.getMenuInflater().inflate(R.menu.open, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				showRemoveDialog();
				return true;

			case R.id.action_share:
				share();
				m_filesAdapter.removeSelection();
				return true;

			case R.id.action_rename:
				showRenameDialog();
				return true;
			default:
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			m_actionMode = null;
		}
	}
}
