package se.slashat.slashat.adapter;

import java.util.List;

import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonAdapter extends ArrayAdapter<Personal>{

	private Context context;
	private int layoutResourceId;
	private Personal person;

	public PersonAdapter(Context context, int layoutResourceId,
			List<Personal> objects) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.person = objects.get(0);
	}
	
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		
		View row = convertView;
		PersonHolder holder;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = createHolderForPerson(row);
			row.setTag(holder);
		} else {
			holder = (PersonHolder) row.getTag();
		}


		holder.name.setText(person.getName());
		holder.email.setText(person.getEmail());
		holder.twitter.setText(person.getTwitter());
		holder.homepage.setText(person.getHomepage());
		holder.bio.setText(person.getBio());
		holder.photo.setImageResource(person.getImg());

		return row;		
	}
	
	private PersonHolder createHolderForPerson(View row) {
		PersonHolder holder;
		holder = new PersonHolder();
		holder.photo = (ImageView) row.findViewById(R.id.photo);
		holder.name = (TextView) row.findViewById(R.id.name);
		holder.email = (TextView) row.findViewById(R.id.email);
		holder.twitter = (TextView) row.findViewById(R.id.twitter);
		holder.homepage = (TextView) row.findViewById(R.id.homepage);
		holder.bio = (TextView) row.findViewById(R.id.bio);
		return holder;
	}

	static class PersonHolder {
		TextView name;
		TextView email;
		TextView twitter;
		TextView homepage;
		TextView bio;
		ImageView photo;
	}

}
