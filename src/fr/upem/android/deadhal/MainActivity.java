package fr.upem.android.deadhal;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.drawers.adapters.DrawerMainListAdapter;
import fr.upem.android.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.android.deadhal.drawers.models.DrawerMainItem;
import fr.upem.android.deadhal.fragments.*;
import fr.upem.android.deadhal.tasks.OpenTask;
import fr.upem.android.deadhal.utils.Storage;
import fr.upem.deadhal.R;

/**
 * Main activity of the deadhal project.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class MainActivity extends Activity implements DrawerMainListener {

	private Level m_level = null;
	private boolean m_newLevel = false;

	private DrawerLayout m_drawerLayout;
	private ListView m_drawerList;
	private ActionBarDrawerToggle m_drawerToggle;

	private ArrayList<DrawerMainItem> m_navDrawerItems;
	private DrawerMainListAdapter m_adapter;

	private int m_menu = R.menu.edition;
	private AbstractFragment m_fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null) {
			Log.e("savedInstance", "not null");
		}

		if (savedInstanceState == null && getIntent().getData() != null) {
			Uri uri = getIntent().getData();
			File file = new File(uri.getPath());

			OpenTask openTask = new OpenTask();
			openTask.execute(file);

			try {
				m_level = openTask.get();
				if (m_level != null) {
					Storage.copyFile(file);
				}

				SharedPreferences preferences = getSharedPreferences("pref",
						Context.MODE_PRIVATE);
				SharedPreferences.Editor ed = preferences.edit();
				ed.clear();
				ed.commit();
			} catch (Exception ignored) {
			}
		} else {
			m_level = new Level();
		}
		buildDrawer(savedInstanceState);
	}

	private void buildDrawer(Bundle savedInstanceState) {
		String[] navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items);

		TypedArray navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		m_drawerList = (ListView) findViewById(R.id.list_slidermenu);

		m_navDrawerItems = new ArrayList<DrawerMainItem>();
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1), true, String.valueOf(Storage
				.getNbFiles())));
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Help
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));

		// About
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

		navMenuIcons.recycle();

		m_drawerList.setOnItemClickListener(new SlideMenuClickListener());
		m_adapter = new DrawerMainListAdapter(getApplicationContext(),
				m_navDrawerItems);
		m_drawerList.setAdapter(m_adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		m_drawerToggle = new ActionBarDrawerToggle(this, m_drawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		m_drawerLayout.setDrawerListener(m_drawerToggle);

		if (savedInstanceState == null || m_level == null
				|| (m_level != null && m_level.getTitle() == null)) {
			displayView(FragmentType.NAVIGATION);
			m_drawerLayout.openDrawer(Gravity.LEFT);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(m_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (m_drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		boolean drawerOpen = m_drawerLayout.isDrawerOpen(m_drawerList);
		switch (m_menu) {
		case R.menu.navigation:
			menu.findItem(R.id.action_start_navigation).setVisible(!drawerOpen);
			menu.findItem(R.id.action_unlock).setVisible(!drawerOpen);
			menu.findItem(R.id.checkable_accelerometer).setVisible(!drawerOpen);
			break;
		case R.menu.edition:
			menu.findItem(R.id.action_list_objects).setVisible(!drawerOpen);
			menu.findItem(R.id.action_lock).setVisible(!drawerOpen);
			break;
		case R.menu.edition_corridor:
			menu.findItem(R.id.action_directed_corridors).setVisible(
					!drawerOpen);
			menu.findItem(R.id.action_end_corridors).setVisible(!drawerOpen);
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * Displays the view for a fragment type.
	 * 
	 * @param fragmentType
	 *            the type containing the view to display
	 */
	public void displayView(FragmentType fragmentType) {
		FragmentManager fragmentManager = getFragmentManager();
		m_fragment = null;

		switch (fragmentType) {
		case NAVIGATION:
			m_fragment = new NavigationFragment();
			m_menu = R.menu.navigation;
			break;
		case EDITION:
			m_fragment = new EditionFragment();
			m_menu = R.menu.edition;
			break;
		case OPEN:
			if (Storage.isExternalStorageReadable()) {
				m_fragment = new OpenFragment();
			} else {
				m_fragment = null;
				Toast.makeText(getApplicationContext(), R.string.error_memory,
						Toast.LENGTH_LONG).show();
			}
			m_menu = R.menu.open;
			break;
		case SAVE:
			if (Storage.isExternalStorageWritable()) {
				m_fragment = new SaveFragment();
			} else {
				m_fragment = null;
				Toast.makeText(getApplicationContext(), R.string.error_memory,
						Toast.LENGTH_LONG).show();
			}
			m_menu = R.menu.save;
			break;
		case EDITION_CORRIDOR:
			m_fragment = new EditionCorridorFragment();
			m_menu = R.menu.edition_corridor;
			break;
		case HELP:
			m_fragment = new HelpFragment();
			m_menu = R.menu.help;
			break;
		case ABOUT:
			m_fragment = new AboutFragment();
			m_menu = R.menu.about;
			break;
		default:
			break;
		}
		if (m_fragment != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelable("level", m_level);
			m_fragment.setArguments(bundle);

			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, m_fragment).commit();
			int index = fragmentType.getIndex();

			m_drawerList.setItemChecked(index, true);
			m_drawerList.setSelection(index);
		}
		m_drawerLayout.closeDrawer(m_drawerList);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		m_drawerToggle.syncState();

		if (savedInstanceState == null && m_level.getRooms().size() == 0) {
			// on first time display view for edition item
			displayView(FragmentType.NAVIGATION);
			m_drawerLayout.openDrawer(Gravity.LEFT);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		m_drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("level", m_level);
		outState.putInt("menu", m_menu);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		m_level = savedInstanceState.getParcelable("level");
		m_menu = savedInstanceState.getInt("menu");
		invalidateOptionsMenu();
	}

	@Override
	public void onFileNumberChange() {
		m_navDrawerItems.get(2).setCount(String.valueOf(Storage.getNbFiles()));
		m_adapter.notifyDataSetChanged();
	}

	@Override
	public void onFragmentChange(FragmentType type) {
		displayView(type);
	}

	@Override
	public void onLevelChange(Level level) {
		if (!m_newLevel) {
			m_level = level;
		} else {
			m_newLevel = false;
		}
	}

	/**
	 * This method is used in a resource layout.
	 * 
	 */
	public void toggleDrawerEdition(MenuItem v) {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_edit_layout);
		boolean drawerOpen = drawerLayout.isDrawerOpen(Gravity.END);
		if (drawerOpen) {
			drawerLayout.closeDrawer(Gravity.END);
		} else {
			drawerLayout.openDrawer(Gravity.END);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return keyCode == KeyEvent.KEYCODE_BACK && m_fragment.onBackPressed()
				|| super.onKeyDown(keyCode, event);
	}

	/**
	 * Slide menu item click listener
	 */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				m_newLevel = true;
				m_level = new Level();
				displayView(FragmentType.NAVIGATION);
				break;
			case 1:
				displayView(FragmentType.NAVIGATION);
				break;
			case 2:
				displayView(FragmentType.OPEN);
				break;
			case 3:
				displayView(FragmentType.SAVE);
				break;
			case 4:
				displayView(FragmentType.HELP);
				break;
			case 5:
				displayView(FragmentType.ABOUT);
				break;
			}
		}
	}
}
