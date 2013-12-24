package fr.upem.deadhal;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import fr.upem.deadhal.multitouch.GestureListener;
import fr.upem.deadhal.multitouch.TouchView;

public class EditionFragment extends Fragment {

	public EditionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit, container,
				false);

		getActivity().setTitle(R.string.edit);

		Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		RelativeLayout rLayout = (RelativeLayout) rootView
				.findViewById(R.id.edit_layout);

		TouchView touchView = new TouchView(rootView.getContext(), drawable);
		GestureDetector gestureDetector = new GestureDetector(
				rootView.getContext(), new GestureListener(touchView));
		touchView.build(gestureDetector);

		rLayout.addView(touchView);

		return rootView;
	}
}
