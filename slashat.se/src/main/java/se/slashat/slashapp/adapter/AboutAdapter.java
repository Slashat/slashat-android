package se.slashat.slashapp.adapter;

import java.io.Serializable;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Personal;
import se.slashat.slashapp.viewmodel.AboutViewModel;
import se.slashat.slashapp.viewmodel.ViewModelBase;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutAdapter extends AbstractArrayAdapter<ViewModelBase<?>> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Callback<Personal> callback;

	public AboutAdapter(Context context, int layoutResourceId,
                        ViewModelBase[] data, Callback<Personal> callback) {
		super(context, layoutResourceId, R.layout.section, data);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.callback = callback;
	}

	public static class AboutHolder extends Holder {
		TextView txtName;
		ImageView imgView;
        TextView txtTitle;
	}

	@Override
	public Holder createHolder(View row) {
		AboutHolder holder = new AboutHolder();
		holder.imgView = (ImageView) row.findViewById(R.id.imgIcon);
		holder.txtName = (TextView) row.findViewById(R.id.txtName);
        holder.txtTitle = (TextView) row.findViewById(R.id.title);
		return holder;
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public OnClickListener createOnClickListener(final ViewModelBase personal) {
        if (personal.getModel() instanceof Personal){
		    return new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.call((Personal) personal.getModel());
			}
		};
        }
        return new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
	}

	@Override
	public void setDataOnHolder(Holder holder, ViewModelBase personal) {
        AboutViewModel pvm = (AboutViewModel) personal;
		AboutHolder ph = (AboutHolder) holder;

		ph.txtName.setText(pvm.getName());
		ph.imgView.setImageResource(pvm.getImg());
        ph.txtTitle.setText(pvm.getTitle());
	}
}
