package fr.upem.android.deadhal.components.listeners;

import fr.upem.android.deadhal.components.Room;

public interface SelectionRoomListener {
	public void onSelectRoom(Room room);

	public void onUnselectRoom(Room room);
}
