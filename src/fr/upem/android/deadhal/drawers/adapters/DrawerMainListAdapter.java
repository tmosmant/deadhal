package fr.upem.android.deadhal.drawers.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.upem.android.deadhal.drawers.models.DrawerMainItem;
import fr.upem.deadhal.R;

/**
 * The adapter for the main drawer.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class DrawerMainListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<DrawerMainItem> m_navDrawerItems;

	/**
	 * Constructs the adapter.
	 * 
	 * @param context
	 *            the context
	 * @param navDrawerItems
	 *            the items
	 */
	public DrawerMainListAdapter(Context context,
			ArrayList<DrawerMainItem> navDrawerItems) {
		this.m_context = context;
		this.m_navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return m_navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return m_navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) m_context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_adapter_main_drawer,
					null);
		}

		ImageView imgIcon = (ImageView) convertView
				.findViewById(R.id.list_adapter_edition_drawer_icon);
		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.list_adapter_edition_drawer_title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

		imgIcon.setImageResource(m_navDrawerItems.get(position).getIcon());
		txtTitle.setText(m_navDrawerItems.get(position).getTitle());

		// displaying count
		// check whether it set visible or not
		if (m_navDrawerItems.get(position).getCounterVisibility()) {
			txtCount.setText(m_navDrawerItems.get(position).getCount());
		} else {
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return convertView;
	}

}
