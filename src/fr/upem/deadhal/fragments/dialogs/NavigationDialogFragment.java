package fr.upem.deadhal.fragments.dialogs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import fr.upem.deadhal.components.Room;

/**
 * Created by michael on 09/02/14.
 */
public class NavigationDialogFragment extends DialogFragment {

	public static NavigationDialogFragment newInstance(int title,
			ArrayList<Room> rooms) {
		NavigationDialogFragment dialogFragment = new NavigationDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		bundle.putParcelableArrayList("rooms", rooms);
		dialogFragment.setArguments(bundle);
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");
		ArrayList<Room> rooms = getArguments().getParcelableArrayList("rooms");
		ArrayAdapter<Room> arrayAdapter = new ArrayAdapter<Room>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, rooms);

		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		TextView textViewStart = new TextView(getActivity());
		textViewStart.setText("Start");

		final Spinner spinnerStart = new Spinner(getActivity(),
				Spinner.MODE_DIALOG);
		spinnerStart.setAdapter(arrayAdapter);

		TextView textViewEnd = new TextView(getActivity());
		textViewEnd.setText("End");

		final Spinner spinnerEnd = new Spinner(getActivity(),
				Spinner.MODE_DIALOG);
		spinnerEnd.setAdapter(arrayAdapter);

		linearLayout.addView(textViewStart);
		linearLayout.addView(spinnerStart);
		linearLayout.addView(textViewEnd);
		linearLayout.addView(spinnerEnd);

		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setView(linearLayout)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Room start = (Room) spinnerStart
										.getSelectedItem();
								Room end = (Room) spinnerEnd.getSelectedItem();

								Intent data = new Intent();
								data.putExtra("start", start);
								data.putExtra("end", end);
								getTargetFragment().onActivityResult(
										getTargetRequestCode(),
										Activity.RESULT_OK, data);
							}
						}).create();
	}

}
