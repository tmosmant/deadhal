package fr.upem.android.deadhal.components;

import java.util.UUID;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

/**
 * This class is the representation of a corridor.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class Corridor implements Parcelable {

	private UUID id;
	private UUID src;
	private UUID dst;
	private boolean directed;
	private double m_weight;
	private PointF srcPoint;
	private PointF dstPoint;

	/**
	 * Constructs a corridor.
	 * 
	 * @param id
	 *            the id of the corridor
	 * @param src
	 *            the id of the source room
	 * @param dst
	 *            the id of the destination room
	 * @param directed
	 *            true if the corridor is directed (unidirectional), false if
	 *            the corridor is non directed (bidirectional)
	 * @param srcPoint
	 *            the source point
	 * @param dstPoint
	 *            the end point
	 */
	public Corridor(UUID id, UUID src, UUID dst, boolean directed,
			PointF srcPoint, PointF dstPoint) {
		this.id = id;
		this.src = src;
		this.dst = dst;
		this.directed = directed;
		this.srcPoint = srcPoint;
		this.dstPoint = dstPoint;
	}

	/**
	 * Constructs a corridor from a parcel.
	 * 
	 * @param source
	 *            the parcel to construct the corridor from
	 */
	public Corridor(Parcel source) {
		ParcelUuid src = source.readParcelable(ParcelUuid.class
				.getClassLoader());
		this.src = src.getUuid();

		ParcelUuid dst = source.readParcelable(ParcelUuid.class
				.getClassLoader());
		this.dst = dst.getUuid();

		boolean[] val = new boolean[1];
		source.readBooleanArray(val);
		directed = val[0];
	}

	/**
	 * Returns the id of the corridor.
	 * 
	 * @return the id of the corridor
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the id of the source room.
	 * 
	 * @return the id of the source room
	 */
	public UUID getSrc() {
		return src;
	}

	/**
	 * Set the id of the source room.
	 * 
	 * @param src
	 *            the id of the source room
	 */
	public void setSrc(UUID src) {
		this.src = src;
	}

	/**
	 * Returns the id of the destination room.
	 * 
	 * @return the id of the destination room
	 */
	public UUID getDst() {
		return dst;
	}

	/**
	 * Set the id of the destination room.
	 * 
	 * @param dst
	 *            the id of the destination room
	 */
	public void setDst(UUID dst) {
		this.dst = dst;
	}

	/**
	 * Returns the directed state of the corridor.
	 * 
	 * @return true if the corridor is directed (unidirectional), false
	 *         otherwise (bidirectional)
	 */
	public boolean isDirected() {
		return directed;
	}

	/**
	 * Set the directed state of the corridor.
	 * 
	 * @param directed
	 *            the directed state to set
	 */
	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		ParcelUuid srcParcelUuid = new ParcelUuid(src);
		srcParcelUuid.writeToParcel(dest, flags);
		ParcelUuid dstParcelUuid = new ParcelUuid(src);
		dstParcelUuid.writeToParcel(dest, flags);
		dest.writeBooleanArray(new boolean[] { directed });
	}

	public static final Parcelable.Creator<Corridor> CREATOR = new Parcelable.Creator<Corridor>() {
		@Override
		public Corridor createFromParcel(Parcel source) {
			return new Corridor(source);
		}

		@Override
		public Corridor[] newArray(int size) {
			return new Corridor[size];
		}
	};

	/**
	 * Set the weight of the corridor.
	 * 
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		m_weight = weight;
	}

	/**
	 * Returns the weight of the corridor.
	 * 
	 * @return the weight of the corridor
	 */
	public double getWeight() {
		return m_weight;
	}

	/**
	 * Returns the source point of the corridor
	 * 
	 * @return the source point of the corridor
	 */
	public PointF getSrcPoint() {
		return srcPoint;
	}

	/**
	 * Returns the destination point of the corridor.
	 * 
	 * @return the destination point of the corridor
	 */
	public PointF getDstPoint() {
		return dstPoint;
	}
}
