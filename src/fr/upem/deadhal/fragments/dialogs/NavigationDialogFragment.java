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

	private Spinner m_spinnerStart;
	private int m_spinnerStartPosition = 0;
	private Spinner m_spinnerEnd;
	private int m_spinnerEndPosition = 1;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			m_spinnerStartPosition = savedInstanceState.getInt("startPosition");
			m_spinnerEndPosition = savedInstanceState.getInt("endPosition");
		}
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

		m_spinnerStart = new Spinner(getActivity(), Spinner.MODE_DIALOG);
		m_spinnerStart.setAdapter(arrayAdapter);
		m_spinnerStart.setSelection(m_spinnerStartPosition);

		TextView textViewEnd = new TextView(getActivity());
		textViewEnd.setText("End");

		m_spinnerEnd = new Spinner(getActivity(), Spinner.MODE_DIALOG);
		m_spinnerEnd.setAdapter(arrayAdapter);
		m_spinnerEnd.setSelection(m_spinnerEndPosition);

		linearLayout.addView(textViewStart);
		linearLayout.addView(m_spinnerStart);
		linearLayout.addView(textViewEnd);
		linearLayout.addView(m_spinnerEnd);

		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setView(linearLayout)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Room start = (Room) m_spinnerStart
										.getSelectedItem();
								Room end = (Room) m_spinnerEnd
					.getSelectedItem();

								Intent data = new Intent();
	data.putExtra("start", start);
								data.putExtra("end", end);
								getTargetFragment().onActivityResult(
										getTargetRequestCode(),
										Activity.RESULT_OK, data);
							}
						}).create();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("startPosition",
				m_spinnerStart.getSelectedItemPosition());
		outState.putInt("endPosition", m_spinnerEnd.getSelectedItemPosition());
	}
}
