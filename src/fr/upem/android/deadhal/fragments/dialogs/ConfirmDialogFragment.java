package fr.upem.android.deadhal.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import fr.upem.deadhal.R;

public class ConfirmDialogFragment extends DialogFragment {
	
	public static ConfirmDialogFragment newInstance(int title, int message) {
		ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		bundle.putInt("message", message);
		dialogFragment.setArguments(bundle);
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");

		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setIcon(R.drawable.ic_action_warning)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
						                                     getActivity().getIntent());
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).create();
	}
}
