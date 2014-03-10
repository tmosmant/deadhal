package fr.upem.deadhal.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.deadhal.fragments.dialogs.NavigationDialogFragment;
import fr.upem.deadhal.graphics.drawable.NavigationLevelDrawable;
import fr.upem.deadhal.tasks.ShortestPathTask;
import fr.upem.deadhal.view.NavigationView;
import fr.upem.deadhal.view.listeners.NavigationGestureListener;

public class NavigationFragment extends Fragment {

	private static final int NAV_DIALOG = 1;

	private Level m_level = null;
	private DrawerMainListener m_callback;
	private SharedPreferences m_prefs = null;
	private NavigationView m_view = null;
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

		m_view = new NavigationView(rootView.getContext(), m_levelHandler,
				levelDrawable);

		m_levelHandler.setView(m_view);

		NavigationGestureListener m_gestureListener = new NavigationGestureListener(
				m_view, m_levelHandler);

		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), m_gestureListener);
		m_view.build(gestureDetector, savedInstanceState, m_prefs);
		relativeLayout.addView(m_view);

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
				Room start = data.getParcelableExtra("start");
				Room end = data.getParcelableExtra("end");
				System.out.println(start.getName());
				System.out.println(end.getName());
				System.out.println(m_level.getCorridors().size());
				System.out.println(m_levelHandler.getLevel().getCorridors()
						.size());
				if (start.equals(end)) {
					Toast.makeText(getActivity(), R.string.start_end,
							Toast.LENGTH_LONG).show();
				} else {
					m_levelHandler.setRoomStart(null);
					m_levelHandler.setRoomEnd(null);
					ShortestPathTask spt = new ShortestPathTask(start.getId(),
							end.getId());
					spt.execute(m_levelHandler.getLevel());
					try {
						List<UUID> listCoridors = spt.get();
						if (listCoridors == null) {
							Toast.makeText(getActivity(),
									R.string.no_path_found, Toast.LENGTH_LONG)
									.show();
						} else {
							m_levelHandler.setRoomStart(start);
							m_levelHandler.setRoomEnd(end);
							Toast.makeText(getActivity(),
									R.string.follow_the_green_path,
									Toast.LENGTH_LONG).show();
							m_levelHandler.setShortestPath(listCoridors);
							m_view.refresh();
						}
					} catch (InterruptedException e) {
						Toast.makeText(getActivity(),
								"An error occured : " + e.getMessage(),
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} catch (ExecutionException e) {
						Toast.makeText(getActivity(),
								"An error occured : " + e.getMessage(),
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
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
			if (m_level.nbRooms() >= 2) {
				showNavigationDialog();
			} else {
				Toast.makeText(getActivity(), R.string.need_at_least_2_rooms,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showNavigationDialog() {
		int title = R.string.navigation;

		ArrayList<Room> rooms = new ArrayList<Room>(m_level.getRooms().values());
		Collections.sort(rooms, new Comparator<Room>() {

			@Override
			public int compare(Room lhs, Room rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});

		DialogFragment dialogFragmentEnd = NavigationDialogFragment
				.newInstance(title, rooms);
		dialogFragmentEnd.setTargetFragment(this, NAV_DIALOG);
		dialogFragmentEnd.show(getFragmentManager().beginTransaction(),
				"navigationDialog");
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
}
