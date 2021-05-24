package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;

import com.elegion.tracktor.data.model.Track;

import java.util.List;

public class SortingViewModel extends ResultsViewModel{

    private MutableLiveData<String> mSortByIdQuery = new MutableLiveData<>();

    private boolean ascending = false;


    public  SortingViewModel(){
        super();
        updateFromRepository();

    }

    @Override
    protected void updateFromRepository() {
        //super.updateFromRepository();

        List<Track> sortedTracks=mRealmRepository.getRealmSortedTracks(ascending);
        mTracks.postValue(sortedTracks);

    }
}

