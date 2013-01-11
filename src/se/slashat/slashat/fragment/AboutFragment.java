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

public class AboutFragment extends ListFragment implements Callback<Personal> {
	ArrayAdapter<Personal> adapter;
	private FragmentSwitcher fragmentSwitcher;

	// TODO: replace with default constructor and setArguments
	/**
	 * Create a new AboutFragment without an adapter. This will show all the
	 * persons in a clickable list
	 * 
	 * @param fragmentSwitcher
	 */
	public AboutFragment(FragmentSwitcher fragmentSwitcher) {
		this.fragmentSwitcher = fragmentSwitcher;
	}

	// TODO: replace with default constructor and setArguments
	/**
	 * Creates a new AboutFragment with an adapter already set. Used for showing
	 * details about a person without the need to have another Fragment class.
	 * 
	 * @param fragmentSwitcher
	 * @param adapter
	 */
	public AboutFragment(FragmentSwitcher fragmentSwitcher,
			ArrayAdapter<Personal> adapter) {
		this.fragmentSwitcher = fragmentSwitcher;
		this.adapter = adapter;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If the AboutFragment was called without an adapter create the one
		// that shows all persons.
		if (adapter == null) {
			adapter = new PersonalAdapter(getActivity(),
					R.layout.about_item_row, PersonalService.getPersonal(),
					this);
		}
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_about, container, false);
	}

	@Override
	public void call(Personal personal) {
		PersonAdapter p = new PersonAdapter(getActivity(),
				R.layout.about_detail, new Personal[] { personal });
		// Create a new AboutFragment with an adapter already set
		this.fragmentSwitcher.switchFragment(new AboutFragment(
				fragmentSwitcher, p), true);

	}
}