package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;

import javax.inject.Inject;

import toothpick.Toothpick;

public class DialogFragmentViewModel extends ViewModel {
  //  private MutableLiveData<String> mComment = new MutableLiveData<>();

    //private int trackId;
    private int mTitleId;
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
                comment

        );
    }

    public int getTitleId(Long trackid) {
        Track track = mRepository.getItem(trackid);
        if(track.getComment()==null){
            mTitleId= R.string.dialog_title_new_comment;
        }else mTitleId=R.string.dialog_title_edit_comment;
        return mTitleId;
    }

    public String getComment(Long trackid) {
        Track track = mRepository.getItem(trackid);

        return track.getComment();
    }
}
