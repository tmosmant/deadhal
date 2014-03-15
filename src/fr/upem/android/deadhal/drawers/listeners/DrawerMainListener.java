package fr.upem.android.deadhal.drawers.listeners;

import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.fragments.FragmentType;

public interface DrawerMainListener {

	public void onFileNumberChange();

	public void onFragmentChange(FragmentType type);

	public void onLevelChange(Level level);
}