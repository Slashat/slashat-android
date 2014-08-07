package se.slashat.slashapp.fragments.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.fragments.FragmentSwitcher;
import se.slashat.slashapp.model.Crew;

/**
 * Created by nicklas on 6/18/13.
 */
public class AboutFragment extends Fragment implements Callback<Crew> {
    private static View view;
    private AboutListFragment aboutListFragment;
    private AboutDetailFragment aboutDetailFragment;
    private boolean isDualPane;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.about_fragment, null);
        }catch (InflateException e) {

        }

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        aboutListFragment = (AboutListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.about_list_fragment);
        aboutDetailFragment = (AboutDetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.aboutdetailfragment);

        View detailView = view.findViewById(R.id.aboutdetailfragment);

        isDualPane = detailView != null && detailView.getVisibility() == View.VISIBLE;

        aboutListFragment.setCallback(this);
}

    @Override
    public void call(Crew result) {

        if (isDualPane){
            Bundle bundle = new Bundle();
            bundle.putSerializable("person", result);

            aboutDetailFragment = new AboutDetailFragment();
            aboutDetailFragment.setArguments(bundle);

            FragmentSwitcher.getInstance().switchFragment(aboutDetailFragment, true, R.id.aboutdetailfragment);
        }else{
            Intent intent = new Intent(this.getActivity(), AboutDetailActivity.class);
            intent.putExtra("person", result);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter,R.anim.exit);
        }

    }


}
