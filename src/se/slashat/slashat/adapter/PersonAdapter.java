package se.slashat.slashat.adapter;

import java.io.Serializable;

import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonAdapter extends AbstractArrayAdapter<Personal> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Personal person;

	public PersonAdapter(Context context, int layoutResourceId, Personal[] data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.person = data[0];
	}

	static class PersonHolder extends Holder {
		TextView name;
		ImageView email;
		ImageView twitter;
		ImageView homepage;
		TextView bio;
		ImageView photo;
	}

	@Override
	public PersonHolder createHolder(View row) {
		PersonHolder holder = new PersonHolder();
		holder.photo = (ImageView) row.findViewById(R.id.photo);
		holder.name = (TextView) row.findViewById(R.id.name);
		holder.email = (ImageView) row.findViewById(R.id.email);
		holder.twitter = (ImageView) row.findViewById(R.id.twitter);
		holder.homepage = (ImageView) row.findViewById(R.id.browser);
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
	public void setDataOnHolder(Holder holder, final Personal t) {
		PersonHolder ph = (PersonHolder) holder;
		ph.name.setText(person.getName());
		setTwitterClickListener(t, ph);
		setEmailClickListener(t, ph);
		setHomepageClickListener(t, ph);
		ph.bio.setText(person.getBio());
		ph.photo.setImageResource(person.getImg());
	}

	private void setHomepageClickListener(final Personal t, PersonHolder ph) {
		ph.homepage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openBrowserIntent(t);
			}
		});
	}

	private void setEmailClickListener(final Personal t, PersonHolder ph) {
		ph.email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openEmailIntent(t);
			}
		});
	}

	private void setTwitterClickListener(final Personal t, PersonHolder ph) {
		ph.twitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openTwitterIntent(t);
			}
		});
	}

	private void openTwitterIntent(final Personal t) {
		try {
			// TODO: Can all twitter clients be targeted?
			// Looks like every different client has it's own way to open it
			// correctly. Tried with twitter:// but got errors that no
			// application was found.

			context.getPackageManager()
					.getPackageInfo("com.twitter.android", 0);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName("com.twitter.android",
					"com.twitter.android.ProfileActivity");
			intent.putExtra("screen_name", t.getTwitter());
			context.startActivity(intent);
		} catch (NameNotFoundException e) {
			// Fall back to browser
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("https://twitter.com/" + t.getTwitter())));
		}
	}

	private void openEmailIntent(final Personal t) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { t.getEmail() });
		// TODO: The chooser shows more than just email clients.
		context.startActivity(Intent.createChooser(intent,
				"Välj applikation att skicka mail med"));
	}

	private void openBrowserIntent(Personal t) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(t
				.getHomepage())));
	}
}
