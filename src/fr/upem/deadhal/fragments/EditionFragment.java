package fr.upem.deadhal.fragments;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.drawers.adapters.DrawerEditionListAdapter;
import fr.upem.deadhal.drawers.models.DrawerEditionItem;
import fr.upem.deadhal.fragments.dialogs.InputDialogFragment;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.view.EditionView;
import fr.upem.deadhal.view.listeners.EditionGestureListener;

public class EditionFragment extends Fragment {

	private static final int ADD_NEW_ROOM = 0;
	private Level m_level = null;
	private EditionView m_editionView = null;
	private SharedPreferences m_prefs = null;
	private EditionGestureListener m_editionGestureListener;
	private ListView m_drawerList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_edition,
				container, false);

		getActivity().setTitle(R.string.edition);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.edit_layout);

		m_level = getArguments().getParcelable("level");

		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(m_level.getTitle());

		LevelDrawable levelDrawable = new LevelDrawable(m_level);

		m_editionView = new EditionView(rootView.getContext(), levelDrawable);

		m_editionGestureListener = new EditionGestureListener(m_editionView,
				levelDrawable);
		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), m_editionGestureListener);
		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		m_editionView.build(gestureDetector, savedInstanceState, m_prefs);

		relativeLayout.addView(m_editionView);

		DrawerLayout drawerLayout = (DrawerLayout) rootView
				.findViewById(R.id.drawer_edit_layout);

		drawerLayout.setDrawerListener(new ActionBarDrawerToggle(getActivity(),
				drawerLayout, R.drawable.ic_action_collection_dark,
				R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerOpened(View drawerView) {
				// nothing to do
				super.onDrawerOpened(drawerView);
			}
		});
		updateDrawer(rootView);

		m_editionGestureListener
				.addSelectionRoomListener(new SelectionRoomListener() {
					@Override
					public void onUnselectRoom(Room room) {
						for (int i = 0; i < m_drawerList.getAdapter()
								.getCount(); i++) {
							DrawerEditionItem item = (DrawerEditionItem) m_drawerList
									.getAdapter().getItem(i);
							Room room2 = item.getRoom();
							if (room2 != null
									&& room.getId().equals(room2.getId())) {
								m_drawerList.setItemChecked(i, false);
								return;
							}
						}
					}

					@Override
					public void onSelectRoom(Room room) {
						for (int i = 0; i < m_drawerList.getAdapter()
								.getCount(); i++) {
							DrawerEditionItem item = (DrawerEditionItem) m_drawerList
									.getAdapter().getItem(i);
							Room room2 = item.getRoom();
							if (room2 != null
									&& room.getId().equals(room2.getId())) {
								m_drawerList.setItemChecked(i, true);
							}
						}
					}
				});
		return rootView;
	}

	public void updateDrawer(View rootView) {

		m_drawerList = (ListView) rootView
				.findViewById(R.id.list_edit_slidermenu);
		ArrayList<DrawerEditionItem> drawerItems = new ArrayList<DrawerEditionItem>();
		drawerItems.add(new DrawerEditionItem(getString(R.string.add_room),
				true));
		for (Room room : m_level.getRooms().values()) {
			drawerItems.add(new DrawerEditionItem(room));
		}
		drawerItems.add(new DrawerEditionItem(getString(R.string.add_corridor),
				true));
		for (Corridor corridor : m_level.getCorridors().values()) {
			String title = corridorTitle(corridor);
			drawerItems.add(new DrawerEditionItem(corridor, title));
		}
		DrawerEditionListAdapter adapter = new DrawerEditionListAdapter(
				getActivity(), this, drawerItems, m_editionGestureListener);
		m_drawerList.setAdapter(adapter);

		m_drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					int title = R.string.action_add;

					DialogFragment dialogFragment = InputDialogFragment
							.newInstance(title);
					dialogFragment.setTargetFragment(EditionFragment.this,
							ADD_NEW_ROOM);
					dialogFragment.show(
							getFragmentManager().beginTransaction(),
							"addDialog");

					m_drawerList.setItemChecked(position, false);
				} else {
					DrawerEditionItem item = (DrawerEditionItem) parent
							.getItemAtPosition(position);
					Room room = item.getRoom();
					if (room != null) {

						boolean selectRoom = m_editionGestureListener
								.selectRoom(room);
						m_drawerList.setItemChecked(position, selectRoom);
					}
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_NEW_ROOM:
			if (resultCode == Activity.RESULT_OK) {
				String title = data.getStringExtra("inputText");
				Room room = new Room(UUID.randomUUID(), title, new RectF(0, 0,
						150, 150));
				m_editionGestureListener.addRoom(room);
				updateDrawer(getView());
				m_editionGestureListener.selectRoom(room);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String corridorTitle(Corridor corridor) {
		UUID src = corridor.getSrc();
		UUID dst = corridor.getDst();
		String strSrc = m_level.getRooms().get(src).getTitle();
		String strDst = m_level.getRooms().get(dst).getTitle();
		String op = (corridor.isDirected()) ? " <-> " : " -> ";
		return strSrc + op + strDst;
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
