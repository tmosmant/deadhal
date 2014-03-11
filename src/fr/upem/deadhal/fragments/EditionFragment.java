package fr.upem.deadhal.fragments;

import java.util.ArrayList;
import java.util.List;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.deadhal.MainActivity;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.drawers.adapters.DrawerEditionListAdapter;
import fr.upem.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.deadhal.drawers.models.DrawerEditionItem;
import fr.upem.deadhal.drawers.models.DrawerEditionItem.Type;
import fr.upem.deadhal.fragments.dialogs.InputDialogFragment;
import fr.upem.deadhal.graphics.drawable.EditionLevelDrawable;
import fr.upem.deadhal.utils.Input;
import fr.upem.deadhal.view.EditionView;
import fr.upem.deadhal.view.listeners.EditionGestureListener;

public class EditionFragment extends Fragment {

	private static final int ADD_NEW_ROOM = 0;
	private static final int ADD_NEW_CORRIDOR = 1;

	private DrawerMainListener m_callback;

	private Level m_level = null;
	private EditionView m_view;
	private SharedPreferences m_prefs;
	private ListView m_drawerList;
	private TextView m_levelTitleTextView;
	private EditText m_levelTitleEditText;
	private EditionLevelHandler m_levelHandler;
	private DrawerLayout m_drawerLayout;

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
		View m_rootView = inflater.inflate(R.layout.fragment_edition,
				container, false);

		getActivity().setTitle(R.string.edition);

		RelativeLayout relativeLayout = (RelativeLayout) m_rootView
				.findViewById(R.id.edit_layout);

		m_level = getArguments().getParcelable("level");
		if (savedInstanceState != null) {
			m_level = savedInstanceState.getParcelable("level");
		}

		m_levelTitleTextView = (TextView) m_rootView
				.findViewById(R.id.levelTitleTextView);
		m_levelTitleTextView.setText(m_level.getTitle());

		m_levelHandler = new EditionLevelHandler(m_level);

		EditionLevelDrawable levelDrawable = new EditionLevelDrawable(
				m_levelHandler);

		m_view = new EditionView(m_rootView.getContext(), m_levelHandler,
				levelDrawable);

		m_levelHandler.setView(m_view);

