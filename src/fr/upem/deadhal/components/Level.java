package fr.upem.deadhal.components;

import java.util.LinkedList;
import java.util.List;

import listeners.SelectionRoomListener;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Toast;

public class Level extends View {

	private String title;
	private List<Room> rooms = new LinkedList<Room>();

	private final int NONE = -1;

	private int selectedRoomId = NONE;

	public Room getSelectedRoom() {
		if (selectedRoomId == NONE) {
			return null;
		}
		return rooms.get(selectedRoomId);
	}

	private List<SelectionRoomListener> selectionRoomListeners = new LinkedList<SelectionRoomListener>();

	public Level(Context context) {
		super(context);
	}

	public Level(Context context, String title) {
		super(context);
		this.title = title;
	}

	public boolean addRoom(Room room) {
		for (Room r : rooms) {
			if (r.getRect().intersect(room.getRect())) {
				return false;
			}
		}
		rooms.add(room);

		return true;
	}

	public String getTitle() {
		return title;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void draw(Canvas canvas) {
		for (int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			if (i == selectedRoomId) {
				room.drawSelected(canvas);
			} else {
				room.draw(canvas);
			}
		}
	}

	public void addSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.add(listener);
	}

	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.remove(listener);
	}

	public Room selectRoom(float x, float y) {
		for (int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			if (room.getRect().contains((int) x, (int) y)) {
				if (i == selectedRoomId) {
					for (SelectionRoomListener listener : selectionRoomListeners) {
						listener.onUnselectRoom(room);
					}
					selectedRoomId = -1;
				} else if (selectedRoomId == NONE) {
					for (SelectionRoomListener listener : selectionRoomListeners) {
						listener.onSelectRoom(room);
					}
					selectedRoomId = i;
				}
				return room;
			}
		}
		return null;
	}

}
