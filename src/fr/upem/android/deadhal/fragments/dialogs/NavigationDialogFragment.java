package fr.upem.android.deadhal.fragments.dialogs;

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
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.R;

/**
 * This class is the navigation dialog used to select a route.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class NavigationDialogFragment extends DialogFragment {

	private Spinner m_spinnerStart;
	private int m_spinnerStartPosition = 0;
	private Spinner m_spinnerEnd;
	private int m_spinnerEndPosition = 1;
	private static NavigationLevelHandler m_levelHandler;

	/**
	 * Constructs the navigation dialog.
	 * 
	 * @param levelHandler
	 *            the level handler
	 * @param rooms
	 *            the rooms
	 * @return
	 */
	public static NavigationDialogFragment newInstance(
			NavigationLevelHandler levelHandler, ArrayList<Room> rooms) {
		NavigationDialogFragment.m_levelHandler = levelHandler;
		NavigationDialogFragment dialogFragment = new NavigationDialogFragment();
		Bundle bundle = new Bundle();
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
		ArrayList<Room> rooms = getArguments().getParcelableArrayList("rooms");
		ArrayAdapter<Room> arrayAdapter = new ArrayAdapter<Room>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, rooms);

		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		TextView textViewStart = new TextView(getActivity());
		textViewStart.setText(R.string.from_);
		textViewStart.setPadding(15, 10, 0, 0);

		m_spinnerStart = new Spinner(getActivity(), Spinner.MODE_DIALOG);
		m_spinnerStart.setAdapter(arrayAdapter);
		Room selectedRoom = m_levelHandler.getSelectedRoom();
		int lastIndexOf = rooms.lastIndexOf(selectedRoom);
		if (lastIndexOf != -1) {
			m_spinnerStartPosition = lastIndexOf;
		}
		m_spinnerStart.setSelection(m_spinnerStartPosition);

		TextView textViewEnd = new TextView(getActivity());
		textViewEnd.setText(R.string.to_);
		textViewEnd.setPadding(15, 0, 0, 0);

		m_spinnerEnd = new Spinner(getActivity(), Spinner.MODE_DIALOG);
		m_spinnerEnd.setAdapter(arrayAdapter);
		m_spinnerEndPosition = 1;
		if (m_spinnerStartPosition == 1) {
			m_spinnerEndPosition = 0;
		}
		m_spinnerEnd.setSelection(m_spinnerEndPosition);

		linearLayout.addView(textViewStart);
		linearLayout.addView(m_spinnerStart);
		linearLayout.addView(textViewEnd);
		linearLayout.addView(m_spinnerEnd);

		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.select_route)
				.setView(linearLayout)
				.setIcon(R.drawable.ic_action_directions)
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
						}).setNegativeButton(R.string.cancel, null).create();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("startPosition",
				m_spinnerStart.getSelectedItemPosition());
		outState.putInt("endPosition", m_spinnerEnd.getSelectedItemPosition());
	}
}