		EditionGestureListener m_editionGestureListener = new EditionGestureListener(
				m_view, m_levelHandler);
		GestureDetector gestureDetector = new GestureDetector(
				m_rootView.getContext(), m_editionGestureListener);
		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);

		m_view.build(gestureDetector, savedInstanceState, m_prefs);

		relativeLayout.addView(m_view);

		m_drawerLayout = (DrawerLayout) m_rootView
				.findViewById(R.id.drawer_edit_layout);

		m_drawerLayout.setDrawerListener(new ActionBarDrawerToggle(
				getActivity(), m_drawerLayout,
				R.drawable.ic_action_collection_dark, R.string.app_name,
				R.string.app_name) {

			@Override
			public void onDrawerOpened(View drawerView) {
				// nothing to do
				super.onDrawerOpened(drawerView);
			}
		});

		m_levelTitleEditText = (EditText) m_rootView
				.findViewById(R.id.edit_text_level_name);
		m_levelTitleEditText.setText(m_level.getTitle());
		m_levelTitleEditText
				.setOnEditorActionListener(buildEditorActionListener());

		m_drawerList = (ListView) m_rootView
				.findViewById(R.id.list_edit_slidermenu);

		OnItemClickListener listener = buildOnItemClickListener();

		m_drawerList.setOnItemClickListener(listener);

		updateDrawer();

		m_levelHandler.addSelectionRoomListener(new SelectionRoomListener() {

			@Override
			public void onUnselectRoom(Room room) {
				m_view.getVibrator().vibrate(100);
				for (int i = 0; i < m_drawerList.getAdapter().getCount(); i++) {
					DrawerEditionItem item = (DrawerEditionItem) m_drawerList
							.getAdapter().getItem(i);
					Room room2 = item.getRoom();
					if (room2 != null && room.getId().equals(room2.getId())) {
						m_drawerList.setItemChecked(i, false);
						return;
					}
				}
			}

			@Override
			public void onSelectRoom(Room room) {
				m_view.getVibrator().vibrate(100);
				for (int i = 0; i < m_drawerList.getAdapter().getCount(); i++) {
					DrawerEditionItem item = (DrawerEditionItem) m_drawerList
							.getAdapter().getItem(i);
					Room room2 = item.getRoom();
					if (room2 != null && room.getId().equals(room2.getId())) {
						m_drawerList.setItemChecked(i, true);
					}
				}
			}
		});
		return m_rootView;
	}

	private TextView.OnEditorActionListener buildEditorActionListener() {
		return new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					Input.hideKeyboard(getActivity(), m_levelTitleTextView);
					String title = m_levelTitleEditText.getText().toString();
					m_level.setTitle(title);
					m_levelTitleTextView.setText(title);
					return true;
				}
				return false;
			}
		};
	}

	private OnItemClickListener buildOnItemClickListener() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DrawerEditionItem item = (DrawerEditionItem) parent
						.getItemAtPosition(position);
				Type type = item.getType();
				switch (type) {
				case ADD_CORRIDOR:
					m_drawerList.setItemChecked(position, false);

					m_drawerLayout.closeDrawers();

					MainActivity activity = (MainActivity) getActivity();
					activity.displayView(FragmentType.EDITION_CORRIDOR);
					m_levelHandler.unselectRoom();

					break;
				case ADD_ROOM:
					int title = R.string.action_add;

					DialogFragment dialogFragment = InputDialogFragment
							.newInstance(title);
					dialogFragment.setTargetFragment(EditionFragment.this,
							ADD_NEW_ROOM);
					dialogFragment.show(
							getFragmentManager().beginTransaction(),
							"addDialog");

					m_levelHandler.unselectRoom();
					m_drawerList.setItemChecked(position, false);
					break;
				case CORRIDOR:
					m_levelHandler.unselectRoom();
					m_drawerList.setItemChecked(position, false);
					break;
				case ROOM:
					Room room = item.getRoom();
					if (room != null) {
						boolean selectRoom = m_levelHandler.selectRoom(room);
						m_drawerList.setItemChecked(position, selectRoom);
					}
					break;
				}
			}
		};
	}

	public void updateDrawer() {
		ArrayList<DrawerEditionItem> drawerItems = new ArrayList<DrawerEditionItem>();
		drawerItems.add(new DrawerEditionItem(getString(R.string.add_room),
				DrawerEditionItem.Type.ADD_ROOM));
		List<Room> roomByName = m_levelHandler.getRoomByName();
		for (Room room : roomByName) {
			drawerItems.add(new DrawerEditionItem(room));
		}
		drawerItems.add(new DrawerEditionItem(getString(R.string.add_corridor),
				DrawerEditionItem.Type.ADD_CORRIDOR));
		for (Corridor corridor : m_levelHandler.getCorridorBySrc()) {
			String title = corridorTitle(corridor);

			drawerItems.add(new DrawerEditionItem(corridor, title));
		}
		DrawerEditionListAdapter adapter = new DrawerEditionListAdapter(
				getActivity(), drawerItems, m_levelHandler, m_view, this);
		m_drawerList.setAdapter(adapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_NEW_ROOM:
			if (resultCode == Activity.RESULT_OK) {
				String name = data.getStringExtra("inputText");
				Room room = new Room(UUID.randomUUID(), name, new RectF(0, 0,
						150, 150));
				m_levelHandler.addRoom(room);
				updateDrawer();
				m_levelHandler.selectRoom(room);
			}
			break;
		case ADD_NEW_CORRIDOR:
			if (resultCode == Activity.RESULT_OK) {
				String title = data.getStringExtra("inputText");
				Room room = new Room(UUID.randomUUID(), title, new RectF(0, 0,
						150, 150));
				m_levelHandler.addRoom(room);
				updateDrawer();
				m_levelHandler.selectRoom(room);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String corridorTitle(Corridor corridor) {
		UUID src = corridor.getSrc();
		UUID dst = corridor.getDst();
		String strSrc = m_level.getRooms().get(src).getName();
		String strDst = m_level.getRooms().get(dst).getName();
		String op = (corridor.isDirected()) ? " -> " : " <-> ";
		return strSrc + op + strDst;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_lock:
			m_callback.onFragmentChange(FragmentType.NAVIGATION);
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
}
