package com.elegion.tracktor.data;

import android.support.annotation.MainThread;
import android.util.Log;
import android.widget.Toast;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.model.Track;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author Azret Magometov
 */
public class RealmRepository implements IRepository<Track> {

    private Realm mRealm;
    private RealmAsyncTask asyncTransaction;

    private static AtomicLong sPrimaryId;
    @Inject
    public RealmRepository(App context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
        Number max = mRealm.where(Track.class).max("id");
        sPrimaryId = max == null ? new AtomicLong(0) : new AtomicLong(max.longValue());
    }

/*
    public RealmRepository() {

        //дополнительный конструктор для создания объекта класса
        //который используется в loadSortedByIdTracks

            mRealm = Realm.getDefaultInstance();
            Number max = mRealm.where(Track.class).max("id");
            sPrimaryId = max == null ? new AtomicLong(0) : new AtomicLong(max.longValue());

    }
*/

    @Override
    public Track getItem(long id) {
        Track track = getRealmAssociatedTrack(id);
        return track != null ? mRealm.copyFromRealm(track) : null;
    }

    private Track getRealmAssociatedTrack(long id) {
        return mRealm.where(Track.class).equalTo("id", id).findFirst();
    }

    @Override
    public List<Track> getAll() {
        return mRealm.where(Track.class).sort("id",Sort.DESCENDING).findAll();
    }



    @Override
    public long insertItem(Track track) {
        track.setId(sPrimaryId.incrementAndGet());
        mRealm.beginTransaction();
        mRealm.copyToRealm(track);
        mRealm.commitTransaction();
        return sPrimaryId.longValue();
    }

    @Override
    public boolean deleteItem(final long id) {

        boolean isDeleteSuccessful;
        mRealm.beginTransaction();

        Track track = getRealmAssociatedTrack(id);

        if (track != null) {
            track.deleteFromRealm();
            isDeleteSuccessful = true;
        } else {
            isDeleteSuccessful = false;
        }

        mRealm.commitTransaction();

        return isDeleteSuccessful;
    }

    /*
        @Override
    public void updateItem(Film Film) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(Film);
        mRealm.commitTransaction();
        EventBus.getDefault().post(new OnFilmDataBaseUpdate());
    }

     */
    @Override
    public long updateItem(Track track) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(track);
        mRealm.commitTransaction();
        return 0;
    }

    public long createAndInsertTrackFrom(long duration, double distanse, String base64image) {
        Track track = new Track();

//        mRealm.beginTransaction();
//        Track track = mRealm.createObject(Track.class, sPrimaryId.incrementAndGet());
        track.setDistance(distanse);
        track.setDuration(duration);
        track.setImageBase64(base64image);
        track.setDate(new Date());
        track.setAverageSpeed(distanse,duration);
//        mRealm.commitTransaction();
//        return sPrimaryId.longValue();

        return insertItem(track);

    }
    public long createAndUpdateTrackFrom(long id,long duration,double distanse,String base64image,String comment){

        Track track = new Track();
        track.setId(id);
        track.setDistance(distanse);
        track.setDuration(duration);
        track.setImageBase64(base64image);
        track.setDate(new Date());
        track.setAverageSpeed(distanse,duration);
        track.setComment(comment);

        return updateItem(track);

    }


    @Override
    public List<Track> getRealmSortedTracks(boolean ascending) {


        if (ascending) {

            return mRealm.copyFromRealm(mRealm.where(Track.class)
                    .sort("id", Sort.ASCENDING)
                    .findAll()
            );

        } else {


            return mRealm.copyFromRealm(mRealm.where(Track.class)
                    .sort("id", Sort.DESCENDING)
                    .findAll()
            );
        }
    }


    public List<Track> getRealmSortedByDateDurationDistanceTracks(int mSortByDateDurationDistance) {

        Realm mAnotherRealm = Realm.getDefaultInstance();
        List<Track> mSortedRealm = null;
        switch (mSortByDateDurationDistance){
            case 1:  //sort by date

                mSortedRealm = mAnotherRealm.copyFromRealm(mAnotherRealm.where(Track.class)
                        .sort("date",Sort.ASCENDING)
                        .findAll()
                );

                break;
            case 2: //sort by duration
                mSortedRealm = mAnotherRealm.copyFromRealm(mAnotherRealm.where(Track.class)
                        .sort("duration",Sort.ASCENDING)
                        .findAll()
                );

                break;
            case 3://sort by distance;
                mSortedRealm = mAnotherRealm.copyFromRealm(mAnotherRealm.where(Track.class)
                        .sort("distance",Sort.ASCENDING)
                        .findAll()
                );

                break;


        }

        mAnotherRealm.close();
      return   mSortedRealm;
    }
}
