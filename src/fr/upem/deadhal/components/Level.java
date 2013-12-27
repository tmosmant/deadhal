package fr.upem.deadhal.components;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;

public class Level implements Parcelable {

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

	public Level(String title) {
		this.title = title;
	}
	
	public Level(Parcel source) {
		this.title = source.readString();
		source.readList(rooms, (Object.class.getClassLoader()));
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
			if (i != selectedRoomId) {
				room.draw(canvas);
			}
		}
		if (selectedRoomId != NONE) {
			rooms.get(selectedRoomId).drawSelected(canvas);
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
					selectedRoomId = NONE;
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

	public void unselectRoom() {
		selectedRoomId = NONE;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeList(rooms);
	}

	public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {
		@Override
		public Level createFromParcel(Parcel source) {
			return new Level(source);
		}

		@Override
		public Level[] newArray(int size) {
			return new Level[size];
		}
	};

}
