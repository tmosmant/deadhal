package fr.upem.deadhal.fragments;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.tasks.SaveTask;
import fr.upem.deadhal.utils.OnDataPass;
import fr.upem.deadhal.utils.Storage;

public class SaveFragment extends Fragment {

	private OnDataPass m_callback;
	private String m_fileName = null;

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

		Button button = (Button) rootView.findViewById(R.id.buttonSave);
		button.setOnClickListener(saveOnClickListener(rootView));

		return rootView;
	}

	private OnClickListener saveOnClickListener(final View rootView) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) rootView
						.findViewById(R.id.entryFileName);
				m_fileName = textView.getText().toString();

				if (m_fileName.isEmpty()) {
					Toast.makeText(getActivity(),
							"Veuillez entrer un nom de fichier",
							Toast.LENGTH_SHORT).show();
				} else {
					save();
				}
			}
		};
	}

	private void save() {
		final Level m_level = getArguments().getParcelable("level");
		try {
			final File m_file = Storage.createFile(m_fileName);
			if (!m_file.exists()) {
				SaveTask saveTask = new SaveTask(getActivity(), m_file);
				saveTask.execute(m_level);
			} else {
				AlertDialog dialog = null;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Ecraser ?")
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										SaveTask saveTask = new SaveTask(getActivity(), m_file);
										saveTask.execute(m_level);
									}
								})
						.setNegativeButton(android.R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
									}
								});
				dialog = builder.create();
				dialog.show();
			}
			m_callback.nbFilePass();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}