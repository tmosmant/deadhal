package fr.upem.deadhal.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;

public class SaveFragment extends Fragment {
	
	private Level m_level;

	private File m_directory = new File(
			Environment.getExternalStorageDirectory() + File.separator
					+ "deadhal");
	private static final String FTYPE = ".xml";

	public SaveFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_save, container,
				false);

		getActivity().setTitle(R.string.save);
		
		if (!m_directory.exists()) {
			m_directory.mkdirs();
		}

		final TextView textView = (TextView) rootView
				.findViewById(R.id.entryFileName);
		Button button = (Button) rootView.findViewById(R.id.buttonSave);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String m_fileName = textView.getText().toString();
				if (m_fileName.isEmpty()) {
					Toast.makeText(getActivity(),
							"Veuillez entrer un nom de fichier",
							Toast.LENGTH_SHORT).show();
				} else {
					saveFile(m_fileName);
				}
			}
		});
		

		m_level = getArguments().getParcelable("level");
		Log.i("level", m_level.toString());
		
		return rootView;
	}

	private void saveFile(String fileName) {
		try {
			File m_file = new File(m_directory.getAbsolutePath()
					+ File.separator + fileName + FTYPE);
			m_file.createNewFile();
			
			FileOutputStream m_os = new FileOutputStream(m_file);
			OutputStreamWriter m_osw = new OutputStreamWriter(m_os);
			m_osw.append("blabla");
			m_osw.close();
			m_os.close();
			
			Toast.makeText(getActivity(), "Sauvegarde effectuée", Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}
}
