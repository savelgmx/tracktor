package com.elegion.tracktor.ui.results;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.preference.PreferenceManager;
import android.widget.RadioButton;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.ui.map.MainActivity;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import java.util.List;

import javax.inject.Inject;

import toothpick.Toothpick;



/**
 * ResultsViewModel. В нем также нужно сделать инжект репозитория
 */
public class ResultsViewModel extends AndroidViewModel {

    private static final String APP_PREFERENCES = "_preferences";
    //private
    @Inject
    IRepository<Track> mRepository;

    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();
    private MutableLiveData<Bitmap> mImage = new MutableLiveData<>();

    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mDistanceText = new MutableLiveData<>();
    private MutableLiveData<String> mAverageSpeed = new MutableLiveData<>();


    public ResultsViewModel(Application mApp) {
        super(mApp);

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

        //       getSharedPreferences();

        mAverageSpeed.postValue(averageSpeed);
        mTimeText.postValue(time);
        mDistanceText.postValue(distance);
        mImage.postValue(bitmapImage);
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


    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    private void loadSharedPreferences()  {
        Context context = getApplication().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString("username", "Default NickName");
        String passw = prefs.getString("password", "Default Password");
        boolean checkBox = prefs.getBoolean("checkBox", false);
        String listPrefs = prefs.getString("listpref", "Default list prefs");
    }
}

