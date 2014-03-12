package fr.upem.deadhal.components.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Matrix;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.view.AbstractView;
import fr.upem.deadhal.view.TouchEvent;

public abstract class AbstractLevelHandler {

	protected Level m_level;
	protected List<SelectionRoomListener> selectionRoomListeners = new LinkedList<SelectionRoomListener>();
	protected AbstractView m_view;

	public AbstractLevelHandler(Level level) {
		m_level = level;
	}

	public AbstractView getView() {
		return m_view;
	}

	public void setView(AbstractView view) {
		m_view = view;
	}

	public void addSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.add(listener);
	}

	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.remove(listener);
	}

	public void addRoom(Room room) {
		getLevel().addRoom(room);
		refreshView();
	}

	public void removeRoom(Room room) {
		for (Corridor corridor : m_level.getCorridors().values()) {
			if (room.getId().equals(corridor.getSrc())
					|| room.getId().equals(corridor.getDst())) {
				m_level.removeCorridor(corridor);
			}
		}
		getLevel().removeRoom(room);
		refreshView();
	}

	public void addCorridor(Corridor corridor) {
		m_level.addCorridor(corridor);
	}

	public void removeCorridor(Corridor corridor) {
		m_level.removeCorridor(corridor);
	}

	public Level getLevel() {
		return m_level;
	}

	public abstract TouchEvent getProcess(float x, float y);

	public abstract void endProcess();

	protected void refreshView() {
		if (m_view != null) {
			m_view.refresh();
		}
	}
	
	public Room getRoomFromCoordinates(float x, float y) {
		List<Room> reverseRooms = reverseRooms();
		for (Room room : reverseRooms) {
			Matrix inverse = new Matrix();
			room.getMatrix().invert(inverse);
			float[] pts = { x, y };
			inverse.mapPoints(pts);
			if (room.getRect().contains(pts[0], pts[1])) {
				return room;
			}
		}
		return null;
	}
	
	protected List<Room> reverseRooms() {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);
		return reverseRooms;
	}
}
