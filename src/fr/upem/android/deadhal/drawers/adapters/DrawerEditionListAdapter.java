package fr.upem.android.deadhal.drawers.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.upem.android.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.android.deadhal.drawers.models.DrawerEditionItem;
import fr.upem.android.deadhal.drawers.models.DrawerEditionItem.Type;
import fr.upem.android.deadhal.fragments.EditionFragment;
import fr.upem.android.deadhal.fragments.dialogs.RoomOptionsDialogFragment;
import fr.upem.android.deadhal.view.AbstractView;
import fr.upem.deadhal.R;

/**
 * The adapter for the edition drawer.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class DrawerEditionListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<DrawerEditionItem> m_navDrawerItems;
	private EditionLevelHandler m_levelHandler;
	private AbstractView m_view;
	private EditionFragment m_editionFragment;

	/**
	 * Constructs the adapter.
	 * 
	 * @param context
	 *            the context
	 * @param navDrawerItems
	 *            the items
	 * @param levelHandler
	 *            the level handler
	 * @param view
	 *            the view
	 * @param editionFragment
	 *            the edition fragment
	 */
	public DrawerEditionListAdapter(Context context,
			ArrayList<DrawerEditionItem> navDrawerItems,
			EditionLevelHandler levelHandler, AbstractView view,
			EditionFragment editionFragment) {
		m_context = context;
		m_navDrawerItems = navDrawerItems;
		m_levelHandler = levelHandler;
		m_view = view;
		m_editionFragment = editionFragment;
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
			if (item.getType() == Type.CORRIDOR) {
				imgIcon.setImageResource(R.drawable.ic_action_remove_dark);
			} else {
				imgIcon.setImageResource(R.drawable.ic_action_expand);

			}
			imgIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (item.getType()) {
					case CORRIDOR:
						removeItem(position, item);
						break;
					case ROOM:
						DialogFragment dialogFragment = RoomOptionsDialogFragment
								.newInstance(item.getRoom().getName(), item
										.getRoom().getId());
						dialogFragment.setTargetFragment(m_editionFragment,
								EditionFragment.OPTION_DIALOG);
						dialogFragment.show(m_editionFragment
								.getFragmentManager().beginTransaction(),
								"roomOptionsDialog");
					default:
						break;
					}
				}

			});
			imgIcon.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	/**
	 * Remove an item from the list.
	 * 
	 * @param position
	 *            the item's position
	 * @param item
	 *            the item to remove
	 */
	private void removeItem(final int position, final DrawerEditionItem item) {
		switch (item.getType()) {
		case CORRIDOR:
			m_levelHandler.removeCorridor(item.getCorridor());
			m_levelHandler.unselectRoom();
			m_levelHandler.unselectCorridor();
			break;
		case ROOM:
			m_levelHandler.unselectCorridor();
			break;
		default:
			break;
		}
		m_editionFragment.updateDrawer();
		m_view.refresh();
	}
}
