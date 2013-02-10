package se.slashat.slashat.adapter;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class AbstractArrayAdapter<T> extends ArrayAdapter<T>{

	protected int layoutResourceId;
	protected Context context;
	protected T[] data;

	public AbstractArrayAdapter(Context context, int layoutResourceId, T[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		View row = convertView;
		Holder holder;
		final T t = data[position];
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = createHolder(row);
			row.setTag(holder);
			if (isClickable()) {
				row.setOnClickListener(createOnClickListener(t));
			} else {
				row.setClickable(false);
			}

		} else {
			holder = (Holder) row.getTag();
		}
		setDataOnHolder(holder, t);
		return row;
	}

	/**
	 * Create a holder for this row. The holder is following the Android holder
	 * pattern for UI caching
	 * 
	 * @param row
	 * @return
	 */
	public abstract Holder createHolder(View row);

	/**
	 * Returns an OnClickListener that will be executed when the user clicks on
	 * this row. It takes the generic type of the class as argument (the
	 * datamodel of the clicked row)
	 * 
	 * @param t
	 * @return
	 */

	public abstract OnClickListener createOnClickListener(T t);

	/**
	 * Is the row clickable or not
	 * @return
	 */
	public abstract boolean isClickable();

	/**
	 * Set all the data on the holder based on the datamodel T
	 * @param holder
	 * @param t
	 */
	public abstract void setDataOnHolder(Holder holder, T t);

	static class Holder {
	}
}
