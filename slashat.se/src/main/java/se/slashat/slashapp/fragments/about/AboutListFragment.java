package se.slashat.slashapp.fragments.about;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.AboutAdapter;
import se.slashat.slashapp.model.Personal;
import se.slashat.slashapp.model.SectionModel;
import se.slashat.slashapp.model.highfive.HighFiver;
import se.slashat.slashapp.service.HighFiveService;
import se.slashat.slashapp.service.PersonalService;
import se.slashat.slashapp.viewmodel.AboutViewModel;
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
    private static Callback<Personal> callback;

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
        populate();
    }

    private void populate() {



        HighFiveService.getAllHighfivers(new Callback<Collection<HighFiver>>() {
            @Override
            public void call(Collection<HighFiver> result) {
                AboutAdapter adapter = null;
                Personal[] show = PersonalService.getPersonal(Personal.Type.SHOW);
                Personal[] crew = PersonalService.getPersonal(Personal.Type.HOST);
                Personal[] assistant = PersonalService.getPersonal(Personal.Type.ASSISTANT);
                Personal[] dev = PersonalService.getPersonal(Personal.Type.DEV);


                ArrayList<ViewModelBase> arrayList = new ArrayList<ViewModelBase>();

                arrayList.add(new SectionViewModel(new SectionModel("Om showen")));
                for (int i = 0; i < show.length; i++) {
                    arrayList.add(new AboutViewModel(show[i]));
                }

                arrayList.add(new SectionViewModel(new SectionModel("Programledare")));
                for (int i = 0; i < crew.length; i++) {
                    arrayList.add(new AboutViewModel(crew[i]));
                }

                arrayList.add(new SectionViewModel(new SectionModel("Medarbetare")));
                for (int i = 0; i < assistant.length; i++) {
                    arrayList.add(new AboutViewModel(assistant[i]));
                }

                arrayList.add(new SectionViewModel(new SectionModel("Team Slashat Devops")));
                for (int i = 0; i < dev.length; i++) {
                    arrayList.add(new AboutViewModel(dev[i]));
                }

                arrayList.add(new SectionViewModel(new SectionModel("Highfivers")));

                for (HighFiver allHighfiver : result) {
                    arrayList.add(new AboutViewModel(allHighfiver));
                }

                ViewModelBase<?>[] array = arrayList.toArray(new ViewModelBase<?>[arrayList.size()]);


                adapter = new AboutAdapter(getActivity(), R.layout.about_list_item_row, array, AboutListFragment.this);

                setListAdapter(adapter);

            }
        },false);


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

        callback.call(result);
    }

    public static void setCallback(Callback<Personal> callback) {
        AboutListFragment.callback = callback;
    }
}