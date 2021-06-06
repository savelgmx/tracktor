package com.elegion.tracktor.ui.results;

import android.app.MediaRouteButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.util.StringUtil;

import java.util.List;

/**
 * @author Azret Magometov
 */
public class ResultHolder extends RecyclerView.ViewHolder {

    public LinearLayout expandableLayout;
    private View mView;
    private TextView mDateText;
    private TextView mDistanceText;
    private TextView mDuration;
    private long mTrackId;

    private TextView mAverageSpeed;
    private TextView mSpentCalories;
    private MultiAutoCompleteTextView mComment;

    List<Track> trackList;

    public ResultHolder(View view) {
        super(view);
        mView = view;
        mDateText = view.findViewById(R.id.tv_date);
        mDistanceText = view.findViewById(R.id.tv_distance);
        mDuration = view.findViewById(R.id.tv_duration);

        mAverageSpeed = view.findViewById(R.id.tv_AverageSpeed);
        mSpentCalories = view.findViewById(R.id.tv_SpentCalories);
        mComment       = view.findViewById(R.id.tv_Comment);

        expandableLayout = view.findViewById(R.id.expandableLayout);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }

    public void bind(Track track) {
        mTrackId = track.getId();
        mDateText.setText(StringUtil.getDateText(track.getDate()));
        mDistanceText.setText(StringUtil.getDistanceText(track.getDistance()));
        mDuration.setText(StringUtil.getTimeText(track.getDuration()));

        mAverageSpeed.setText(StringUtil.getAverageSpeedText(track.getDistance(),track.getDuration()));
      //  mSpentCalories.setText(StringUtil.getSpentCaloriesText(track.get));
        mComment.setText(StringUtil.getCommentsText(track.getComment()));

    }


    public void setOnClickListener(ResultsAdapter.OnItemClickListener listener) {


        mView.setOnClickListener(view -> {

            Log.d("ResultHolder","On Click Listener  triggered");


            if(listener != null) {
                listener.onItemClick(mTrackId);
            }

        });
    }

    public void setOnLongClickListener(ResultsAdapter.OnItemLongClickListener listener) {
        mView.setOnLongClickListener(view->{
            if(listener!=null) {
                listener.OnItemLongClick(mTrackId);
            }
            return true;
        });
    }

}
