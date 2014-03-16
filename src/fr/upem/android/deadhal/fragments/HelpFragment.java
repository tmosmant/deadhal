package fr.upem.android.deadhal.fragments;

//public class HelpFragment extends AbstractFragment {
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_help, container,
//				false);
//		getActivity().setTitle(R.string.help);
//
//		TabHost tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
//		tabHost.setup();
//
//		final TabWidget tabWidget = tabHost.getTabWidget();
//		final FrameLayout tabContent = tabHost.getTabContentView();
//
//		// Get the original tab textviews and remove them from the viewgroup.
//		TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
//		for (int index = 0; index < tabWidget.getTabCount(); index++) {
//			originalTextViews[index] = (TextView) tabWidget
//					.getChildTabViewAt(index);
//		}
//		tabWidget.removeAllViews();
//
//		// Ensure that all tab content childs are not visible at startup.
//		for (int index = 0; index < tabContent.getChildCount(); index++) {
//			tabContent.getChildAt(index).setVisibility(View.GONE);
//		}
//
//		// Create the tabspec based on the textview childs in the xml file.
//		// Or create simple tabspec instances in any other way...
//		for (int index = 0; index < originalTextViews.length; index++) {
//			final TextView tabWidgetTextView = originalTextViews[index];
//			final View tabContentView = tabContent.getChildAt(index);
//			TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView
//					.getTag());
//			tabSpec.setContent(new TabHost.TabContentFactory() {
//				@Override
//				public View createTabContent(String tag) {
//					return tabContentView;
//				}
//			});
//			if (tabWidgetTextView.getBackground() == null) {
//				tabSpec.setIndicator(tabWidgetTextView.getText());
//			} else {
//				tabSpec.setIndicator(tabWidgetTextView.getText(),
//						tabWidgetTextView.getBackground());
//			}
//			tabHost.addTab(tabSpec);
//		}
//
//		return rootView;
//	}
//
//	@Override
//	public boolean onBackPressed() {
//		return false;
//	}
//}

/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.upem.android.deadhal.MainActivity;
import fr.upem.deadhal.R;

public class HelpFragment extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
	 * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
	 * derivative, which will destroy and re-create fragments as needed, saving and restoring their
	 * state in the process. This is important to conserve memory and is a best practice when
	 * allowing navigation between objects in a potentially large collection.
	 */
	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the object collection.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_collection_demo);

		// Create an adapter that when requested, will return a fragment representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must use
		// getSupportFragmentManager.
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());

		// Set up action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home button should show an "Up" caret, indicating that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This is called when the Home (Up) button is pressed in the action bar.
				// Create a simple intent that starts the hierarchical parent activity and
				// use NavUtils in the Support Package to ensure proper handling of Up.
				Intent upIntent = new Intent(this, MainActivity.class);
				if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
					// This activity is not part of the application's task, so create a new task
					// with a synthesized back stack.
					TaskStackBuilder.from(this)
							// If there are ancestor activities, they should be added here.
							.addNextIntent(upIntent)
							.startActivities();
					finish();
				} else {
					// This activity is part of the application's task, so simply
					// navigate up to the hierarchical parent activity.
					NavUtils.navigateUpTo(this, upIntent);
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
	 * representing an object in the collection.
	 */
	public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new DemoObjectFragment();
			Bundle args = new Bundle();
			args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// For this contrived example, we have a 100-object collection.
			return 100;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "OBJECT " + (position + 1);
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DemoObjectFragment extends Fragment {

		public static final String ARG_OBJECT = "object";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1)).setText(
					Integer.toString(args.getInt(ARG_OBJECT)));
			return rootView;
		}
	}
}
