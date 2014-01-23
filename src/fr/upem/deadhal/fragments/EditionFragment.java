package fr.upem.deadhal.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.view.EditView;
import fr.upem.deadhal.view.GestureListener;

public class EditionFragment extends Fragment {

	private Level m_level = null;
	private EditView m_editView = null;
	private SharedPreferences m_prefs = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit, container,
				false);

		getActivity().setTitle(R.string.edit);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.edit_layout);

		m_level = getArguments().getParcelable("level");

		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(m_level.getTitle());

		LevelDrawable levelDrawable = new LevelDrawable(m_level);

		m_editView = new EditView(rootView.getContext(), levelDrawable);

		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), new GestureListener(m_editView,
						levelDrawable));
		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		if (savedInstanceState != null) {
			m_editView.build(gestureDetector, savedInstanceState);
		} else {
			m_editView.build(gestureDetector, m_prefs);
		}
		
		relativeLayout.addView(m_editView);

		return rootView;
	}
	
	@Override
	public void onPause() {
		m_editView.saveMatrix(m_prefs);
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		m_editView.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}

}
