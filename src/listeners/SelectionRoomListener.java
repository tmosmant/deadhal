package listeners;

import fr.upem.deadhal.components.Room;

public interface SelectionRoomListener {
	public void onSelectRoom(Room room);

	public void onUnselectRoom(Room room);
}
