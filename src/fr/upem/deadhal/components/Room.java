package fr.upem.deadhal.components;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

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

	public Room(UUID id, String name, RectF rect) {
		this.id = id;
		this.name = name;
		this.rect = rect;
	}

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

	public String getName() {
		return name;
	}

	public RectF getRect() {
		return rect;
	}

	public UUID getId() {
		return id;
	}

	public Map<UUID, UUID> getNeighbors() {
		return neighbors;
	}

	public void addNeighbor(UUID corridor, UUID room) {
		neighbors.put(corridor, room);
	}

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
