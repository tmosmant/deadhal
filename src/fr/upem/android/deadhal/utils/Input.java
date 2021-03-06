package fr.upem.android.deadhal.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Provides static input methods.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class Input {

	/**
	 * Hide the keyboard.
	 * 
	 * @param activity
	 *            the activity
	 * @param view
	 *            the view
	 */
	public static void hideKeyboard(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

}
