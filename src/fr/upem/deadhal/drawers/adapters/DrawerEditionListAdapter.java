package fr.upem.deadhal.drawers.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.deadhal.drawers.models.DrawerEditionItem;
import fr.upem.deadhal.view.AbstractView;

public class DrawerEditionListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<DrawerEditionItem> m_navDrawerItems;
	private EditionLevelHandler m_levelHandler;
	private AbstractView m_view;

	public DrawerEditionListAdapter(Context context,
			ArrayList<DrawerEditionItem> navDrawerItems,
			EditionLevelHandler levelHandler, AbstractView view) {
		m_context = context;
		m_navDrawerItems = navDrawerItems;
		m_levelHandler = levelHandler;
		m_view = view;
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
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) m_context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.list_adapter_edition_drawer, null);
		}

		TextView superTitle = (TextView) convertView
				.findViewById(R.id.list_adapter_edition_drawer_super_title);
		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.list_adapter_edition_drawer_title);
		ImageView imgIcon = (ImageView) convertView
				.findViewById(R.id.list_adapter_edition_drawer_icon);

		final DrawerEditionItem item = m_navDrawerItems.get(position);

		if (item.isSuperTitle()) {
			superTitle.setText(item.getTitle());
			txtTitle.setText("");
			imgIcon.setVisibility(View.GONE);
		} else {
			superTitle.setText("");
			txtTitle.setText(item.getTitle());
			imgIcon.setImageResource(R.drawable.ic_action_remove_dark);
			imgIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (item.getType()) {
					case CORRIDOR:
						break;
					case ROOM:
						removeItem(position, parent, item);
					default:
						break;
					}
				}

			});
			imgIcon.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	private void removeItem(final int position, final ViewGroup parent,
			final DrawerEditionItem item) {
		ListView drawerList = (ListView) parent;
		int checkedItemPosition = drawerList.getCheckedItemPosition();
		DrawerEditionItem itemToCheck = null;
		if (checkedItemPosition != AdapterView.INVALID_POSITION
				&& checkedItemPosition != position) {
			itemToCheck = m_navDrawerItems.get(checkedItemPosition);
		}

		m_levelHandler.removeRoom(item.getRoom());
		m_navDrawerItems.remove(position);

		m_view.invalidate();

		DrawerEditionListAdapter.this.notifyDataSetChanged();

		if (itemToCheck == null) {
			m_levelHandler.unselectRoom();
			drawerList.clearChoices();
		} else {
			for (int i = 0; i < m_navDrawerItems.size(); i++) {
				if (itemToCheck == m_navDrawerItems.get(i)) {
					drawerList.setItemChecked(i, true);
					return;
				}
			}
			drawerList.clearChoices();
			m_levelHandler.unselectRoom();
		}

	}
}
