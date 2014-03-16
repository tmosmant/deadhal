package fr.upem.android.deadhal.drawers.listeners;

import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.fragments.FragmentType;

/**
 * This class interfaces the main drawer listener
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public interface DrawerMainListener {

	/**
	 * Called when the file number changes.
	 */
	public void onFileNumberChange();

	/**
	 * Called when the fragment changes.
	 * 
	 * @param type
	 *            the new fragment.
	 */
	public void onFragmentChange(FragmentType type);

	/**
	 * Called when the level changes.
	 * 
	 * @param level
	 *            the new level.
	 */
	public void onLevelChange(Level level);
}