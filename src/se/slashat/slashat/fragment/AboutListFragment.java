package se.slashat.slashat.fragment;

import java.util.ArrayList;

import se.slashat.slashat.Callback;
import se.slashat.slashat.R;
import se.slashat.slashat.adapter.PersonalAdapter;
import se.slashat.slashat.model.Personal;
import se.slashat.slashat.model.Personal.Type;
import se.slashat.slashat.service.PersonalService;
import se.slashat.slashat.viewmodel.PersonalViewModel;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Nicklas Löf
 * 
 */

public class AboutListFragment extends ListFragment implements Callback<PersonalViewModel> {
	// ArrayAdapter<Personal> adapter;

	public static final String ADAPTER = "adapter";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PersonalAdapter adapter = null;

			
			Personal[] crew = PersonalService.getPersonal(Type.HOST);
			Personal[] assistant = PersonalService.getPersonal(Type.ASSISTANT);
			Personal[] dev = PersonalService.getPersonal(Type.DEV);
			
			ArrayList<PersonalViewModel> arrayList = new ArrayList<PersonalViewModel>();
			
			arrayList.add(new PersonalViewModel(null, "Programledare"));
			for (int i = 0; i < crew.length; i++) {
				arrayList.add(new PersonalViewModel(crew[i], ""));
			}
			
			arrayList.add(new PersonalViewModel(null, "Medarbetare"));
			for (int i = 0; i < assistant.length; i++) {
				arrayList.add(new PersonalViewModel(assistant[i], ""));
			}
			
			arrayList.add(new PersonalViewModel(null, "Team Slashat Devops"));
			for (int i = 0; i < dev.length; i++) {
				arrayList.add(new PersonalViewModel(dev[i], ""));
			}
			
			PersonalViewModel[] array = arrayList.toArray(new PersonalViewModel[arrayList.size()]);
			
			
			adapter = new PersonalAdapter(getActivity(), R.layout.about_item_row, array, this);

			setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_aboutlist, container, false);
	}

	@Override
	public void call(PersonalViewModel personal) {
		// If we want to show something when someone in the list is clicked.
			
	}
}