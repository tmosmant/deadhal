package fr.upem.android.deadhal.fragments.dialogs;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import fr.upem.deadhal.R;

public class RoomOptionsDialogFragment extends DialogFragment {

	public static RoomOptionsDialogFragment newInstance(String title, UUID id) {
		RoomOptionsDialogFragment dialogFragment = new RoomOptionsDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("id", id.toString());
		dialogFragment.setArguments(bundle);
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		final String strId = getArguments().getString("id");

		String[] options = getResources().getStringArray(R.array.room_options);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1, options);

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				getActivity());
		builderSingle.setTitle(title);
		builderSingle.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						passSelectedOption(which, strId);
					}
				});
		return builderSingle.create();
	}

	private void passSelectedOption(int which, String strId) {
		Intent data = new Intent();
		data.putExtra("option", which);
		data.putExtra("id", strId);

		getTargetFragment().onActivityResult(getTargetRequestCode(),
				Activity.RESULT_OK, data);
	}
}
