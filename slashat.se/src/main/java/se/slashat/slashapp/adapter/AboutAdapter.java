package se.slashat.slashapp.adapter;

import java.io.Serializable;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Crew;
import se.slashat.slashapp.viewmodel.AboutViewModel;
import se.slashat.slashapp.viewmodel.DisplayRowViewModel;
import se.slashat.slashapp.viewmodel.ViewModelBase;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AboutAdapter extends AbstractArrayAdapter<DisplayRowViewModel<?>> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Callback<Crew> callback;

    public AboutAdapter(Context context, int layoutResourceId,
                        DisplayRowViewModel[] data, Callback<Crew> callback) {
        super(context, layoutResourceId, R.layout.section, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.callback = callback;
    }

    public static class AboutHolder extends ImageAsyncHolder {
        TextView txtName;
        TextView txtTitle;
    }

    @Override
    public Holder createHolder(View row) {
        AboutHolder holder = new AboutHolder();
        holder.imageThumb = (ImageView) row.findViewById(R.id.imgIcon);
        holder.txtName = (TextView) row.findViewById(R.id.txtName);
        holder.txtTitle = (TextView) row.findViewById(R.id.title);
        holder.progressBar = (ProgressBar) row.findViewById(R.id.imageviewprogress);
        return holder;
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public OnClickListener createOnClickListener(final DisplayRowViewModel aboutViewModel) {
        if (!(aboutViewModel instanceof AboutViewModel)) {
            return new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            };
        }
        final AboutViewModel vm = (AboutViewModel) aboutViewModel;
        if (vm.getType() == AboutViewModel.Type.CREW) {
            return new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.call(vm.getCrew());
                }
            };
        }
        return null;
    }

    @Override
    public void setDataOnHolder(Holder holder, DisplayRowViewModel personal, int position) {
        AboutViewModel pvm = (AboutViewModel) personal;
        final AboutHolder aboutHolder = (AboutHolder) holder;

        aboutHolder.txtName.setText(pvm.getName());
        if (pvm.getImageUrl() == null) {
            aboutHolder.imageThumb.setImageResource(pvm.getImg());
        } else if (pvm.getImageUrl() != null) {
            aboutHolder.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(aboutHolder.imageThumb.getContext()).load(pvm.imageUrl.toString()).into(aboutHolder.imageThumb, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    aboutHolder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    aboutHolder.progressBar.setVisibility(View.GONE);
                }
            });
        }
        //

        aboutHolder.txtTitle.setText(pvm.getTitle());
    }
}
