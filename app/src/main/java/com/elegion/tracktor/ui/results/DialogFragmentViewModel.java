package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;

import javax.inject.Inject;

import toothpick.Toothpick;

public class DialogFragmentViewModel extends ViewModel {

    @Inject
    IRepository<Track> mRepository;
    @Inject
    RealmRepository mRealmRepository;



    public DialogFragmentViewModel(){
        super();
        Toothpick.inject(this, App.getAppScope());
    }

    public DialogFragmentViewModel(Long mTrackId) {

        super();
        Toothpick.inject(this,App.getAppScope());
    }

    public void updateComment(long mTrackId,String comment){
        Track track = mRepository.getItem(mTrackId);
        mRealmRepository.createAndUpdateTrackFrom(mTrackId,
                track.getDuration(),
                track.getDistance(),
                track.getImageBase64(),
                track.getComment()

        );
    }


}
