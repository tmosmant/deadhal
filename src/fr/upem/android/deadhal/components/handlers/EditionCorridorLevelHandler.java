package fr.upem.android.deadhal.components.handlers;

import java.util.UUID;

import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.Toast;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.view.TouchEvent;

/**
 * This class handle the level for the edition corridor part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class EditionCorridorLevelHandler extends AbstractLevelHandler {

	private Room m_start;
	private Room m_end;
	private boolean m_directed = false;
	private PointF m_startPoint;
	private PointF m_endPoint;

	/**
	 * Constructs the level handler.
	 * 
	 * @param level
	 *            the level to handle
	 */
	public EditionCorridorLevelHandler(Level level) {
		super(level);
	}

	/**
	 * Return the start room for a new corridor.
	 * 
	 * @return the start room
	 */
	public Room getStart() {
		return m_start;
	}

	/**
	 * Set the start room for a new corridor.
	 * 
	 * @param m_start
	 *            the start room to set
	 */
	public void setStart(Room m_start) {
		this.m_start = m_start;
	}

	/**
	 * Return the end room for a new corridor.
	 * 
	 * @return the end room
	 */
	public Room getEnd() {
		return m_end;
	}

	@Override
	public TouchEvent getProcess(float x, float y) {
		// nothing to do
		return TouchEvent.NONE;
	}

	@Override
	public void endProcess() {
		// nothing to do
	}

	/**
	 * Returns the state of the new corridor construction process.
	 * 
	 * @return true if there is a start and an end room, false otherwise
	 */
	public boolean isAllSet() {
		return m_start != null && m_end != null;
	}

	/**
	 * Set the next room for constructing a new corridor. May be the start or
	 * the end room.
	 * 
	 * @param room
	 *            the room to set
	 * @param point
	 *            the point for the corridor
	 */
	public void setRoom(Room room, PointF point) {
		m_view.getVibrator().vibrate(100);

		if (m_start == null) {
			m_start = room;
			RectF rect = m_start.getRect();
			m_startPoint = new PointF(point.x - rect.left, point.y - rect.top);
		} else if (m_start.equals(room)) {
			Toast.makeText(m_view.getContext(),
					"Corridors are only available between two rooms.",
					Toast.LENGTH_SHORT).show();
			m_start = null;
			m_startPoint = null;
		} else {
			m_end = room;
			RectF rect = m_end.getRect();
			m_endPoint = new PointF(point.x - rect.left, point.y - rect.top);
			Corridor corridor = new Corridor(UUID.randomUUID(),
					m_start.getId(), m_end.getId(), m_directed, m_startPoint,
					m_endPoint);
			m_level.addCorridor(corridor);
			m_start = m_end = null;
			m_startPoint = m_endPoint = null;
		}
		refreshView();
	}

	/**
	 * Returns the directed state.
	 * 
	 * @return the directed state
	 */
	public boolean isDirected() {
		return m_directed;
	}

	/**
	 * Set the directed state.
	 * 
	 * @param directed
	 *            the directed state to set
	 */
	public void setDirected(boolean directed) {
		m_directed = directed;
	}

}
