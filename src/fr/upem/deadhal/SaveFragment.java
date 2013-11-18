package fr.upem.deadhal;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SaveFragment extends Fragment {

	public SaveFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_save, container,
				false);

		getActivity().setTitle(R.string.save);
		return rootView;
	}
}
