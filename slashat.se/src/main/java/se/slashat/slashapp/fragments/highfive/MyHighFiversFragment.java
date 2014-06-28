package se.slashat.slashapp.fragments.highfive;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.AbstractArrayAdapter;
import se.slashat.slashapp.adapter.MyHighFiversArrayAdapter;
import se.slashat.slashapp.model.SectionModel;
import se.slashat.slashapp.model.highfive.Achivement;
import se.slashat.slashapp.model.highfive.Badge;
import se.slashat.slashapp.model.highfive.HighFiver;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.service.HighFiveService;
import se.slashat.slashapp.service.ImageService;
import se.slashat.slashapp.viewmodel.HighFiverViewModel;
import se.slashat.slashapp.viewmodel.SectionViewModel;
import se.slashat.slashapp.viewmodel.ViewModelBase;

/**
 * Created by nicklas on 9/28/13.
 */
public class MyHighFiversFragment extends ListFragment {

    private View view;
    private View headerView;
    private boolean headerPopulated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myhighfivers_fragment, null);
        headerView = inflater.inflate(R.layout.badge_list_view, null);

        //populate(true);
        return view;
    }

    private void populate(boolean reload) {
        HighFiveService.getUser(
                new Callback<User>() {
                    @Override
                    public void call(User user) {
                        if (user == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            AlertDialog alertDialog = builder.setMessage("Kunde inte ta emot data från servern. Detta kan bero på dålig nätanslutning men ifall du loggat in med samma konto på en annan enhet måste du för tillfället ominstallera denna app för att logga in igen")
                                    .setCancelable(true).create();
                            alertDialog.show();
                        } else {
                            Collection<HighFiver> highFivers = user.getHighFivers();
                            if (!highFivers.isEmpty()) {
                                ArrayList<ViewModelBase> list = new ArrayList<ViewModelBase>();

                                list.add(new SectionViewModel(new SectionModel("Mina "+user.getHighFivers().size()+" High-Fivers:")));

                                for (HighFiver highFiver : highFivers) {
                                    HighFiverViewModel highFiverViewModel = new HighFiverViewModel(highFiver);
                                    list.add(highFiverViewModel);
                                }

                                list.add(new SectionViewModel(new SectionModel("")));

                                if (!headerPopulated) {

                                    GridView achivmentGridView = (GridView) headerView.findViewById(R.id.achivements);
                                    GridView badgesView = (GridView) headerView.findViewById(R.id.badges);

                                    Collection<Achivement> achivements = user.getAchivements();
                                    Collection<Badge> badges = user.getBadges();

                                    AchivementArrayAdapter achivementArrayAdapter = new AchivementArrayAdapter(new LinkedList(achivements));
                                    AchivementArrayAdapter badgesArrayAdatper = new AchivementArrayAdapter(new LinkedList<Badge>(badges));


                                    achivmentGridView.setAdapter(achivementArrayAdapter);
                                    badgesView.setAdapter(badgesArrayAdatper);


                                    headerPopulated = true;
                                    getListView().addHeaderView(headerView, null, false);
                                }

                                MyHighFiversArrayAdapter myHighFiversArrayAdapter = new MyHighFiversArrayAdapter(getActivity(), R.layout.about_list_item_row, list.toArray(new ViewModelBase[list.size()]));
                                setListAdapter(myHighFiversArrayAdapter);

                            }
                        }
                    }
                }, reload);
    }

    public class AchivementArrayAdapter extends BaseAdapter{

        private final LinkedList<Badge> achivements;

        public AchivementArrayAdapter(LinkedList<Badge> achivements) {
            this.achivements = achivements;
        }

        @Override
        public int getCount() {
            return achivements.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new GridView.LayoutParams(getPx(70), getPx(70)));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(0, 0, 0, 0);

                ProgressBar progressBar = new ProgressBar(getActivity());

                BadgeImageHolder imageHolder = new BadgeImageHolder();
                imageHolder.imageThumb = imageView;
                imageHolder.image = imageView;
                imageHolder.progressBar = progressBar;
                Badge badge = achivements.get(i);
                if (badge instanceof Achivement) {
                    if (!((Achivement)badge).isAchieved()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            imageView.setAlpha(0.5f);
                        } else {
                            imageView.setVisibility(View.GONE);
                        }
                    }
                }

                ImageService.populateImage(imageHolder, achivements.get(i).getPicture(), i);

                return imageView;
            }
    }

    public class BadgeImageHolder extends AbstractArrayAdapter.ImageAsyncHolder{

    }

    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            if (HighFiveService.hasToken()) {
                populate(true);
            }
        } catch (Exception e) {
            //supress errors
            e.printStackTrace();
        }
    }
}