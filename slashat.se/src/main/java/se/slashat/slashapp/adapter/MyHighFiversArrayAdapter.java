package se.slashat.slashapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import se.slashat.slashapp.R;
import se.slashat.slashapp.service.ImageService;
import se.slashat.slashapp.viewmodel.HighFiverViewModel;
import se.slashat.slashapp.viewmodel.ViewModelBase;

/**
 * Created by nicklas on 10/1/13.
 */
public class MyHighFiversArrayAdapter extends AbstractArrayAdapter<ViewModelBase<?>>{

    public MyHighFiversArrayAdapter(Context context, int layoutResourceId, ViewModelBase[] data) {
        super(context, layoutResourceId, R.layout.section, data);
    }

    public static class MyHighFiversHolder extends ImageAsyncHolder {
        TextView name;
        TextView username;
    }

    @Override
    public Holder createHolder(View row) {
        MyHighFiversHolder myHighFiversHolder = new MyHighFiversHolder();
        myHighFiversHolder.name = (TextView) row.findViewById(R.id.txtName);
        myHighFiversHolder.username = (TextView) row.findViewById(R.id.title);
        myHighFiversHolder.imageThumb = (ImageView) row.findViewById(R.id.imgIcon);
        myHighFiversHolder.progressBar = (ProgressBar) row.findViewById(R.id.imageviewprogress);

        return myHighFiversHolder;
    }

    @Override
    public View.OnClickListener createOnClickListener(ViewModelBase highFiverViewModel) {
        return null;
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void setDataOnHolder(Holder holder, ViewModelBase highFiverViewModel, int position) {
        MyHighFiversHolder h = (MyHighFiversHolder) holder;
        HighFiverViewModel vm = (HighFiverViewModel) highFiverViewModel;

        h.name.setText(vm.getModel().getUsername());
        h.username.setText("");

        if (vm.getModel().getPicture() != null){
            ImageService.populateImage(h, vm.getModel().getPicture().toString(), position);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            Holder holder = (Holder) convertView.getTag();
            if (holder instanceof ImageAsyncHolder) {
                if (holder != null) {
                    ImageAsyncHolder h = (ImageAsyncHolder) holder;
                    if (h.loadImageAsyncTask != null) {
                        h.loadImageAsyncTask.cancel();
                    }
                }
            }
        }
        return super.getView(position, convertView, parent);
    }
}
