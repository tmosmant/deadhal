package fr.upem.deadhal.fragments;

import android.app.Fragment;
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
import fr.upem.deadhal.view.GestureListener;
import fr.upem.deadhal.view.TouchView;

public class EditionFragment extends Fragment {

	private Level m_level = null;
	private TouchView m_touchView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit, container,
				false);

		getActivity().setTitle(R.string.edit);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.edit_layout);

		m_level = getArguments().getParcelable("level");

		// m_level = buildSampleLevel();
		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(m_level.getTitle());

		LevelDrawable levelDrawable = new LevelDrawable(m_level);

		m_touchView = new TouchView(rootView.getContext(), levelDrawable);

		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), new GestureListener(m_touchView,
						levelDrawable));
		m_touchView.build(gestureDetector, savedInstanceState);

		relativeLayout.addView(m_touchView);

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		m_touchView.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}

}
