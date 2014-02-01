package fr.upem.deadhal.fragments;

import java.util.UUID;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.view.EditionView;
import fr.upem.deadhal.view.listeners.EditGestureListener;

public class EditionFragment extends Fragment {

	private Level m_level = null;
	private EditionView m_editionView = null;
	private SharedPreferences m_prefs = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edition, container,
				false);

		getActivity().setTitle(R.string.edition);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.edit_layout);

		m_level = getArguments().getParcelable("level");

		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(m_level.getTitle());

		LevelDrawable levelDrawable = new LevelDrawable(m_level);

		m_editionView = new EditionView(rootView.getContext(), levelDrawable);

		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), new EditGestureListener(m_editionView,
						levelDrawable));
		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		m_editionView.build(gestureDetector, savedInstanceState, m_prefs);

		relativeLayout.addView(m_editionView);

		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			newRoom();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void newRoom() {
		System.out.println("newroom");
		m_level.addRoom(new Room(UUID.randomUUID(), "new room", new RectF(0, 0,
				120, 120)));
	}

	@Override
	public void onPause() {
		m_editionView.saveMatrix(m_prefs);
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		m_editionView.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}

}
