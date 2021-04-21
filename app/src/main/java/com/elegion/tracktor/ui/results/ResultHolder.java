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
    private TextView mAverageSpeed;
    private long mTrackId;

    public ResultHolder(View view) {
        super(view);
        mView = view;
        mDateText = view.findViewById(R.id.tv_date);
        mDistanceText = view.findViewById(R.id.tv_distance);
        mAverageSpeed = view.findViewById(R.id.tv_averagespeed);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }

    public void bind(Track track) {
        mTrackId = track.getId();
        mDateText.setText(StringUtil.getDateText(track.getDate()));
        mDistanceText.setText(StringUtil.getDistanceText(track.getDistance()));
        mAverageSpeed.setText(StringUtil.getAverageSpeedText(track.getAverageSpeed(),0));
    }

/*
    public void setListener(final ResultsFragment.OnItemClickListener listener) {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onClick(mTrackId);
                }
            }
        });
    }
*/

    public void setOnClickListener(ResultsAdapter.OnItemClickListener listener) {
        mView.setOnClickListener(view -> {
            if(listener != null) {
                listener.onItemClick(mTrackId);
            }
        });
    }

}
