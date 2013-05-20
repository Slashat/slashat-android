package se.slashat.slashat.fragment;

import se.slashat.slashat.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/*
 * This class is just a wrapper fragment using different archive_fragment layouts depending on the device layout.
 * Bigger screens gets a layout with both list and details in the same layout while smaller devices only gets the list
 * and then the details are switched on top of it.
 */
public class ArchiveFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_archive, container, false);
	}
}
