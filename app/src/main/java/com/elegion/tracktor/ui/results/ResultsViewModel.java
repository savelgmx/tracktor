package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.ContextModule;
import com.elegion.tracktor.ui.preferences.ReadUserPreferences;
import com.elegion.tracktor.ui.preferences.UserRepository;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import java.util.List;

import javax.inject.Inject;

import toothpick.Toothpick;



/**
 * ResultsViewModel. В нем также нужно сделать инжект репозитория
 */
public class ResultsViewModel extends ViewModel {

    private static final String APP_PREFERENCES = "_preferences";
     @Inject
    IRepository<Track> mRepository;


     @Inject
     UserRepository mUserRepository;

     @Inject
     Context mContext;


    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();
    private MutableLiveData<Bitmap> mImage = new MutableLiveData<>();

    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mDistanceText = new MutableLiveData<>();
    private MutableLiveData<String> mAverageSpeed = new MutableLiveData<>();
    private String TAG ="ResultsViewModel";


    public ResultsViewModel() {
        super();

        Toothpick.inject(this, App.getAppScope());


    }



    public void loadTracks() {
        if (mTracks.getValue() == null || mTracks.getValue().isEmpty()) {
            mTracks.postValue(mRepository.getAll());
        }
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }

    public void loadImage(long mTrackId) {
        Track track = mRepository.getItem(mTrackId);
        String distance = StringUtil.getDistanceText(track.getDistance());
        String time = StringUtil.getTimeText(track.getDuration());
        Bitmap bitmapImage = ScreenshotMaker.fromBase64(track.getImageBase64());
        String averageSpeed = StringUtil.getAverageSpeedText(track.getDistance(), track.getDuration());

        mAverageSpeed.postValue(averageSpeed);
        mTimeText.postValue(time);
        mDistanceText.postValue(distance);
        mImage.postValue(bitmapImage);
    }


    public void loadSavedPreferences(){

        Log.d(TAG,"mContext="+mContext);


        String mWeight=mUserRepository.getUserWeight(mContext);
        String mHeight=mUserRepository.getUserHeight(mContext);
        String mAge=mUserRepository.getUserAge(mContext);

        Log.d(TAG," weight= "+mWeight+" Height= "+mHeight+ " Age= "+mAge);
    }




    public MutableLiveData<Bitmap> getImage() {
        return mImage;
    }

    public MutableLiveData<String> getTime() {
        return mTimeText;
    }

    public MutableLiveData<String> getDistance() {
        return mDistanceText;
    }

    public MutableLiveData<String> getAverageSpeed() {
        return mAverageSpeed;
    }


}

