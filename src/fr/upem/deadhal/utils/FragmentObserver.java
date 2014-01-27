package fr.upem.deadhal.utils;

import fr.upem.deadhal.components.Level;

public interface FragmentObserver {
	public void notifyNbFileChange();
	public void notifyFragmentChange(Position dest);
	public void notifyLevelChange(Position dest, Level level);
}