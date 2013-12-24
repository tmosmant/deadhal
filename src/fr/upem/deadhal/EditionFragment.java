package fr.upem.deadhal;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.multitouch.GestureListener;
import fr.upem.deadhal.multitouch.TouchView;

public class EditionFragment extends Fragment {

	public EditionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit, container,
				false);

		getActivity().setTitle(R.string.edit);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.edit_layout);

		Level level = buildSampleLevel(rootView.getContext());
		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(level.getTitle());

		TouchView touchView = new TouchView(rootView.getContext(), level);

		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), new GestureListener(touchView, level));
		touchView.build(gestureDetector);

		relativeLayout.addView(touchView);

		return rootView;
	}

	private Level buildSampleLevel(Context context) {
		Level sample = new Level(context, "Copernic, 3rd level");
		sample.addRoom(new Room("3B117", 0, 0, 120, 120));
		sample.addRoom(new Room("3B113", 150, 0, 150 + 120, 120));

		sample.addRoom(new Room("3B116", 0, 150, 120, 150 + 120));
		sample.addRoom(new Room("3B112", 150, 150, 150 + 120, 150 + 120));

		return sample;
	}
}
