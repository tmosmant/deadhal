package fr.upem.deadhal.fragments.dialogs;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.upem.deadhal.components.Room;

/**
 * Created by michael on 09/02/14.
 */
public class MultiTouchNavigationDialogFragment extends DialogFragment {

	public static MultiTouchNavigationDialogFragment newInstance(
			int resultCode, int title, int message, ArrayList<Room> rooms) {
		MultiTouchNavigationDialogFragment dialogFragment = new MultiTouchNavigationDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("resultCode", resultCode);
		bundle.putInt("title", title);
		bundle.putInt("message", message);
		bundle.putParcelableArrayList("rooms", rooms);
		dialogFragment.setArguments(bundle);
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int resultCode = getArguments().getInt("resultCode");
		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");
		ArrayList<Room> rooms = getArguments().getParcelableArrayList("rooms");

		ListView listView = new ListView(getActivity());
		ArrayAdapter<Room> arrayAdapter = new ArrayAdapter<Room>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, rooms);
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(buildOnItemClickListener(resultCode));

		return new AlertDialog.Builder(getActivity()).setTitle(title)
				.setMessage(message)
				.setIcon(android.R.drawable.ic_dialog_alert).setView(listView)
				.create();
	}

	private AdapterView.OnItemClickListener buildOnItemClickListener(
			final int resultCode) {
		return new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Room room = (Room) parent.getItemAtPosition(position);
				Intent data = new Intent();
				data.putExtra("room", room);

				dismiss();
				getTargetFragment().onActivityResult(getTargetRequestCode(),
						resultCode, data);
			}
		};
	}
}
