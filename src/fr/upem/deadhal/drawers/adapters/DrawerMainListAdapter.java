package fr.upem.deadhal.drawers.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.drawers.models.DrawerMainItem;

public class DrawerMainListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<DrawerMainItem> m_navDrawerItems;

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
			convertView = mInflater.inflate(R.layout.list_adapter_edition_drawer, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
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
