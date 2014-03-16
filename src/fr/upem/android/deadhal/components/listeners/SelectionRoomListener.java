package fr.upem.android.deadhal.components.listeners;

import fr.upem.android.deadhal.components.Room;

/**
 * Listener for toggling the selection of a room.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public interface SelectionRoomListener {
	/**
	 * Notify the selection of a room.
	 * 
	 * @param room
	 *            the selected room
	 */
	public void onSelectRoom(Room room);

	/**
	 * Notify the unselection of a room
	 * 
	 * @param room
	 *            the unselected room
	 */
	public void onUnselectRoom(Room room);
}
