package fr.upem.android.deadhal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.upem.deadhal.R;

public class HelpFragment extends AbstractFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_help, container,
		                                 false);
		getActivity().setTitle(R.string.help);
		return rootView;
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}
}
