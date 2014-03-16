package fr.upem.android.deadhal.fragments;

/**
 * Enumerate all the possible values for fragments type.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public enum FragmentType {
	NAVIGATION(1), OPEN(2), SAVE(3), EDITION(1), EDITION_CORRIDOR(1);

	private int m_index;

	private FragmentType(int value) {
		m_index = value;
	}

	/**
	 * Returns the index of a type.
	 * 
	 * @return the index of a type
	 */
	public int getIndex() {
		return m_index;
	}
}
