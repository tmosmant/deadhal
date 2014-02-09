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
import fr.upem.deadhal.fragments.dialogs.MonoTouchNavigationDialogFragment;
import fr.upem.deadhal.fragments.dialogs.MultiTouchNavigationDialogFragment;
import fr.upem.deadhal.graphics.drawable.NavigationLevelDrawable;
import fr.upem.deadhal.view.NavigationView;
import fr.upem.deadhal.view.listeners.NavigationGestureListener;

public class NavigationFragment extends Fragment {

	private static final int MONO_TOUCH_NAV_DIALOG = 1;
	private static final int MULTI_TOUCH_NAV_DIALOG = 2;
	private static final int RESULT_CODE_ROOM_START = 1;
	private static final int RESULT_CODE_ROOM_END = 2;
	private Room m_start = null;
	private Room m_end = null;
	private Room m_selected = null;

	private Level m_level = null;
	private DrawerMainListener m_callback;
	private SharedPreferences m_prefs = null;
	private NavigationView m_navigationView = null;

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

		NavigationLevelHandler m_levelHandler = new NavigationLevelHandler(
				m_level);

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
				m_selected = room;
				showMonoTouchNavigationDialog();
			}

			@Override
			public void onUnselectRoom(Room room) {
			}
		});

		return rootView;
	}

	private void showMonoTouchNavigationDialog() {
		int title = R.string.navigation;
		int message = R.string.yes;

		DialogFragment dialogFragment = MonoTouchNavigationDialogFragment
				.newInstance(title, message);
		dialogFragment.setTargetFragment(this, MONO_TOUCH_NAV_DIALOG);
		dialogFragment.show(getFragmentManager().beginTransaction(),
				"navigationDialog");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case MONO_TOUCH_NAV_DIALOG:
			if (resultCode == RESULT_CODE_ROOM_START) {
				m_start = m_selected;
				// m_gestureListener.setStart(m_start);
			} else if (resultCode == RESULT_CODE_ROOM_END) {
				m_end = m_selected;
				// m_gestureListener.setEnd(m_end);
			} else if (m_selected != null && m_start != null
					&& m_start.getId().equals(m_selected.getId())) {
				m_start = null;
				// m_gestureListener.setStart(m_start);
			} else if (m_selected != null && m_end != null
					&& m_end.getId().equals(m_selected.getId())) {
				m_end = null;
				// m_gestureListener.setEnd(m_end);
			}
			break;

		case MULTI_TOUCH_NAV_DIALOG:
			if (resultCode == RESULT_CODE_ROOM_START) {
				Room room = data.getParcelableExtra("room");
				Log.e("room start", room.getName());
				m_start = room;
			} else if (resultCode == RESULT_CODE_ROOM_END) {
				Room room = data.getParcelableExtra("room");
				Log.e("room end", room.getName());
				m_end = room;
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
		case R.id.action_start_navigation_mono_touch:
			if (m_start != null && m_end != null) {
				Log.e("start", m_start.toString());
				Log.e("end", m_end.toString());
			} else {
				Toast.makeText(getActivity(), "Please choose start and end",
						Toast.LENGTH_SHORT).show();
			}
			return true;
		case R.id.action_start_navigation_multi_touch:
			showMultiTouchNavigationDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showMultiTouchNavigationDialog() {
		int title = R.string.navigation;
		int message = R.string.yes;

		ArrayList<Room> rooms = new ArrayList<Room>(m_level.getRooms().values());

		DialogFragment dialogFragmentEnd = MultiTouchNavigationDialogFragment
				.newInstance(RESULT_CODE_ROOM_END, title, message, rooms);
		dialogFragmentEnd.setTargetFragment(this, MULTI_TOUCH_NAV_DIALOG);
		dialogFragmentEnd.show(getFragmentManager().beginTransaction(),
				"navigationDialog");

		DialogFragment dialogFragmentStart = MultiTouchNavigationDialogFragment
				.newInstance(RESULT_CODE_ROOM_START, title, message, rooms);
		dialogFragmentStart.setTargetFragment(this, MULTI_TOUCH_NAV_DIALOG);
		dialogFragmentStart.show(getFragmentManager().beginTransaction(),
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
