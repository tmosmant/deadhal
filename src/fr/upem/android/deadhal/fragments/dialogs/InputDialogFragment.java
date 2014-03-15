package fr.upem.android.deadhal.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class InputDialogFragment extends DialogFragment {

	private EditText m_editText = null;
	private String m_strId = new String();

	public static InputDialogFragment newInstance(String title, String strId) {
		InputDialogFragment renameDialogFragment = new InputDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("id", strId);
		renameDialogFragment.setArguments(bundle);
		return renameDialogFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_editText = new EditText(getActivity());
		if (savedInstanceState != null) {
			String m_inputText = savedInstanceState.getString("inputText");
			m_strId = savedInstanceState.getString("id");
			m_editText.setText(m_inputText);
			m_editText.setSelection(m_inputText.length());
		}
		m_editText.setSingleLine(true);
		m_editText.setOnEditorActionListener(editorActionListener());
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		final String strId = getArguments().getString("id");
		m_strId = strId;
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setView(m_editText)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								Editable value = m_editText.getText();
								passNewName(value.toString(), strId);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

	private OnEditorActionListener editorActionListener() {
		return new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					dismiss();
					hideKeyboard();
					Editable value = m_editText.getText();
					passNewName(value.toString(), m_strId);
					return true;
				}
				return false;
			}
		};
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(m_editText.getWindowToken(), 0);
	}

	private void passNewName(String name, String strId) {
		Intent data = new Intent();
		data.putExtra("inputText", name);
		data.putExtra("id", strId);

		getTargetFragment().onActivityResult(getTargetRequestCode(),
				Activity.RESULT_OK, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("id", m_strId);
		if (m_editText != null) {
			outState.putString("inputText", m_editText.getText().toString());
		}
	}
}
