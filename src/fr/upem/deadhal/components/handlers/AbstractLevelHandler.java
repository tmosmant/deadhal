package fr.upem.deadhal.components.handlers;

import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.view.AbstractView;
import fr.upem.deadhal.view.TouchEvent;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractLevelHandler {

	protected Level m_level;
	protected List<SelectionRoomListener> selectionRoomListeners = new LinkedList<SelectionRoomListener>();
	protected AbstractView m_view;

	public AbstractLevelHandler(Level level) {
		m_level = level;
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
		invalidateView();
	}

	public void removeRoom(Room room) {
		getLevel().removeRoom(room);
		invalidateView();
	}

	public Level getLevel() {
		return m_level;
	}

	public abstract TouchEvent getProcess(float x, float y);

	public abstract void endProcess();

	protected void invalidateView() {
		if (m_view != null) {
			m_view.invalidate();
		}
	}
}
