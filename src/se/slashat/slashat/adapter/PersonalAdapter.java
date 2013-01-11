package se.slashat.slashat.adapter;

import se.slashat.slashat.Callback;
import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalAdapter extends AbstractArrayAdapter<Personal> {

	private Callback<Personal> callback;

	public PersonalAdapter(Context context, int layoutResourceId,
			Personal data[], Callback<Personal> callback) {
		super(context, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.callback = callback;
	}

	public static class PersonalHolder extends Holder {
		TextView txtTitle;
		ImageView imgView;
	}

	@Override
	public Holder createHolder(View row) {
		PersonalHolder holder = new PersonalHolder();
		holder.imgView = (ImageView) row.findViewById(R.id.imgIcon);
		holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
		return holder;
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public OnClickListener createOnClickListener(final Personal personal) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.call(personal);
			}
		};
	}

	@Override
	public void setDataOnHolder(Holder holder, Personal personal) {
		PersonalHolder ph = (PersonalHolder) holder;
		ph.txtTitle.setText(personal.getName());
		ph.imgView.setImageResource(personal.getImg());
	}
}
