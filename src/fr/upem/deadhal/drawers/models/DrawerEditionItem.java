package fr.upem.deadhal.drawers.models;

public class DrawerEditionItem {

	private String m_title;
	private boolean m_isSuperTitle;

	public DrawerEditionItem(String title, boolean isSuperTitle) {
		this.m_title = title;
		this.m_isSuperTitle = isSuperTitle;
	}

	public String getTitle() {
		return this.m_title;
	}

	public boolean isSuperTitle() {
		return m_isSuperTitle;
	}
}
