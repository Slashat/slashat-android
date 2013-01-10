package se.slashat.slashat.adapter;

import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonAdapter extends AbstractArrayAdapter<Personal> {

	private Personal person;

	public PersonAdapter(Context context, int layoutResourceId, Personal[] data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.person = data[0];
	}

	static class PersonHolder extends Holder {
		TextView name;
		TextView email;
		TextView twitter;
		TextView homepage;
		TextView bio;
		ImageView photo;
	}

	@Override
	public PersonHolder createHolder(View row) {
		PersonHolder holder = new PersonHolder();
		holder.photo = (ImageView) row.findViewById(R.id.photo);
		holder.name = (TextView) row.findViewById(R.id.name);
		holder.email = (TextView) row.findViewById(R.id.email);
		holder.twitter = (TextView) row.findViewById(R.id.twitter);
		holder.homepage = (TextView) row.findViewById(R.id.homepage);
		holder.bio = (TextView) row.findViewById(R.id.bio);
		return holder;
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public OnClickListener createOnClickListener(Personal t) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		};
	}

	@Override
	public void setDataOnHolder(Holder holder, Personal t) {
		PersonHolder ph = (PersonHolder) holder;
		ph.name.setText(person.getName());
		ph.email.setText(person.getEmail());
		ph.twitter.setText(person.getTwitter());
		ph.homepage.setText(person.getHomepage());
		ph.bio.setText(person.getBio());
		ph.photo.setImageResource(person.getImg());
	}
}
