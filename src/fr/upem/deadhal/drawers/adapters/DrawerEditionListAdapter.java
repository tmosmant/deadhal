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
import fr.upem.deadhal.drawers.models.DrawerEditionItem;

public class DrawerEditionListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<DrawerEditionItem> m_navDrawerItems;

	public DrawerEditionListAdapter(Context context,
			ArrayList<DrawerEditionItem> navDrawerItems) {
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
			convertView = mInflater.inflate(
					R.layout.list_adapter_edition_drawer, null);
		}

		TextView superTitle = (TextView) convertView
				.findViewById(R.id.superTitle);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);

		DrawerEditionItem item = m_navDrawerItems.get(position);

		if (item.isSuperTitle()) {
			superTitle.setText(item.getTitle());
			imgIcon.setVisibility(View.GONE);
		} else {
			txtTitle.setText(item.getTitle());
			imgIcon.setImageResource(R.drawable.ic_action_remove_dark);
		}

		return convertView;
	}

}
