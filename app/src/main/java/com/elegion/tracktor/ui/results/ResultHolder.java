package com.elegion.tracktor.ui.results;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
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
    
    @Inject
    Context mContext;


    @BindView(R.id.tv_date) TextView mDateText;
    @BindView(R.id.tv_distance) TextView mDistanceText;
    @BindView(R.id.tv_duration) TextView mDuration;
    @BindView(R.id.tv_AverageSpeed) TextView mAverageSpeed;
    @BindView(R.id.tv_SpentCalories) TextView mSpentCalories;
    @BindView(R.id.tv_Comment) MultiAutoCompleteTextView mComment;
    @BindView(R.id.tv_Action) TextView mAction;
    @BindView(R.id.ibViewOptions) ImageButton mIbViewOptions;
    
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
        mResultsViewModel.updateComment(mTrackId,mComment.getText().toString());//здесь сохраняем редактируемый комментарий
        
    }
    
    @OnClick(R.id.ibViewOptions)
    public void onOptionsMenu(){
        Log.d("ResultHolder","On Optopns Menu with TrackId="+ mTrackId);
        //здесь будем вызывать popup options menu
        PopupMenu popup=new PopupMenu(mContext, mIbViewOptions);
        popup.inflate(R.menu.menu_details_fragment);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.actionShare://делимся результатами трека
                        Log.d("ResultHolder","actionShare Menu with TrackId="+ mTrackId);
                        
                        break;
                    case R.id.actionDelete://удаляем трек
                        //       mResultsViewModel.deleteTrack(mTrackId);
                        //        break;
                }
                return false;
            }
        });
        
        
        
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
