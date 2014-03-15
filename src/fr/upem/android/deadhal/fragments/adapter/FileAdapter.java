package fr.upem.android.deadhal.fragments.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.upem.deadhal.R;

import java.io.File;
import java.util.List;

public class FileAdapter extends ArrayAdapter<File> {

	private final int resource;
	private final Activity activity;
	private final List<File> files;

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

		return listView;
	}

	public void removeSelection() {
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView fileName;
	}

}
