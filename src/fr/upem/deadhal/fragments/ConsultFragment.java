package fr.upem.deadhal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.view.ConsultView;

public class ConsultFragment extends Fragment {
	
	private Level m_level = null;
	private ConsultView m_consultView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_consult, container,
				false);

		getActivity().setTitle(R.string.consult);

		RelativeLayout relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.consult_layout);

		m_level = getArguments().getParcelable("level");

		TextView levelTitleTextView = (TextView) rootView
				.findViewById(R.id.levelTitleTextView);
		levelTitleTextView.setText(m_level.getTitle());

		LevelDrawable levelDrawable = new LevelDrawable(m_level);

		m_consultView = new ConsultView(rootView.getContext(), levelDrawable);
		m_consultView.build(savedInstanceState);
		relativeLayout.addView(m_consultView);

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		m_consultView.saveMatrix(outState);
		super.onSaveInstanceState(outState);
	}
	
}
