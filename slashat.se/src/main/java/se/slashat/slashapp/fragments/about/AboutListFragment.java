package se.slashat.slashapp.fragments.about;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.PersonalAdapter;
import se.slashat.slashapp.fragments.episode.EpisodeDetailFragment;
import se.slashat.slashapp.model.Personal;
import se.slashat.slashapp.model.SectionModel;
import se.slashat.slashapp.service.PersonalService;
import se.slashat.slashapp.viewmodel.PersonalViewModel;
import se.slashat.slashapp.viewmodel.SectionViewModel;
import se.slashat.slashapp.viewmodel.ViewModelBase;

/**
 * Created by nicklas on 6/19/13.
 */
public class AboutListFragment extends ListFragment implements Callback<Personal> {
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().findViewById(R.id.dualpane) != null) {
            mTwoPane = true;
        }
        populate();
    }

    private void populate() {
        PersonalAdapter adapter = null;

        Personal[] crew = PersonalService.getPersonal(Personal.Type.HOST);
        Personal[] assistant = PersonalService.getPersonal(Personal.Type.ASSISTANT);
        Personal[] dev = PersonalService.getPersonal(Personal.Type.DEV);

        ArrayList<ViewModelBase> arrayList = new ArrayList<ViewModelBase>();

        arrayList.add(new SectionViewModel(new SectionModel("Programledare")));
        for (int i = 0; i < crew.length; i++) {
            arrayList.add(new PersonalViewModel(crew[i]));
        }

        arrayList.add(new SectionViewModel(new SectionModel("Medarbetare")));
        for (int i = 0; i < assistant.length; i++) {
            arrayList.add(new PersonalViewModel(assistant[i]));
        }

        arrayList.add(new SectionViewModel(new SectionModel("Team Slashat Devops")));
        for (int i = 0; i < dev.length; i++) {
            arrayList.add(new PersonalViewModel(dev[i]));
        }

        ViewModelBase<?>[] array = arrayList.toArray(new ViewModelBase<?>[arrayList.size()]);


        adapter = new PersonalAdapter(getActivity(), R.layout.about_list_item_row, array, this);

        setListAdapter(adapter);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void call(Personal result) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("person",result);

        AboutDetailFragment aboutDetailFragment = new AboutDetailFragment();
        aboutDetailFragment.setArguments(bundle);
        if (mTwoPane){
            getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.aboutdetailfragment, aboutDetailFragment).commit();
        }else{
            getFragmentManager().beginTransaction().addToBackStack(null).setCustomAnimations(R.anim.slider_in, R.anim.noanimation, R.anim.noanimation, R.anim.slider_out).replace(R.id.aboutdetailfragment, aboutDetailFragment).commit();
        }
    }
}