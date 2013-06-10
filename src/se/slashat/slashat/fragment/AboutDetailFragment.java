package se.slashat.slashat.fragment;

import se.slashat.slashat.Callback;
import se.slashat.slashat.R;
import se.slashat.slashat.adapter.PersonAdapter;
import se.slashat.slashat.adapter.PersonalAdapter;
import se.slashat.slashat.model.Personal;
import se.slashat.slashat.service.PersonalService;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AboutDetailFragment extends ListFragment implements Callback<Personal> {
	// ArrayAdapter<Personal> adapter;

	public static final String ADAPTER = "adapter";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;

		ArrayAdapter<Personal> adapter = null;

		if (bundle != null) {
			adapter = (ArrayAdapter<Personal>) bundle.getSerializable(ADAPTER);
		}

		// If no adapter is found in the bundle create a new one with all
		// people.
		if (adapter != null) {
			setListAdapter(adapter);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_aboutlist, container, false);
	}

	@Override
	public void call(Personal personal) {
		PersonAdapter p = new PersonAdapter(getActivity(), R.layout.about_detail, new Personal[] { personal });

		Bundle bundle = new Bundle();
		bundle.putSerializable(ADAPTER, p);

		AboutDetailFragment aboutFragment = new AboutDetailFragment();
		aboutFragment.setArguments(bundle);
		FragmentSwitcher.getInstance().switchFragment(aboutFragment, true);
	}
}