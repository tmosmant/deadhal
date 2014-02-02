package fr.upem.deadhal.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class NavigationDialogFragment extends DialogFragment {

	public static NavigationDialogFragment newInstance(int title, int message) {
		NavigationDialogFragment dialogFragment = new NavigationDialogFragment();
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
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getTargetFragment().onActivityResult(
										getTargetRequestCode(), 0,
										getActivity().getIntent());
							}
						})
				.setNeutralButton("Start",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getTargetFragment().onActivityResult(
										getTargetRequestCode(), 1,
										getActivity().getIntent());
							}
						})
				.setPositiveButton("End", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getTargetFragment().onActivityResult(
								getTargetRequestCode(), 2,
								getActivity().getIntent());
					}
				}).create();
	}
}
