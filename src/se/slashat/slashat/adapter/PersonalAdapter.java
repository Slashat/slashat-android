package se.slashat.slashat.adapter;

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

public class PersonalAdapter extends ArrayAdapter<Personal> {

	private int layoutResourceId;
	private Context context;
	private Personal[] data;

	public PersonalAdapter(Context context, int layoutResourceId,
			Personal data[]) {
		super(context, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		PersonalHolder holder;
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = createHolderForPersonal(row);
			row.setTag(holder);
		} else {
			holder = (PersonalHolder) row.getTag();
		}

		Personal personal = data[position];
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
