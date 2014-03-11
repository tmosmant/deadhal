package fr.upem.deadhal.fragments.dialogs;

import java.util.ArrayList;
import java.util.Random;

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
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;

public class NavigationDialogFragment extends DialogFragment {

	private Spinner m_spinnerStart;
	private int m_spinnerStartPosition = 0;
	private Spinner m_spinnerEnd;
	private int m_spinnerEndPosition = 1;
	private static NavigationLevelHandler m_levelHandler;

	public static NavigationDialogFragment newInstance(int title,
			NavigationLevelHandler levelHandler, ArrayList<Room> rooms) {
		NavigationDialogFragment.m_levelHandler = levelHandler;
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
		Room localisationRoom = m_levelHandler.getLocalisationRoom();
		int lastIndexOf = rooms.lastIndexOf(localisationRoom);
		if (lastIndexOf != -1) {
			m_spinnerStartPosition = lastIndexOf;
		}
		m_spinnerStart.setSelection(m_spinnerStartPosition);

		TextView textViewEnd = new TextView(getActivity());
		textViewEnd.setText("End");

		m_spinnerEnd = new Spinner(getActivity(), Spinner.MODE_DIALOG);
		m_spinnerEnd.setAdapter(arrayAdapter);
		Random rand = new Random();
		do {
			m_spinnerEndPosition = rand.nextInt(rooms.size());
		} while (m_spinnerStartPosition == m_spinnerEndPosition);
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
