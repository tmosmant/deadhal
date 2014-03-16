package fr.upem.android.deadhal.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import fr.upem.deadhal.R;

import java.io.IOException;
import java.io.InputStream;

public class AboutFragment extends AbstractFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		getActivity().setTitle(R.string.about);

		ImageView imageView = (ImageView) rootView.findViewById(R.id.thomas);
		try {
			InputStream ims = getActivity().getAssets().open("thomas.jpg");
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);
			// set image to ImageView
			imageView.setImageDrawable(d);
		} catch (IOException e) {
			e.printStackTrace();
		}

		imageView = (ImageView) rootView.findViewById(R.id.michael);
		try {
			InputStream ims = getActivity().getAssets().open("michael.jpg");
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);
			// set image to ImageView
			imageView.setImageDrawable(d);
		} catch (IOException e) {
			e.printStackTrace();
		}

		imageView = (ImageView) rootView.findViewById(R.id.vincent);
		try {
			InputStream ims = getActivity().getAssets().open("vincent.jpg");
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);
			// set image to ImageView
			imageView.setImageDrawable(d);
		} catch (IOException e) {
			e.printStackTrace();
		}

		imageView = (ImageView) rootView.findViewById(R.id.fazal);
		try {
			InputStream ims = getActivity().getAssets().open("fazal.jpg");
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);
			// set image to ImageView
			imageView.setImageDrawable(d);
		} catch (IOException e) {
			e.printStackTrace();
		}


		return rootView;
	}

	@Override
	public boolean onBackPressed() {
		return true;
	}
}
