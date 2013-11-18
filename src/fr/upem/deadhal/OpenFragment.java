package fr.upem.deadhal;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OpenFragment extends Fragment {

	public OpenFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_open, container,
				false);

		getActivity().setTitle(R.string.open);
		return rootView;
	}
}
