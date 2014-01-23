package fr.upem.deadhal.fragments;

import java.io.File;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.tasks.SaveTask;
import fr.upem.deadhal.utils.OnDataPass;
import fr.upem.deadhal.utils.Storage;

public class SaveFragment extends Fragment {

	private TextView m_textView;
	private OnDataPass m_callback;
	private String m_fileName = null;
	public static final int DIALOG_FRAGMENT = 1;

	public SaveFragment() {
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
		View rootView = inflater.inflate(R.layout.fragment_save, container,
				false);

		getActivity().setTitle(R.string.save);

		m_textView = (TextView) rootView.findViewById(R.id.entryFileName);
		m_textView.setOnEditorActionListener(editorActionListener());

		Button button = (Button) rootView.findViewById(R.id.buttonSave);
		button.setOnClickListener(saveOnClickListener(rootView));

		return rootView;
	}

	private OnEditorActionListener editorActionListener() {
		return new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					hideKeyboard();
					m_fileName = v.getText().toString();
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

	private OnClickListener saveOnClickListener(final View rootView) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) rootView
						.findViewById(R.id.entryFileName);
				m_fileName = textView.getText().toString();

				if (entryIsValid()) {
					hideKeyboard();
					if (Storage.fileExists(m_fileName)) {
						showDialog();
					} else {
						save();
					}
				}
			}
		};
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(m_textView.getWindowToken(), 0);
	}

	private boolean entryIsValid() {
		if (m_fileName.isEmpty()) {
			Toast.makeText(getActivity(), "Veuillez entrer un nom de fichier",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void showDialog() {
		int title = R.string.save;
		int message = R.string.save_warning;

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
				m_fileName = m_textView.getText().toString();
				save();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(getActivity(), "Sauvegarde annulée",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void save() {
		Level m_level = getArguments().getParcelable("level");
		File m_file = Storage.createFile(m_fileName);
		SaveTask saveTask = new SaveTask(getActivity(), m_file);
		saveTask.execute(m_level);
		m_textView.setText("");
		m_callback.nbFilePass();
	}

}