package fr.upem.android.deadhal.tasks;

import java.util.*;

import android.os.AsyncTask;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;

/**
 * This class is in charge of computing the shortest path between two rooms.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class ShortestPathTask extends AsyncTask<Level, Integer, List<UUID>> {

	private UUID m_start;
	private UUID m_finish;

	/**
	 * Constructs the task.
	 * 
	 * @param start
	 *            the id of the start room
	 * @param finish
	 *            the id of the end room
	 */
	public ShortestPathTask(UUID start, UUID finish) {
		m_start = start;
		m_finish = finish;
	}

	@Override
	protected List<UUID> doInBackground(Level... params) {
		Level level = params[0];
		return dijkstra(level);
	}

	private List<UUID> dijkstra(Level level) {
		Double startFloat = 0.0;
		Map<UUID, Double> dijkstraMap = initMap(level);
		Map<UUID, UUID> corridorsRoom = new HashMap<UUID, UUID>();

		dijkstraMap.put(m_start, startFloat);
		Room lastRoom = level.getRooms().get(m_start);
		List<UUID> checkRoom = new ArrayList<UUID>();
		checkRoom.addAll(dijkstraMap.keySet());
		checkRoom.remove(lastRoom.getId());
		while (lastRoom != null) {
			for (UUID id : lastRoom.getNeighbors().keySet()) {
				double weight = level.getCorridors().get(id).getWeight();
				if (dijkstraMap.get(lastRoom.getNeighbors().get(id)) < 0.0
						|| dijkstraMap.get(lastRoom.getNeighbors().get(id)) > weight
								+ dijkstraMap.get(lastRoom.getId())) {
					dijkstraMap.put(lastRoom.getNeighbors().get(id), weight
							+ dijkstraMap.get(lastRoom.getId()));
					corridorsRoom.put(lastRoom.getNeighbors().get(id), id);
				}
			}
			UUID id = lighter(dijkstraMap, checkRoom);
			if (id == null) {
				lastRoom = null;
			} else {
				lastRoom = level.getRooms().get(id);
			}
			checkRoom.remove(id);
		}
		return parcours(level, m_start, m_finish, corridorsRoom);
	}

	private Map<UUID, Double> initMap(Level level) {
		Double initFloat = -1.0;
		Map<UUID, Double> hm = new HashMap<UUID, Double>();
		for (UUID id : level.getRooms().keySet()) {
			hm.put(id, initFloat);
		}
		return hm;
	}

	private List<UUID> parcours(Level level, UUID start, UUID finish,
			Map<UUID, UUID> corridorsRoom) {
		List<UUID> listCorridor = new LinkedList<UUID>();
		while (!finish.equals(start)) {
			if (!corridorsRoom.containsKey(finish)) {
				return null;
			}
			listCorridor.add(corridorsRoom.get(finish));
			Corridor corridor = level.getCorridors().get(
					corridorsRoom.get(finish));
			if (corridor.getDst().equals(finish)) {
				finish = corridor.getSrc();
			} else {
				finish = corridor.getDst();
			}
		}
		Collections.reverse(listCorridor);
		return listCorridor;
	}

	private UUID lighter(Map<UUID, Double> hm, List<UUID> al) {
		UUID room = null;
		Double min = -1.0;
		if (al.size() > 0) {
			for (UUID id : al) {
				if (hm.get(id) != -1.0 && (hm.get(id) < min || min == -1.0)) {
					min = hm.get(id);
					room = id;
				}
			}
		}
		return room;
	}

}
