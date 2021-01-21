package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.drawable.Drawable;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;

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
    private MutableLiveData<Drawable> mImage = new MutableLiveData<>();

    private long mTrackId;//передаем сюда trackID для отыскания нужного трека



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

    public MutableLiveData<Drawable> getImage(){
        return mImage;
    }



}
