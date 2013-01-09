package se.slashat.slashat;

import se.slashat.slashat.adapter.PersonalAdapter;
import se.slashat.slashat.service.PersonalService;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends ListFragment {
	PersonalAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new PersonalAdapter(getActivity(), R.layout.about_item_row,
				PersonalService.getPersonal());

		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_about, container, false);
	}
}