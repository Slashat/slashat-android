package se.slashat.slashat.adapter;

import java.io.Serializable;

import se.slashat.slashat.Callback;
import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;
import se.slashat.slashat.viewmodel.PersonalViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalAdapter extends AbstractArrayAdapter<PersonalViewModel> implements Serializable {
	/**
	 * 
	 * @author Nicklas Löf
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Callback<PersonalViewModel> callback;

	public PersonalAdapter(Context context, int layoutResourceId,
			PersonalViewModel[] data, Callback<PersonalViewModel> callback) {
		super(context, layoutResourceId, R.layout.section, data);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.callback = callback;
	}

	public static class PersonalHolder extends Holder {
		TextView txtName;
		ImageView imgView;
		ImageView email;
		ImageView twitter;
		ImageView homepage;
	}

	@Override
	public Holder createHolder(View row) {
		PersonalHolder holder = new PersonalHolder();
		holder.imgView = (ImageView) row.findViewById(R.id.imgIcon);
		holder.txtName = (TextView) row.findViewById(R.id.txtName);
		holder.email = (ImageView) row.findViewById(R.id.email);
		holder.twitter = (ImageView) row.findViewById(R.id.twitter);
		holder.homepage = (ImageView) row.findViewById(R.id.browser);
		return holder;
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public OnClickListener createOnClickListener(final PersonalViewModel personal) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				//callback.call(personal); if we want to display something when a person is clicked
			}
		};
	}

	@Override
	public void setDataOnHolder(Holder holder, PersonalViewModel personal) {
		PersonalHolder ph = (PersonalHolder) holder;
		Personal model = personal.getModel();
		ph.txtName.setText(model.getName());
		ph.imgView.setImageResource(model.getImg());
		setTwitterClickListener(model, ph);
		setEmailClickListener(model, ph);
		setHomepageClickListener(model, ph);
	}
	
	private void setHomepageClickListener(final Personal t, PersonalHolder ph) {
		ph.homepage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openBrowserIntent(t);
			}
		});
	}

	private void setEmailClickListener(final Personal t, PersonalHolder ph) {
		ph.email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openEmailIntent(t);
			}
		});
	}

	private void setTwitterClickListener(final Personal t, PersonalHolder ph) {
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

			context.getPackageManager().getPackageInfo("com.twitter.android", 0);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
			intent.putExtra("screen_name", t.getTwitter());
			context.startActivity(intent);
		} catch (NameNotFoundException e) {
			// Fall back to browser
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + t.getTwitter())));
		}
	}

	private void openEmailIntent(final Personal t) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { t.getEmail() });
		// TODO: The chooser shows more than just email clients.
		context.startActivity(Intent.createChooser(intent, "Välj applikation att skicka mail med"));
	}

	private void openBrowserIntent(Personal t) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(t.getHomepage())));
	}
}
