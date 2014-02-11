package fr.upem.deadhal.tasks;

import java.util.*;

import android.os.AsyncTask;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;

public class ShortestPathTask extends AsyncTask<Level, Integer, List<UUID>> {
	
	private UUID m_start;
	private UUID m_finish;
	
	public ShortestPathTask(UUID start, UUID finish) {
		this.m_start = start;
		this.m_finish = finish;
	}
	
	@Override
	protected List<UUID> doInBackground(Level... params) {
		Level m_level = params[0];
		return dijkstra(m_level);
	}

	private List<UUID> dijkstra(Level level) {
		float startFloat = 0;
		Map<UUID, Float> dijkstraMap = initMap(level);
		Map<UUID, UUID> corridorsRoom = new HashMap<UUID, UUID>();

		dijkstraMap.put(m_start, startFloat);
		Room lastRoom = level.getRooms().get(m_start);
		List<UUID> checkRoom = new ArrayList<UUID>();
		checkRoom.addAll(dijkstraMap.keySet());
		checkRoom.remove(lastRoom.getId());
		while (lastRoom != null) {
			for (UUID id : lastRoom.getNeighbors().keySet()) {
				float weight = level.getCorridors().get(id).computeWeight();
				if (dijkstraMap.get(lastRoom.getNeighbors().get(id)) < 0.0
						|| dijkstraMap.get(lastRoom.getNeighbors().get(id)) > weight
								+ dijkstraMap.get(lastRoom.getId())) {
					dijkstraMap.put(lastRoom.getNeighbors().get(id), weight
							+ dijkstraMap.get(lastRoom.getId()));
					corridorsRoom.put(lastRoom.getNeighbors().get(id), id);
				}
			}
			UUID id = lighter(dijkstraMap,					checkRoom);
			if (id == null) {
				lastRoom = null;
			} else {
				lastRoom = level.getRooms().get(id);
			}
			checkRoom.remove(id);
		}
		return parcours(level, m_start, m_finish, corridorsRoom);
	}

	private Map<UUID, Float> initMap(Level level) {
		float initFloat = -1;
		Map<UUID, Float> hm = new HashMap<UUID, Float>();
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

	private UUID lighter(Map<UUID, Float> hm, List<UUID> al) {
		UUID room = null;
		float min = -1;
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
