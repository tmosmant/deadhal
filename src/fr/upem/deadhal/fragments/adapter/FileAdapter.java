package fr.upem.deadhal.fragments.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.upem.deadhal.R;

public class FileAdapter extends ArrayAdapter<File> {

	private final int resource;
	private final Activity activity;
	private final List<File> files;
	private int m_selectedItemId = -1;

	public FileAdapter(Activity activity, int resource, List<File> files) {
		super(activity, resource, files);
		this.resource = resource;
		this.activity = activity;
		this.files = files;
	}

	@Override
	public int getCount() {
		return files.size();
	}

	@Override
	public File getItem(int position) {
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public boolean hasCheckedItem() {
		return m_selectedItemId != -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View listView = convertView;

		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			listView = inflater.inflate(resource, null);

			holder = new ViewHolder();
			holder.fileName = (TextView) listView.findViewById(R.id.file_name);

			listView.setTag(holder);
		} else {
			holder = (ViewHolder) listView.getTag();
		}

		File file = files.get(position);
		holder.fileName.setText(file.getName());

		if (m_selectedItemId == position) {
			listView.setBackgroundColor(activity.getResources().getColor(
					android.R.color.holo_blue_light));
		} else {
			listView.setBackgroundColor(Color.TRANSPARENT);
		}

		return listView;
	}

	public void toggleSelection(int position) {
		if (m_selectedItemId == position) {
			unselectView(position);
		} else {
			selectView(position);
		}
	}

	public void removeSelection() {
		m_selectedItemId = -1;
		notifyDataSetChanged();
	}

	public void selectView(int position) {
		removeSelection();
		m_selectedItemId = position;
		notifyDataSetChanged();
	}

	public void unselectView(int position) {
		removeSelection();
		m_selectedItemId = -1;
		notifyDataSetChanged();
	}

	public int getSelectedId() {
		return m_selectedItemId;
	}

	private static class ViewHolder {
		TextView fileName;
	}

}
