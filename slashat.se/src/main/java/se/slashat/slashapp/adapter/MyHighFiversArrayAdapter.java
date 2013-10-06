package se.slashat.slashapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import se.slashat.slashapp.R;
import se.slashat.slashapp.service.ImageService;
import se.slashat.slashapp.viewmodel.HighFiverViewModel;

/**
 * Created by nicklas on 10/1/13.
 */
public class MyHighFiversArrayAdapter extends AbstractArrayAdapter<HighFiverViewModel>{

    public MyHighFiversArrayAdapter(Context context, int layoutResourceId, HighFiverViewModel[] data) {
        super(context, layoutResourceId, 0, data);
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

        return myHighFiversHolder;
    }

    @Override
    public View.OnClickListener createOnClickListener(HighFiverViewModel highFiverViewModel) {
        return null;
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void setDataOnHolder(Holder holder, HighFiverViewModel highFiverViewModel) {
        MyHighFiversHolder h = (MyHighFiversHolder) holder;

        h.name.setText(highFiverViewModel.getModel().getName());
        h.username.setText(highFiverViewModel.getModel().getUsername());
        //TODO: load async
        //h.userPhoto.setImageResource(R.drawable.nicklas);


        if (highFiverViewModel.getModel().getPicture() != null){
            ImageService.populateImage(h, highFiverViewModel.getModel().getPicture().toString());
        }

    }
}
