package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

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

        Log.d("Result Dailog", "mTrackId="+String.valueOf(mTrackId));

        Log.d("Result Dailog", "Duration="+String.valueOf(track.getDuration()));
        Log.d("Result Dailog", "Distancе="+String.valueOf(track.getDistance()));
        //Log.d("Result Dailog", "ImageBase="+String.valueOf(track.getImageBase64()));

        Log.d("Result Dailog","Get Current Comment="+String.valueOf(track.getComment()));

        Log.d("Result Dailog", "Comment="+String.valueOf(comment));


        mRealmRepository.createAndUpdateTrackFrom(mTrackId,
                track.getDuration(),
                track.getDistance(),
                track.getImageBase64(),
                comment

        );
    }


}
