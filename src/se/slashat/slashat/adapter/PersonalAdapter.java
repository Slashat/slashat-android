package se.slashat.slashat.adapter;

import se.slashat.slashat.Callback;
import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;
import android.app.Activity;
import android.content.Context;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalAdapter extends ArrayAdapter<Personal> {

	private int layoutResourceId;
	private Context context;
	private Personal[] data;
	private Callback<Personal> callback;

	public PersonalAdapter(Context context, int layoutResourceId,
			Personal data[],Callback<Personal> callback) {
		super(context, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.callback = callback;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		View row = convertView;
		PersonalHolder holder;
		final Personal personal = data[position];
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = createHolderForPersonal(row);
			row.setTag(holder);
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println(personal.getBio());
					callback.call(personal);
				}
			});
		} else {
			holder = (PersonalHolder) row.getTag();
		}


		holder.txtTitle.setText(personal.getName());
		holder.imgView.setImageResource(personal.getImg());

		return row;
	}

	private PersonalHolder createHolderForPersonal(View row) {
		PersonalHolder holder;
		holder = new PersonalHolder();
		holder.imgView = (ImageView) row.findViewById(R.id.imgIcon);
		holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
		return holder;
	}

	static class PersonalHolder {
		TextView txtTitle;
		ImageView imgView;
	}
}
