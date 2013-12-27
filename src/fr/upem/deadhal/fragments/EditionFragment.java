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
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.multitouch.GestureListener;
import fr.upem.deadhal.multitouch.TouchView;

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

		// Level level = buildSampleLevel(rootView.getContext());
		m_level = getArguments().getParcelable("level");
		buildSampleLevel();
		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		// levelTitleTextView.setText(level.getTitle());
		levelTitleTextView.setText(m_level.getTitle());

		// m_touchView = new TouchView(rootView.getContext(), level);
		m_touchView = new TouchView(rootView.getContext(), m_level);

		// GestureDetector gestureDetector = new GestureDetector(
		// rootView.getContext(), new GestureListener(m_touchView, level));
		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(),
				new GestureListener(m_touchView, m_level));
		m_touchView.build(gestureDetector, savedInstanceState);

		relativeLayout.addView(m_touchView);

		return rootView;
	}

	private void buildSampleLevel() {
		m_level.addRoom(new Room("3B117", 0, 0, 120, 120));
		m_level.addRoom(new Room("3B113", 150, 0, 150 + 120, 120));
		m_level.addRoom(new Room("3B116", 0, 150, 120, 150 + 120));
		m_level.addRoom(new Room("3B112", 150, 150, 150 + 120, 150 + 120));
	}

	// private Level buildSampleLevel() {
	// Level sample = new Level("Copernic, 3rd level");
	// sample.addRoom(new Room("3B117", 0, 0, 120, 120));
	// sample.addRoom(new Room("3B113", 150, 0, 150 + 120, 120));
	//
	// sample.addRoom(new Room("3B116", 0, 150, 120, 150 + 120));
	// sample.addRoom(new Room("3B112", 150, 150, 150 + 120, 150 + 120));
	//
	// return sample;
	// }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		m_touchView.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}
}
