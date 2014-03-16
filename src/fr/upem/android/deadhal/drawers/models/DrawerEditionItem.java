package fr.upem.android.deadhal.drawers.models;

import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Room;

/**
 * This class represents an item for the edition drawer.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class DrawerEditionItem {

	private String m_title;
	private Room m_room;
	private Corridor m_corridor;
	private Type m_type;

	/**
	 * Constructs an item.
	 * 
	 * @param room
	 *            the item room
	 */
	public DrawerEditionItem(Room room) {
		m_room = room;
		m_title = room.getName();
		m_type = Type.ROOM;
	}

	/**
	 * Constructs an item.
	 * 
	 * @param title
	 *            the item title
	 * @param type
	 *            the item type
	 */
	public DrawerEditionItem(String title, Type type) {
		m_title = title;
		m_type = type;
	}

	/**
	 * Construcs an item.
	 * 
	 * @param corridor
	 *            the corridor item
	 * @param title
	 *            the title item
	 */
	public DrawerEditionItem(Corridor corridor, String title) {
		m_corridor = corridor;
		m_title = title;
		m_type = Type.CORRIDOR;
	}

	/**
	 * Returns the item title.
	 * 
	 * @return the item title
	 */
	public String getTitle() {
		return m_title;
	}

	/**
	 * Returns the super title state of the item.
	 * 
	 * @return the super title state of the item
	 */
	public boolean isSuperTitle() {
		return m_type == Type.ADD_CORRIDOR || m_type == Type.ADD_ROOM;
	}

	/**
	 * Returns the item room.
	 * 
	 * @return the item room.
	 */
	public Room getRoom() {
		return m_room;
	}

	/**
	 * Returns the item type.
	 * 
	 * @return the item type
	 */
	public Type getType() {
		return m_type;
	}

	/**
	 * Returns the item corridor.
	 * 
	 * @return the item corridor
	 */
	public Corridor getCorridor() {
		return m_corridor;
	}

	/**
	 * Enumerate all the possible values for the item type.
	 * 
	 * @author fbousry mremy tmosmant vfricotteau
	 * 
	 */
	public enum Type {
		ADD_ROOM, ROOM, ADD_CORRIDOR, CORRIDOR
	}
}
