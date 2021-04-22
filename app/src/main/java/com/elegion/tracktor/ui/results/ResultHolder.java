package com.elegion.tracktor.ui.results;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.util.StringUtil;

/**
 * @author Azret Magometov
 */
public class ResultHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mDateText;
    private TextView mDistanceText;
    private TextView mDuration;
    private long mTrackId;

    public ResultHolder(View view) {
        super(view);
        mView = view;
        mDateText = view.findViewById(R.id.tv_date);
        mDistanceText = view.findViewById(R.id.tv_distance);
        mDuration = view.findViewById(R.id.tv_duration);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }

    public void bind(Track track) {
        mTrackId = track.getId();
        mDateText.setText(StringUtil.getDateText(track.getDate()));
        mDistanceText.setText(StringUtil.getDistanceText(track.getDistance()));
        mDuration.setText(StringUtil.getDurationText(track.getDuration()));
    }


    public void setOnClickListener(ResultsAdapter.OnItemClickListener listener) {
        mView.setOnClickListener(view -> {
            if(listener != null) {
                listener.onItemClick(mTrackId);
            }
        });
    }

}
