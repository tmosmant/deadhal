package fr.upem.android.deadhal.drawers.models;

public class DrawerMainItem {
	
	private String m_title;
	private int m_icon;
	private String m_count = "0";
	// boolean to set visiblity of the counter
	private boolean m_isCounterVisible = false;
	
	public DrawerMainItem(){}

	public DrawerMainItem(String title, int icon){
		this.m_title = title;
		this.m_icon = icon;
	}
	
	public DrawerMainItem(String title, int icon, boolean isCounterVisible, String count){
		this.m_title = title;
		this.m_icon = icon;
		this.m_isCounterVisible = isCounterVisible;
		this.m_count = count;
	}
	
	public String getTitle(){
		return this.m_title;
	}
	
	public int getIcon(){
		return this.m_icon;
	}
	
	public String getCount(){
		return this.m_count;
	}
	
	public boolean getCounterVisibility(){
		return this.m_isCounterVisible;
	}
	
	public void setTitle(String title){
		this.m_title = title;
	}
	
	public void setIcon(int icon){
		this.m_icon = icon;
	}
	
	public void setCount(String count){
		this.m_count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.m_isCounterVisible = isCounterVisible;
	}
}
