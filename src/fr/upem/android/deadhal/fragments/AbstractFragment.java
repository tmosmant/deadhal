package fr.upem.android.deadhal.fragments;

import android.app.Fragment;

/**
 * This class extends the Fragment one by adding a back button handler.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public abstract class AbstractFragment extends Fragment {
	/**
	 * Called when the back button is pressed.
	 * 
	 * @return true if the call have been handled, false otherwise
	 */
	public abstract boolean onBackPressed();
}
