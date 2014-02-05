package fr.upem.deadhal.fragments;

public enum FragmentType {
	NAVIGATION(1), OPEN(2), SAVE(3), EDITION(1);

	private int m_index;

	private FragmentType(int value) {
		m_index = value;
	}

	public int getIndex() {
		return m_index;
	}
}
