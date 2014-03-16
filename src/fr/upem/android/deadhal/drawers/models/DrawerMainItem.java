package fr.upem.android.deadhal.drawers.models;

/**
 * This class represents an item for the main drawer.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class DrawerMainItem {

	private String m_title;
	private int m_icon;
	private String m_count = "0";
	// boolean to set visiblity of the counter
	private boolean m_isCounterVisible = false;

	/**
	 * Constructs an item.
	 */
	public DrawerMainItem() {
	}

	/**
	 * Constructs an item.
	 * 
	 * @param title
	 *            the item title
	 * @param icon
	 *            the item icon
	 */
	public DrawerMainItem(String title, int icon) {
		this.m_title = title;
		this.m_icon = icon;
	}

	/**
	 * Constructs the item.
	 * 
	 * @param title
	 *            the item title
	 * @param icon
	 *            the item icon
	 * @param isCounterVisible
	 *            the file counter visibility
	 * @param count
	 *            the item count
	 */
	public DrawerMainItem(String title, int icon, boolean isCounterVisible,
			String count) {
		this.m_title = title;
		this.m_icon = icon;
		this.m_isCounterVisible = isCounterVisible;
		this.m_count = count;
	}

	/**
	 * Returns the item title.
	 * 
	 * @return the item title
	 */
	public String getTitle() {
		return this.m_title;
	}

	/**
	 * Returns the item icon.
	 * 
	 * @return the item icon
	 */
	public int getIcon() {
		return this.m_icon;
	}

	/**
	 * Returns the item file count.
	 * 
	 * @return the item file count
	 */
	public String getCount() {
		return this.m_count;
	}

	/**
	 * Returns the counter file visibility.
	 * 
	 * @return the counter file visibility
	 */
	public boolean getCounterVisibility() {
		return this.m_isCounterVisible;
	}

	/**
	 * Set the item title.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.m_title = title;
	}

	/**
	 * Set the item icon.
	 * 
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(int icon) {
		this.m_icon = icon;
	}

	/**
	 * Set the item file count.
	 * 
	 * @param count
	 *            the file count to set
	 */
	public void setCount(String count) {
		this.m_count = count;
	}

	/**
	 * Set the counter visibility.
	 * 
	 * @param isCounterVisible
	 *            the visibility to set
	 */
	public void setCounterVisibility(boolean isCounterVisible) {
		this.m_isCounterVisible = isCounterVisible;
	}
}
