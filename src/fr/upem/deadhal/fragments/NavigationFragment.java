package fr.upem.deadhal.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.deadhal.fragments.dialogs.NavigationDialogFragment;
import fr.upem.deadhal.graphics.drawable.NavigationLevelDrawable;
import fr.upem.deadhal.view.NavigationView;
import fr.upem.deadhal.view.listeners.NavigationGestureListener;

public class NavigationFragment extends Fragment {
	private static final int NAV_DIALOG = 1;
	private Room m_start = null;
	private Room m_end = null;
	private Room m_selected = null;

	private Level m_level = null;
	private DrawerMainListener m_callback;
	private SharedPreferences m_prefs = null;
	private NavigationView m_navigationView = null;
	private NavigationGestureListener m_navigationGestureListener;

	public NavigationFragment() {
	}

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
		View rootView = inflater.inflate(R.layout.fragment_navigation,
				container, false);

		getActivity().setTitle(R.string.navigation);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.navigation_layout);

		m_level = getArguments().getParcelable("level");

		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(m_level.getTitle());

		NavigationLevelDrawable levelDrawable = new NavigationLevelDrawable(m_level);

		m_navigationView = new NavigationView(rootView.getContext(),
				levelDrawable);

		m_navigationGestureListener = new NavigationGestureListener(
				m_navigationView, levelDrawable);

		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), m_navigationGestureListener);
		m_navigationView.build(gestureDetector, savedInstanceState, m_prefs);
		relativeLayout.addView(m_navigationView);

		m_navigationGestureListener
				.addSelectionRoomListener(new SelectionRoomListener() {

					@Override
					public void onSelectRoom(Room room) {
						m_selected = room;
						showNavigationDialog();
					}

					@Override
					public void onUnselectRoom(Room room) {
					}
				});

		return rootView;
	}

	private void showNavigationDialog() {
		int title = R.string.action_remove;
		int message = R.string.remove_warning;

		DialogFragment dialogFragment = NavigationDialogFragment.newInstance(
				title, message);
		dialogFragment.setTargetFragment(this, NAV_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(),
				"navigationDialog");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case NAV_DIALOG:
			if (resultCode == 1) {
				m_start = m_selected;
				m_navigationGestureListener.setStart(m_start);
			} else if (resultCode == 2) {
				m_end = m_selected;
				m_navigationGestureListener.setEnd(m_end);
			}
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_unlock:
			m_callback.onFragmentChange(FragmentType.EDITION);
			return true;
		case R.id.action_start_navigation:
			if (m_start != null && m_end != null) {
				Log.e("start", m_start.toString());
				Log.e("end", m_end.toString());
			}
			else {
				Toast.makeText(getActivity(), "Please choose start and end", Toast.LENGTH_SHORT)
				.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		m_navigationView.saveMatrix(m_prefs);
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		m_navigationView.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}

}
