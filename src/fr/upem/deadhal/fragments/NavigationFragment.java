package fr.upem.deadhal.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
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
	private NavigationLevelHandler m_levelHandler = null;

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

		m_levelHandler = new NavigationLevelHandler(m_level);

		NavigationLevelDrawable levelDrawable = new NavigationLevelDrawable(
				m_levelHandler);

		m_navigationView = new NavigationView(rootView.getContext(),
				m_levelHandler, levelDrawable);

		NavigationGestureListener m_gestureListener = new NavigationGestureListener(
				m_navigationView, m_levelHandler);

		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), m_gestureListener);
		m_navigationView.build(gestureDetector, savedInstanceState, m_prefs);
		relativeLayout.addView(m_navigationView);

		m_levelHandler.addSelectionRoomListener(new SelectionRoomListener() {

			@Override
			public void onSelectRoom(Room room) {
			}

			@Override
			public void onUnselectRoom(Room room) {
			}
		});

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case NAV_DIALOG:
			if (resultCode == Activity.RESULT_OK) {
				m_start = data.getParcelableExtra("start");
				m_end = data.getParcelableExtra("end");

				if (m_start.equals(m_end)) {
					Toast.makeText(getActivity(), "Depart = Arrive !",
							Toast.LENGTH_LONG).show();
				} else {
					Log.e("start", m_start.getName());
					Log.e("end", m_end.getName());
					m_levelHandler.setRoomStart(m_start);
					m_levelHandler.setRoomEnd(m_end);
					// START DIJKSTRA !
				}
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
			showNavigationDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showNavigationDialog() {
		int title = R.string.navigation;

		ArrayList<Room> rooms = new ArrayList<Room>(m_level.getRooms().values());

		DialogFragment dialogFragmentEnd = NavigationDialogFragment
				.newInstance(title, rooms);
		dialogFragmentEnd.setTargetFragment(this, NAV_DIALOG);
		dialogFragmentEnd.show(getFragmentManager().beginTransaction(),
				"navigationDialog");
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
