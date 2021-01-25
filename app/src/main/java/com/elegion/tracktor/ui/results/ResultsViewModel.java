package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.util.ScreenshotMaker;

import java.util.List;

import javax.inject.Inject;

import toothpick.Toothpick;

/**
 * ResultsViewModel. В нем также нужно сделать инжект репозитория
 */
public class ResultsViewModel extends ViewModel {

    //private
    @Inject
    RealmRepository mRepository;

    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();
    private MutableLiveData<Bitmap> mImage = new MutableLiveData<>();


    private Bitmap bitmapImage;


    public ResultsViewModel(RealmRepository repository) {

        Toothpick.inject(this, App.getAppScope());
        mRepository = repository;
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
        bitmapImage = ScreenshotMaker.fromBase64(track.getImageBase64());
        mImage.postValue(bitmapImage);
    }

    public MutableLiveData<Bitmap> getImage(){
        return mImage;
    }
}

