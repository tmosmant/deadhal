package fr.upem.deadhal.fragments;

import java.io.File;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.deadhal.fragments.dialogs.ConfirmDialogFragment;
import fr.upem.deadhal.tasks.SaveTask;
import fr.upem.deadhal.utils.Input;
import fr.upem.deadhal.utils.Storage;

public class SaveFragment extends Fragment {

	public static final int OVERRIDE_DIALOG = 1;
	private EditText m_textViewFileName;
	private DrawerMainListener m_callback;
	private String m_fileName = null;
	private Level m_level = null;

	public SaveFragment() {
	}

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_save, container,
				false);

		getActivity().setTitle(R.string.save);

		m_level = getArguments().getParcelable("level");

		m_textViewFileName = (EditText) rootView
				.findViewById(R.id.entryFileName);
		m_textViewFileName.setOnEditorActionListener(editorActionListener());

		Button button = (Button) rootView.findViewById(R.id.buttonSave);
		button.setOnClickListener(saveOnClickListener());

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private OnEditorActionListener editorActionListener() {
		return new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					Input.hideKeyboard(getActivity(), m_textViewFileName);
					if (entryIsValid()) {
						if (Storage.fileExists(m_fileName)) {
							showDialog();
						} else {
							save();
						}
					}
					return true;
				}
				return false;
			}
		};
	}

	private OnClickListener saveOnClickListener() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (entryIsValid()) {
					Input.hideKeyboard(getActivity(), m_textViewFileName);
					if (Storage.fileExists(m_fileName)) {
						showDialog();
					} else {
						save();
					}
				}
			}
		};
	}

	private boolean entryIsValid() {
		m_fileName = m_textViewFileName.getText().toString();

		if (m_fileName.isEmpty()) {
			Toast.makeText(getActivity(), R.string.invalid_save_entry,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void showDialog() {
		int title = R.string.save;
		int message = R.string.save_warning;

		DialogFragment dialogFragment = ConfirmDialogFragment.newInstance(
				title, message);
		dialogFragment.setTargetFragment(this, OVERRIDE_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(), "dialog");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case OVERRIDE_DIALOG:
			if (resultCode == Activity.RESULT_OK) {
				m_fileName = m_textViewFileName.getText().toString();
				save();
			}
			break;
		}
	}

	private void save() {
		File m_file = Storage.createFile(m_fileName);
		SaveTask saveTask = new SaveTask(getActivity(), m_file);
		saveTask.execute(m_level);
		m_textViewFileName.setText("");
		m_callback.onFileNumberChange();
	}

}
