package fr.upem.deadhal.utils;

import fr.upem.deadhal.components.Level;

public interface OnDataPass {
	public void incNbFilePass();
	public void onLevelPass(Level level);
}