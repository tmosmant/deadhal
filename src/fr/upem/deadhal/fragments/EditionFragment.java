package fr.upem.deadhal.fragments;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.drawers.adapters.DrawerEditionListAdapter;
import fr.upem.deadhal.drawers.models.DrawerEditionItem;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.view.EditionView;
import fr.upem.deadhal.view.listeners.EditionGestureListener;

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
				rootView.getContext(), new EditionGestureListener(
						m_editionView, levelDrawable));
		m_prefs = getActivity().getSharedPreferences("pref",
				Context.MODE_PRIVATE);
		m_editionView.build(gestureDetector, savedInstanceState, m_prefs);

		relativeLayout.addView(m_editionView);

		buildEditionDrawer(rootView);

		return rootView;
	}

	private void buildEditionDrawer(View rootView) {
		// DrawerLayout drawerLayout = (DrawerLayout) rootView
		// .findViewById(R.id.drawer_edit_layout);

		ListView drawerList = (ListView) rootView
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
				getActivity(), drawerItems);
		drawerList.setAdapter(adapter);
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
