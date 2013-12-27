package fr.upem.deadhal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.upem.deadhal.R;

public class OpenFragment extends Fragment {
//	private String[] mFileList;
//	private File mPath = new File(Enviroment.getExternalStorageDirectory() + "//yourdir//");
//	private String mChosenFile;
//	private static final String FTYPE = ".xml";

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
	
//	private void loadFileList() {
//	File m_path = 
//	
//    try {
//        mPath.mkdirs();
//    }
//    catch(SecurityException e) {
//        Log.e(TAG, "unable to write on the sd card " + e.toString());
//    }
//    if(mPath.exists()) {
//        FilenameFilter filter = new FilenameFilter() {
//            public boolean accept(File dir, String filename) {
//                File sel = new File(dir, filename);
//                return filename.contains(FTYPE) || sel.isDirectory();
//            }
//        };
//        mFileList = mPath.list(filter);
//    }
//    else {
//        mFileList= new String[0];
//    }
//}
}
