package fr.upem.deadhal.drawers.listeners;

import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.fragments.FragmentType;

public interface DrawerMainListener {

	public void onFileNumberChange();

	public void onFragmentChange(FragmentType type);

	public void onLevelChange(Level level);
}