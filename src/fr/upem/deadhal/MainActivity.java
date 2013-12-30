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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import fr.upem.deadhal.adapter.NavDrawerListAdapter;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.fragments.EditionFragment;
import fr.upem.deadhal.fragments.OpenFragment;
import fr.upem.deadhal.fragments.SaveFragment;
import fr.upem.deadhal.model.NavDrawerItem;
import fr.upem.deadhal.utils.OnDataPass;
import fr.upem.deadhal.utils.Storage;

public class MainActivity extends Activity implements OnDataPass {

	private Level m_level;
	
	private DrawerLayout m_drawerLayout;
	private ListView m_drawerList;
	private ActionBarDrawerToggle m_drawerToggle;

	private Fragment m_editionFragment = new EditionFragment();
	private Fragment m_openFragment = new OpenFragment();
	private Fragment m_saveFragment = new SaveFragment();

	// used to store app title
	private CharSequence m_title;

	// slide menu items
	private String[] m_navMenuTitles;
	private TypedArray m_navMenuIcons;

	private ArrayList<NavDrawerItem> m_navDrawerItems;
	private NavDrawerListAdapter m_adapter;
	private int m_menu = R.menu.edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		m_level = new Level("Copernic, 3rd level");
		buildSampleLevel();

		m_title = getTitle();

		// load slide menu items
		m_navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items);

		// nav drawer icons from resources
		m_navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		m_drawerList = (ListView) findViewById(R.id.list_slidermenu);

		m_navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// New
		m_navDrawerItems.add(new NavDrawerItem(m_navMenuTitles[0],
				m_navMenuIcons.getResourceId(0, -1)));
		// Open, Will add a counter here
		m_navDrawerItems.add(new NavDrawerItem(m_navMenuTitles[1],
				m_navMenuIcons.getResourceId(1, -1), true, String
						.valueOf(Storage.getNbFiles())));

		// Save
		m_navDrawerItems.add(new NavDrawerItem(m_navMenuTitles[2],
				m_navMenuIcons.getResourceId(2, -1)));

		// Recycle the typed array
		m_navMenuIcons.recycle();

		m_drawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		m_adapter = new NavDrawerListAdapter(getApplicationContext(),
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
			// on first time display view for first nav item
			displayView(0);
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
			displayView(position);
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
		case R.menu.edit:
			menu.findItem(R.id.action_new).setVisible(!drawerOpen);
			menu.findItem(R.id.action_remove).setVisible(!drawerOpen);
			menu.findItem(R.id.action_undo).setVisible(!drawerOpen);
			break;
		case R.menu.open:
			menu.findItem(R.id.action_share).setVisible(!drawerOpen);
			menu.findItem(R.id.action_accept).setVisible(!drawerOpen);
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
	private void displayView(int position) {
		// update the main content by switching fragments
		FragmentManager fragmentManager = getFragmentManager();
		Fragment fragment = null;

		boolean justClose = false;

		switch (position) {
		case 0:
			if (m_editionFragment.equals(fragment)) {
				justClose = true;
				break;
			}
			fragment = m_editionFragment;
			m_menu = R.menu.edit;
			break;
		case 1:
			if (m_openFragment.equals(fragment)) {
				justClose = true;
				break;
			}
			fragment = m_openFragment;
			m_menu = R.menu.open;
			break;
		case 2:
			if (m_saveFragment.equals(fragment)) {
				justClose = true;
				break;
			}
			fragment = m_saveFragment;
			m_menu = R.menu.save;
			break;
		default:
			break;
		}
		if (fragment != null) {
			if (justClose) {
				m_drawerLayout.closeDrawer(m_drawerList);
			} else {
				if (!fragment.isAdded()) {
					Bundle bundle = new Bundle();
					bundle.putParcelable("level", m_level);
					fragment.setArguments(bundle);

					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();

					// update selected item and title, then close the drawer
					m_drawerList.setItemChecked(position, true);
					m_drawerList.setSelection(position);
					// setTitle(navMenuTitles[position]);
				}
				m_drawerLayout.closeDrawer(m_drawerList);
			}
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		m_title = title;
		getActionBar().setTitle(m_title);
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

	public void showAddPopup(MenuItem v) {
		View menuItemView = findViewById(R.id.action_new);
		PopupMenu popup = new PopupMenu(this, menuItemView);

		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.add, popup.getMenu());
		OnMenuItemClickListener listener = new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				toastNotYetImplemented(item);
				return false;
			}

		};
		popup.setOnMenuItemClickListener(listener);

		popup.show();
	}

	public void toastNotYetImplemented(MenuItem v) {
		CharSequence text = getString(R.string.not_yet_implemented);
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
		toast.show();
	}

	public void toastDebug(String str) {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), str, duration);
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

	private void buildSampleLevel() {
		m_level.addRoom(new Room(UUID.randomUUID(), "3B117", new RectF(0, 0,
				120, 120)));
		m_level.addRoom(new Room(UUID.randomUUID(), "3B113", new RectF(150, 0,
				150 + 120, 120)));

		m_level.addRoom(new Room(UUID.randomUUID(), "3B116", new RectF(0, 150,
				120, 150 + 120)));
		m_level.addRoom(new Room(UUID.randomUUID(), "3B112", new RectF(150,
				150, 150 + 120, 150 + 120)));
	}

	@Override
	public void incNbFilePass() {
		m_navDrawerItems.get(1).setCount(String
						.valueOf(Storage.getNbFiles()));
		m_adapter.notifyDataSetChanged();
	}

	@Override
	public void onLevelPass(Level level) {
		m_level = level;
	}
}
