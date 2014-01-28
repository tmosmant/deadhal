package fr.upem.deadhal.fragments;

import fr.upem.deadhal.components.Level;

public interface FragmentObserver {
	public void notifyNbFileChange();
	public void notifyFragmentChange(FragmentType type);
	public void notifyLevelChange(FragmentType type, Level level);
}