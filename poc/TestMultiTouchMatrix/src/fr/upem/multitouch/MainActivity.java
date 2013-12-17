package fr.upem.multitouch;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Drawable mDrawable = getResources().getDrawable(R.drawable.ic_launcher);
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(),
				mDrawable.getIntrinsicHeight());
		
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
		TouchView touchView = new TouchView(MainActivity.this, null, 0, mDrawable);
		rLayout.addView(touchView);
	
	}
	
}
