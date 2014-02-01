package fr.upem.deadhal.drawers.adapters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
	private Set<Integer> m_disabledPositions = new HashSet<Integer>();

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
	public boolean isEnabled(int position) {
		if (m_disabledPositions.contains(position)) {
			return false;
		}
		return super.isEnabled(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) m_context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_adapter_main_drawer,
					null);
		}

		TextView superTitle = (TextView) convertView
				.findViewById(R.id.superTitle);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);

		if (m_navDrawerItems.get(position).isSuperTitle()) {
			superTitle.setText(m_navDrawerItems.get(position).getTitle());
			imgIcon.setVisibility(View.GONE);
			m_disabledPositions.add(position);
		} else {
			txtTitle.setText(m_navDrawerItems.get(position).getTitle());
			imgIcon.setImageResource(R.drawable.ic_action_remove);
		}

		return convertView;
	}

}
