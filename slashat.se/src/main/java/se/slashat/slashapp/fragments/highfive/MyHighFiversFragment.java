package se.slashat.slashapp.fragments.highfive;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.MyHighFiversArrayAdapter;
import se.slashat.slashapp.model.highfive.HighFiver;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.service.HighFiveService;
import se.slashat.slashapp.viewmodel.HighFiverViewModel;

/**
 * Created by nicklas on 9/28/13.
 */
public class MyHighFiversFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myhighfivers_fragment, null);

        HighFiveService.getUser(
                new Callback<User>() {
                    @Override
                    public void call(User user) {
                        Collection<HighFiver> highFivers = user.getHighFivers();
                        ArrayList<HighFiverViewModel> list = new ArrayList<HighFiverViewModel>();

                        for (HighFiver highFiver : highFivers) {
                            HighFiverViewModel highFiverViewModel = new HighFiverViewModel(highFiver);
                            list.add(highFiverViewModel);
                        }

                        MyHighFiversArrayAdapter myHighFiversArrayAdapter = new MyHighFiversArrayAdapter(getActivity(), R.layout.about_list_item_row, list.toArray(new HighFiverViewModel[list.size()]));
                        setListAdapter(myHighFiversArrayAdapter);
                    }
                });
        return view;
    }
}