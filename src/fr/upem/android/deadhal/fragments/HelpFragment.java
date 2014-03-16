package fr.upem.android.deadhal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import fr.upem.deadhal.R;

public class HelpFragment extends AbstractFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_help, container,
				false);
		getActivity().setTitle(R.string.help);

		TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);
		tabHost.setup();

		// Tab Edition
		TabHost.TabSpec specTabEdtion=tabHost.newTabSpec("tabEdition");
		specTabEdtion.setContent(R.id.tabEdition);
		specTabEdtion.setIndicator(getResources().getString(R.string.edition));

		// Tab Navigation
		TabHost.TabSpec specTabNavigation=tabHost.newTabSpec("tabNavigation");
		specTabNavigation.setIndicator(getResources().getString(R.string.navigation_trunc));
		specTabNavigation.setContent(R.id.tabNavigation);

		// Tab Save
		TabHost.TabSpec specTabSave=tabHost.newTabSpec("tabSave");
		specTabSave.setIndicator(getResources().getString(R.string.save));
		specTabSave.setContent(R.id.tabSave);

		// Tab Open
		TabHost.TabSpec specTabOpen=tabHost.newTabSpec("tabOpen");
		specTabOpen.setIndicator(getResources().getString(R.string.open));
		specTabOpen.setContent(R.id.tabOpen);

		// Add all tab
		tabHost.addTab(specTabEdtion);
		tabHost.addTab(specTabNavigation);
		tabHost.addTab(specTabSave);
		tabHost.addTab(specTabOpen);

		// final TabWidget tabWidget = tabHost.getTabWidget();
		// final FrameLayout tabContent = tabHost.getTabContentView();
		//
		// // Get the original tab textviews and remove them from the viewgroup.
		// TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
		// for (int index = 0; index < tabWidget.getTabCount(); index++) {
		// originalTextViews[index] = (TextView) tabWidget
		// .getChildTabViewAt(index);
		// }
		// tabWidget.removeAllViews();
		//
		// // Ensure that all tab content childs are not visible at startup.
		// for (int index = 0; index < tabContent.getChildCount(); index++) {
		// tabContent.getChildAt(index).setVisibility(View.GONE);
		// }
		//
		// // Create the tabspec based on the textview childs in the xml file.
		// // Or create simple tabspec instances in any other way...
		// for (int index = 0; index < originalTextViews.length; index++) {
		// final TextView tabWidgetTextView = originalTextViews[index];
		// final View tabContentView = tabContent.getChildAt(index);
		// TabHost.TabSpec tabSpec = tabHost.newTabSpec((String)
		// tabWidgetTextView
		// .getTag());
		// tabSpec.setContent(new TabHost.TabContentFactory() {
		// @Override
		// public View createTabContent(String tag) {
		// return tabContentView;
		// }
		// });
		// if (tabWidgetTextView.getBackground() == null) {
		// tabSpec.setIndicator(tabWidgetTextView.getText());
		// } else {
		// tabSpec.setIndicator(tabWidgetTextView.getText(),
		// tabWidgetTextView.getBackground());
		// }
		// tabHost.addTab(tabSpec);
		// }

		return rootView;
	}

	@Override
	public boolean onBackPressed() {
		return true;
	}
}
