package fr.upem.deadhal.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.utils.FragmentObserver;
import fr.upem.deadhal.utils.Position;
import fr.upem.deadhal.view.ConsultGestureListener;
import fr.upem.deadhal.view.ConsultView;

public class ConsultFragment extends Fragment {

	private Level m_level = null;
	private FragmentObserver m_callback;
	private SharedPreferences m_prefs = null;
	private ConsultView m_consultView = null;

	public ConsultFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			m_callback = (FragmentObserver) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDataPass");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_consult, container,
				false);

		getActivity().setTitle(R.string.consult);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.consult_layout);

		m_level = getArguments().getParcelable("level");

		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(m_level.getTitle());

		LevelDrawable levelDrawable = new LevelDrawable(m_level);

		m_consultView = new ConsultView(rootView.getContext(), levelDrawable);

		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), new ConsultGestureListener(m_consultView));
		m_consultView.build(gestureDetector, savedInstanceState, m_prefs);
		relativeLayout.addView(m_consultView);

		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			m_callback.notifyFragmentChange(Position.edit);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		m_consultView.saveMatrix(m_prefs);
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		m_consultView.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}

}
