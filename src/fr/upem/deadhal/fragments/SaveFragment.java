package fr.upem.deadhal.fragments;

import android.app.Fragment;
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
import fr.upem.deadhal.save.SaveTask;

public class SaveFragment extends Fragment {

	public SaveFragment() {
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
				String m_fileName = textView.getText().toString();

				if (m_fileName.isEmpty()) {
					Toast.makeText(getActivity(),
							"Veuillez entrer un nom de fichier",
							Toast.LENGTH_SHORT).show();
				} else {
					Level m_level = getArguments().getParcelable("level");
					SaveTask saveTask = new SaveTask(getActivity(), m_fileName);
					saveTask.execute(m_level);
				}
			}
		};
	}

}
