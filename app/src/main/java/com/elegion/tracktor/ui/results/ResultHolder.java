package com.elegion.tracktor.ui.results;


import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.util.StringUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Toothpick;

/**
 * @author Azret Magometov
 */
public class ResultHolder extends RecyclerView.ViewHolder {
    
    private View mView;
    private long mTrackId;
    private Track mTrack;
    
    @Inject
    ResultsViewModel mResultsViewModel;
    
    @BindView(R.id.tv_date) TextView mDateText;
    @BindView(R.id.tv_distance) TextView mDistanceText;
    @BindView(R.id.tv_duration) TextView mDuration;
    @BindView(R.id.tv_AverageSpeed) TextView mAverageSpeed;
    @BindView(R.id.tv_SpentCalories) TextView mSpentCalories;
    @BindView(R.id.tv_Comment) MultiAutoCompleteTextView mComment;
    @BindView(R.id.tv_Action) TextView mAction;
    
    @BindView(R.id.expandableLayout) ConstraintLayout expandableLayout;
    
    
    
    
    
    
    public ResultHolder(View view)  {
        
        super(view);
        mView = view;
        ButterKnife.bind(this,view);
        Toothpick.inject(this, App.getAppScope());
        
    }
    
    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }
    
    public void bind(Track track) {
        
        mTrack = track;
        mTrackId = track.getId();
        mDateText.setText(StringUtil.getDateText(track.getDate()));
        mDistanceText.setText(StringUtil.getDistanceText(track.getDistance()));
        mDuration.setText(StringUtil.getTimeText(track.getDuration()));
        mAverageSpeed.setText(StringUtil.getAverageSpeedText(track.getDistance(),track.getDuration()));
        mSpentCalories.setText(StringUtil.getSpentCaloriesText(mResultsViewModel.calculateSpentCalories(track.getAction())));
        mComment.setText(StringUtil.getCommentsText(track.getComment()));
        mAction.setText(StringUtil.getActionText(track.getAction()));
        
    }
    @OnClick(R.id.tv_Comment)
    public void changeComment(){
        Log.d("ResultHoler","comment was pressed in expandable");
        mResultsViewModel.updateComment(mTrackId,mComment.getText().toString());//здесь сохраняем редактируемый комментарий
    
    }
    
    
    public void setOnClickListener(ResultsAdapter.OnItemClickListener listener) {
        
        
        mView.setOnClickListener(view -> {
            
            if(listener != null) {
                listener.onItemClick(mTrackId);
            }
            
        });
    }
    
    public void setOnLongClickListener(ResultsAdapter.OnItemLongClickListener listener) {
        mView.setOnLongClickListener(view->{
            if(listener!=null) {
                
                mTrack.setExpanded(!mTrack.isExpanded());
                listener.OnItemLongClick(mTrackId);
            }
            return true;
        });
    }
    
}
