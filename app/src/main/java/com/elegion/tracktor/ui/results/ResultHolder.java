package com.elegion.tracktor.ui.results;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.event.DeleteTrackEvent;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Toothpick;

import static android.support.v4.content.ContextCompat.startActivity;


/**
 * @author Azret Magometov
 */
public class ResultHolder extends RecyclerView.ViewHolder {

   // private  RealmRepository mRepository;
    private View mView;
    private long mTrackId;
    private Track mTrack;

    @Inject
    ResultsViewModel mResultsViewModel;

    @Inject
    Context mContext;

    @Inject
    ResultsDetailsFragment mResultsDetailsFragment;

    @Inject

    RealmRepository mRepository;


    @BindView(R.id.tv_date) TextView mDateText;
    @BindView(R.id.tv_distance) TextView mDistanceText;
    @BindView(R.id.tv_duration) TextView mDuration;
    @BindView(R.id.tv_AverageSpeed) TextView mAverageSpeed;
    @BindView(R.id.tv_SpentCalories) TextView mSpentCalories;
    @BindView(R.id.tv_Comment) MultiAutoCompleteTextView mComment;
    @BindView(R.id.tv_Action) TextView mAction;
    @BindView(R.id.ibViewOptions) ImageButton mIbViewOptions;

    @BindView(R.id.expandableLayout) ConstraintLayout expandableLayout;
    @BindView(R.id.btn_expand) Button mExpandButton;


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

        setExpandableLayoutVisibilty();

    }
    @OnClick(R.id.tv_Comment)
    public void changeComment(){
        mResultsViewModel.updateComment(mTrackId,mComment.getText().toString());//здесь сохраняем редактируемый комментарий

    }

    @OnClick(R.id.btn_expand)
    public void setExpandableLayoutVisibilty(){
        mTrack.setExpanded(!mTrack.isExpanded());
        expandableLayout.setVisibility(mTrack.isExpanded() ? View.VISIBLE : View.GONE);//depend on expand flag value set visibility of expand Layout
        mExpandButton.setText(mTrack.isExpanded() ?"Свернуть":"Развернуть");
    }

    @OnClick(R.id.ibViewOptions)
    public void onOptionsMenu(){
         //здесь будем вызывать popup options menu
        PopupMenu popup=new PopupMenu(mContext, mIbViewOptions);
        popup.inflate(R.menu.menu_details_fragment);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.actionShare://делимся результатами трека
                        doShare();
                        break;
                    case R.id.actionDelete://удаляем трек
                        //mResultsViewModel.deleteTrack(mTrackId);
                        EventBus.getDefault().post(new DeleteTrackEvent(mTrackId));

                        break;
                }
                return false;
            }
        });
    }




    private void doShare(){
        //здесь мы получим Image а затем Intent intent = new Intent(Intent.ACTION_SEND);
        Bitmap bitmapImage = ScreenshotMaker.fromBase64(mTrack.getImageBase64());
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmapImage, "Мой маршрут", null);
        Uri uri = Uri.parse(path);
            /* В сообщении должна быть информация о расстоянии,
             времени, средней скорости, затраченных калориях,
             комментарий к треку и изображение трека.*/

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Время: " + mDateText.getText()
                + "\nРасстояние: " + mDistanceText.getText()
                +"\nСредняя скорость: "+mAverageSpeed.getText()
                +"\nЗатраченные калории: "+mSpentCalories.getText()
                + "\nВид деятельности: " + mAction.getText()
                +"\nКомментарий к треку: "+mComment.getText()
        );
        startActivity(mContext,Intent.createChooser(intent, "Результаты маршрута"),null);

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
