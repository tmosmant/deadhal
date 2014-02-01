package fr.upem.deadhal;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.drawers.adapters.DrawerMainListAdapter;
import fr.upem.deadhal.drawers.listeners.DrawerMainListener;
import fr.upem.deadhal.drawers.models.DrawerMainItem;
import fr.upem.deadhal.fragments.EditionFragment;
import fr.upem.deadhal.fragments.FragmentType;
import fr.upem.deadhal.fragments.NavigationFragment;
import fr.upem.deadhal.fragments.OpenFragment;
import fr.upem.deadhal.fragments.SaveFragment;
import fr.upem.deadhal.utils.Storage;

public class MainActivity extends Activity implements DrawerMainListener {

	private Level m_level;

	private DrawerLayout m_drawerLayout;
	private ListView m_drawerList;
	private ActionBarDrawerToggle m_drawerToggle;

	private Fragment m_navigationFragment = new NavigationFragment();
	private Fragment m_editionFragment = new EditionFragment();
	private Fragment m_openFragment = new OpenFragment();
	private Fragment m_saveFragment = new SaveFragment();

	private ArrayList<DrawerMainItem> m_navDrawerItems;
	private DrawerMainListAdapter m_adapter;

	private int m_menu = R.menu.edition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_level = buildSampleLevel();
		buildDrawer(savedInstanceState);
	}

	private void buildDrawer(Bundle savedInstanceState) {
		String[] navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items);

		// nav drawer icons from resources
		TypedArray navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		m_drawerList = (ListView) findViewById(R.id.list_slidermenu);

		m_navDrawerItems = new ArrayList<DrawerMainItem>();

		// adding nav drawer items to array
		// Navigation
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));

		// Edition
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));

		// Open, Will add a counter here
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1), true, String.valueOf(Storage
				.getNbFiles())));

		// Save
		m_navDrawerItems.add(new DrawerMainItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		m_drawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		m_adapter = new DrawerMainListAdapter(getApplicationContext(),
				m_navDrawerItems);
		m_drawerList.setAdapter(m_adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		m_drawerToggle = new ActionBarDrawerToggle(this, m_drawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		m_drawerLayout.setDrawerListener(m_drawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for edition item
			displayView(FragmentType.EDITION);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(FragmentType.values()[position]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(m_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (m_drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = m_drawerLayout.isDrawerOpen(m_drawerList);
		switch (m_menu) {
		case R.menu.navigation:
			menu.findItem(R.id.action_edition).setVisible(!drawerOpen);
			break;
		case R.menu.edition:
			menu.findItem(R.id.action_list_objects).setVisible(!drawerOpen);
			menu.findItem(R.id.action_undo).setVisible(!drawerOpen);
			break;
		case R.menu.open:
			menu.setGroupVisible(R.id.group_open, !drawerOpen);
			menu.findItem(R.id.action_share).setEnabled(!drawerOpen);
			break;
		case R.menu.save:
			break;
		default:
			break;
		}

		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(FragmentType fragmentType) {
		// update the main content by switching fragments
		FragmentManager fragmentManager = getFragmentManager();
		Fragment fragment = null;

		switch (fragmentType) {
		case NAVIGATION:
			fragment = m_navigationFragment;
			m_menu = R.menu.navigation;
			break;
		case EDITION:
			fragment = m_editionFragment;
			m_menu = R.menu.edition;
			break;
		case OPEN:
			fragment = m_openFragment;
			m_menu = R.menu.open;
			break;
		case SAVE:
			fragment = m_saveFragment;
			m_menu = R.menu.save;
			break;
		default:
			break;
		}
		if (fragment != null) {
			if (!fragment.isAdded()) {
				Bundle bundle = new Bundle();
				bundle.putParcelable("level", m_level);
				fragment.setArguments(bundle);

				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();

				// update selected item and title, then close the drawer
				m_drawerList.setItemChecked(fragmentType.ordinal(), true);
				m_drawerList.setSelection(fragmentType.ordinal());
				// setTitle(navMenuTitles[position]);
			}
			m_drawerLayout.closeDrawer(m_drawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		m_drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		m_drawerToggle.onConfigurationChanged(newConfig);
	}

	public void toastNotYetImplemented(MenuItem v) {
		CharSequence text = getString(R.string.not_yet_implemented);
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
		toast.show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("menu", m_menu);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		m_menu = savedInstanceState.getInt("menu");
		invalidateOptionsMenu();
	}

	private Level buildSampleLevel() {
		Level level = new Level("First instruction.");
		level.addRoom(new Room(UUID.randomUUID(), "Welcome", new RectF(6, 11,
				137, 76)));
		level.addRoom(new Room(UUID.randomUUID(), "to", new RectF(165, 26, 218,
				76)));
		level.addRoom(new Room(UUID.randomUUID(), "DeadHal", new RectF(6, 97,
				220, 198)));
		level.addRoom(new Room(UUID.randomUUID(), "!!!", new RectF(75, 219,
				154, 280)));
		level.addRoom(new Room(UUID.randomUUID(), "Swipe left", new RectF(3,
				295, 223, 367)));
		level.addRoom(new Room(UUID.randomUUID(), "to", new RectF(9, 387, 61,
				442)));
		level.addRoom(new Room(UUID.randomUUID(), "begin...", new RectF(91, 381,
				211, 454)));
		return level;
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
	public void onLevelChange(FragmentType type, Level level) {
		m_level = level;
		displayView(type);
	}

	public void toggleDrawerEdition(MenuItem v) {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_edit_layout);
		boolean drawerOpen = drawerLayout.isDrawerOpen(Gravity.END);
		if (drawerOpen) {
			drawerLayout.closeDrawer(Gravity.END);
		} else {
			drawerLayout.openDrawer(Gravity.END);
		}
	}
}
