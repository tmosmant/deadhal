package fr.upem.deadhal.utils;

import fr.upem.deadhal.components.Level;

public interface OnDataPass {
	public void nbFilePass();
	public void onEditPass();
	public void onLevelPass(int menu, Level level);
}