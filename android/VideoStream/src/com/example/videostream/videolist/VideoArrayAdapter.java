package com.example.videostream.videolist;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videostream.R;

public class VideoArrayAdapter extends ArrayAdapter<VideoItem> {
	private Context context;
	private int id;
	private List<VideoItem> data;

	public VideoArrayAdapter(Context context, int textViewResourceId,
			List<VideoItem> data) {
		super(context, textViewResourceId, data);
		this.context = context;
		this.id = textViewResourceId;
		this.data = data;
	}

	public void removeAllItem() {
		data.clear();
	}

	@Override
	public VideoItem getItem(int i) {
		return data.get(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(id, parent, false);

			// save components to ViewHolder
			holder = new ViewHolder();
			holder.title = (TextView) row.findViewById(R.id.TextViewTitle);
			holder.description = (TextView) row
					.findViewById(R.id.TextViewDescription);
			holder.duration = (TextView) row
					.findViewById(R.id.TextViewDuration);
			holder.image = (ImageView) row.findViewById(R.id.fd_Icon1);

			row.setTag(holder);
		} else {
			// get components from ViewHolder
			holder = (ViewHolder) row.getTag();
		}

		VideoItem o = data.get(position);
		if (o != null) {
			if (holder.title != null)
				holder.title.setText(o.getTitle());
			if (holder.description != null)
				holder.description.setText(o.getDescription());
			if (holder.duration != null)
				holder.duration.setText(o.getDuration());
			if (holder.image != null)
				holder.image.setImageBitmap(o.getBitmap());
		}
		return row;
	}

	static class ViewHolder {
		ImageView image;
		TextView title;
		TextView description;
		TextView duration;
	}

}
