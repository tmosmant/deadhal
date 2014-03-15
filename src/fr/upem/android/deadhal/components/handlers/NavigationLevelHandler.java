package fr.upem.android.deadhal.components.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.android.deadhal.graphics.drawable.Pawn;
import fr.upem.android.deadhal.view.TouchEvent;
import fr.upem.deadhal.R;

public class NavigationLevelHandler extends AbstractLevelHandler {

	private static final int SHORT_DELAY = 2000;

	private Room m_roomStart;
	private Room m_roomEnd;
	private Room m_selectedRoom;
	private List<UUID> m_shortestPath = new ArrayList<UUID>();
	private long m_timestampError = 0;
	private Pawn m_pawn = new Pawn(this);

	public NavigationLevelHandler(Level level) {
		super(level);
	}

	public Room getRoomStart() {
		return m_roomStart;
	}

	public void setRoomStart(Room room) {
		m_roomStart = room;
	}

	public Room getRoomEnd() {
		return m_roomEnd;
	}

	public void setRoomEnd(Room room) {
		m_roomEnd = room;
	}

	@Override
	public TouchEvent getProcess(float x, float y) {
		return null;
	}

	@Override
	public void endProcess() {
	}

	public List<UUID> getShortestPath() {
		return m_shortestPath;
	}

	public void setShortestPath(List<UUID> m_path) {
		this.m_shortestPath = m_path;
	}

	public Room getSelectedRoom() {
		return m_selectedRoom;
	}

	public void selectRoom(Room room) {
		if (room != null) {
			m_selectedRoom = room;
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onSelectRoom(room);
			}
		} else {
			m_selectedRoom = null;
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onUnselectRoom(room);
			}
		}
	}

	public void moveWithSensor(float x, float y) {

		for (UUID corridorId : m_selectedRoom.getNeighbors().keySet()) {
			Corridor corridor = m_level.getCorridors().get(corridorId);
			if (m_level.getRooms().get(corridor.getSrc())
					.equals(m_selectedRoom)) {
				Room roomStart = m_level.getRooms().get(corridor.getSrc());
				RectF rectStart = roomStart.getRect();
				Room roomEnd = m_level.getRooms().get(corridor.getDst());
				RectF rectEnd = roomEnd.getRect();

				PointF srcPoint = new PointF(corridor.getSrcPoint().x
						+ rectStart.left, corridor.getSrcPoint().y
						+ rectStart.top);
				PointF dstPoint = new PointF(corridor.getDstPoint().x
						+ rectEnd.left, corridor.getDstPoint().y + rectEnd.top);

				PointF pStart = computeIntersection(srcPoint, dstPoint,
						rectStart);
				PointF pEnd = computeIntersection(srcPoint, dstPoint, rectEnd);

				RectF hitbox = new RectF(pStart.x - 14, pStart.y - 14,
						pStart.x + 38, pStart.y + 38);

				if (hitbox.contains(x, y)) {
					selectRoom(roomEnd);
					m_pawn.slide(pEnd.x, pEnd.y);
					return;

				}
			} else if (m_level.getRooms().get(corridor.getDst())
					.equals(m_selectedRoom)) {
				Log.v("deadhal", "test");
				Room roomStart = m_level.getRooms().get(corridor.getSrc());
				RectF rectStart = roomStart.getRect();
				Room roomEnd = m_level.getRooms().get(corridor.getDst());
				RectF rectEnd = roomEnd.getRect();

				PointF srcPoint = new PointF(corridor.getSrcPoint().x
						+ rectStart.left, corridor.getSrcPoint().y
						+ rectStart.top);
				PointF dstPoint = new PointF(corridor.getDstPoint().x
						+ rectEnd.left, corridor.getDstPoint().y + rectEnd.top);

				PointF pStart = computeIntersection(srcPoint, dstPoint,
						rectStart);
				PointF pEnd = computeIntersection(srcPoint, dstPoint, rectEnd);

				RectF hitbox = new RectF(pEnd.x - 14, pEnd.y - 14, pEnd.x + 38,
						pEnd.y + 38);

				if (hitbox.contains(x, y)) {
					selectRoom(roomStart);
					m_pawn.slide(pStart.x, pStart.y);
					return;

				}
			}
		}

		m_pawn.slide(x, y);
	}

	public boolean move(float x, float y) {
		return move(x, y, true);
	}

	public boolean move(float x, float y, boolean mustSlide) {
		Room room = getRoomFromCoordinates(x, y);
		if (room != null && m_selectedRoom != null) {
			if (room.equals(m_selectedRoom)) {
				m_pawn.handleMove(x, y);
				return true;
			} else {
				for (Corridor corridor : m_level.getCorridors().values()) {
					if (corridor.getSrc().equals(m_selectedRoom.getId())
							&& corridor.getDst().equals(room.getId())) {
						selectRoom(room);
						m_pawn.handleMove(x, y);
						return true;
					}
					if (!corridor.isDirected()) {
						if (corridor.getDst().equals(m_selectedRoom.getId())
								&& corridor.getSrc().equals(room.getId())) {
							selectRoom(room);
							m_pawn.handleMove(x, y);
							return true;
						}
					}
				}
				if (mustPrintError()) {
					Toast.makeText(m_view.getContext(),
							R.string.this_move_isn_t_possible,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		if (mustSlide) {
			m_pawn.slide(x, y);
		}
		return false;
	}

	private boolean mustPrintError() {
		long currentTimeMillis = System.currentTimeMillis();
		if (m_timestampError == 0
				|| m_timestampError + SHORT_DELAY < currentTimeMillis) {
			m_timestampError = currentTimeMillis;
			return true;
		}
		return false;
	}

	public Pawn getPawn() {
		return m_pawn;
	}
}
