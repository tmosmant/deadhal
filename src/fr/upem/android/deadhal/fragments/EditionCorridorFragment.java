package fr.upem.android.deadhal.fragments;

import android.app.Activity;
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
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.handlers.EditionCorridorLevelHandler;
import fr.upem.android.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.android.deadhal.graphics.drawable.EditionCorridorLevelDrawable;
import fr.upem.android.deadhal.view.EditionCorridorView;
import fr.upem.android.deadhal.view.listeners.EditionCorridorGestureListener;
import fr.upem.deadhal.R;

/**
 * This class is the edition corridor fragment. Used to handle the addition of
 * corridors.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class EditionCorridorFragment extends AbstractFragment {

	private DrawerMainListener m_callback;

	private Level m_level = null;
	private EditionCorridorView m_view;
	private SharedPreferences m_prefs;
	private TextView m_levelTitleTextView;
	private EditionCorridorLevelHandler m_levelHandler;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			m_callback = (DrawerMainListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException();
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
		View m_rootView = inflater.inflate(R.layout.fragment_edition_corridor,
				container, false);

		getActivity().setTitle(R.string.corridors);

		RelativeLayout relativeLayout = (RelativeLayout) m_rootView
				.findViewById(R.id.edit_corridor_layout);

		m_level = getArguments().getParcelable("level");
		if (savedInstanceState != null) {
			m_level = savedInstanceState.getParcelable("level");
		}

		m_levelTitleTextView = (TextView) m_rootView
				.findViewById(R.id.levelTitleTextView);
		m_levelTitleTextView.setText(m_level.getTitle());

		m_levelHandler = new EditionCorridorLevelHandler(m_level);

		EditionCorridorLevelDrawable levelDrawable = new EditionCorridorLevelDrawable(
				m_levelHandler);

		m_view = new EditionCorridorView(m_rootView.getContext(),
				m_levelHandler, levelDrawable);

		m_levelHandler.setView(m_view);

		EditionCorridorGestureListener m_editionCorridorGestureListener = new EditionCorridorGestureListener(
				m_view, m_levelHandler);
		GestureDetector gestureDetector = new GestureDetector(
				m_rootView.getContext(), m_editionCorridorGestureListener);
		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);

		m_view.build(gestureDetector, savedInstanceState, m_prefs);

		relativeLayout.addView(m_view);

		return m_rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_directed_corridors:
			if (m_levelHandler.isDirected()) {
				m_levelHandler.setDirected(false);
				item.setIcon(R.drawable.ic_action_repeat);
			} else {
				item.setIcon(R.drawable.ic_action_reply);
				m_levelHandler.setDirected(true);
			}
			return true;
		case R.id.action_end_corridors:
			m_callback.onFragmentChange(FragmentType.EDITION);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		m_view.saveMatrix(m_prefs);
		m_callback.onLevelChange(m_level);
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("level", m_level);
		m_view.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onBackPressed() {
		if (m_levelHandler.getStart() != null) {
			m_levelHandler.setStart(null);
			m_view.refresh();
		} else {
			m_callback.onFragmentChange(FragmentType.EDITION);
		}
		return true;
	}
}
