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

                                    ImageView badge1 = (ImageView) headerView.findViewById(R.id.badge1);
                                    ProgressBar badge1Progress = (ProgressBar) headerView.findViewById(R.id.badgeprogress1);
                                    ImageView badge2 = (ImageView) headerView.findViewById(R.id.badge2);
                                    ProgressBar badge2Progress = (ProgressBar) headerView.findViewById(R.id.badgeprogress2);
                                    ImageView badge3 = (ImageView) headerView.findViewById(R.id.badge3);
                                    ProgressBar badge3Progress = (ProgressBar) headerView.findViewById(R.id.badgeprogress3);

                                    LinkedList<ImageView> badgeImageViews = new LinkedList<ImageView>();
                                    badgeImageViews.add(badge1);
                                    badgeImageViews.add(badge2);
                                    badgeImageViews.add(badge3);

                                    LinkedList<ProgressBar> badgeProgressBars = new LinkedList<ProgressBar>();
                                    badgeProgressBars.add(badge1Progress);
                                    badgeProgressBars.add(badge2Progress);
                                    badgeProgressBars.add(badge3Progress);

                                    LinkedList<Badge> badges = new LinkedList<Badge>(user.getBadges());

                                    for (int i = 0; i < badges.size(); i++) {
                                        BadgeImageHolder imageHolder = new BadgeImageHolder();
                                        ImageView imageView = badgeImageViews.get(i);
                                        ProgressBar progressBar = badgeProgressBars.get(i);
                                        imageHolder.image = imageView;
                                        imageHolder.imageThumb = imageView;
                                        imageHolder.progressBar = progressBar;
                                        Badge badge = badges.get(i);
                                        ImageService.populateImage(imageHolder, badge.getPicture(), 0);
                                    }


                                    GridView gridView = (GridView) headerView.findViewById(R.id.achivements);

                                    Collection<Achivement> achivements = user.getAchivements();
                                    System.out.println("achicments::::::"+ user.getAchivements().size());

                                    AchivementArrayAdapter achivementArrayAdapter = new AchivementArrayAdapter(new LinkedList(achivements));

                                    gridView.setAdapter(achivementArrayAdapter);


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

        private final LinkedList<Achivement> achivements;

        public AchivementArrayAdapter(LinkedList<Achivement> achivements) {
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

                if (!achivements.get(i).achieved){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        imageView.setAlpha(0.5f);
                    }else{
                        imageView.setVisibility(View.GONE);
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