package fr.upem.deadhal.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import fr.upem.deadhal.R;

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
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getTargetFragment().onActivityResult(
										getTargetRequestCode(),
										Activity.RESULT_CANCELED,
										getActivity().getIntent());
							}
						})
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getTargetFragment().onActivityResult(
										getTargetRequestCode(),
										Activity.RESULT_OK,
										getActivity().getIntent());
							}
						}).create();
	}
}
