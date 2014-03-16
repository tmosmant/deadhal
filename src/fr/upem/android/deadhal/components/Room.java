package fr.upem.android.deadhal.components;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

/**
 * This class is the representation of a room.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class Room implements Parcelable {

	public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {

		@Override
		public Room createFromParcel(Parcel source) {
			return new Room(source);
		}

		@Override
		public Room[] newArray(int size) {
			return new Room[size];
		}
	};
	private UUID id;
	private String name;
	private RectF rect;
	private Map<UUID, UUID> neighbors = new HashMap<UUID, UUID>();

	/**
	 * Constructs a room.
	 * 
	 * @param id
	 *            the room id
	 * @param name
	 *            the room name
	 * @param rect
	 *            the room rectangle
	 */
	public Room(UUID id, String name, RectF rect) {
		this.id = id;
		this.name = name;
		this.rect = rect;
	}

	/**
	 * Constructs a room from a parcel.
	 * 
	 * @param source
	 *            the parcel to construct the room from
	 */
	public Room(Parcel source) {
		ParcelUuid id = source
				.readParcelable(ParcelUuid.class.getClassLoader());
		this.id = id.getUuid();
		this.name = source.readString();
		float left = source.readFloat();
		float right = source.readFloat();
		float top = source.readFloat();
		float bottom = source.readFloat();
		this.rect = new RectF(left, top, right, bottom);
		source.readMap(neighbors, HashMap.class.getClass().getClassLoader());
	}

	/**
	 * Returns the room name.
	 * 
	 * @return the room name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the room name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the rect of the room.
	 * 
	 * @return the rect of the room
	 */
	public RectF getRect() {
		return rect;
	}

	/**
	 * Returns the room id.
	 * 
	 * @return the room id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the map of neighbors for this room.
	 * 
	 * @return the map of neighbors for this room
	 */
	public Map<UUID, UUID> getNeighbors() {
		return neighbors;
	}

	/**
	 * Add a neighbor for this room.
	 * 
	 * @param corridor
	 *            the corridor joining the two neighbors
	 * @param room
	 *            the neighbor
	 */
	public void addNeighbor(UUID corridor, UUID room) {
		neighbors.put(corridor, room);
	}

	/**
	 * Returns if a room is accessible.
	 * 
	 * @param dst
	 *            the room to check
	 * @return true if the room is accessible, false otherwise
	 */
	public boolean isAccessible(Room dst) {
		return neighbors.containsValue(dst.getId());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		ParcelUuid parcelUuid = new ParcelUuid(id);
		parcelUuid.writeToParcel(dest, flags);
		dest.writeString(name);
		dest.writeFloat(rect.left);
		dest.writeFloat(rect.right);
		dest.writeFloat(rect.top);
		dest.writeFloat(rect.bottom);
		dest.writeMap(neighbors);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Room)) {
			return false;
		}

		Room room = (Room) o;
		return id.equals(room.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
