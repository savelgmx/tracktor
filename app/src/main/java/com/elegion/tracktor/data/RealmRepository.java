package com.elegion.tracktor.data;

import android.support.annotation.MainThread;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.model.Track;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author Azret Magometov
 */
public class RealmRepository implements IRepository<Track> {

    private Realm mRealm;

    private static AtomicLong sPrimaryId;
    @Inject
    public RealmRepository(App context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
        Number max = mRealm.where(Track.class).max("id");
        sPrimaryId = max == null ? new AtomicLong(0) : new AtomicLong(max.longValue());
    }

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

    @MainThread
    private List<Track> getRealmSortedTracks(boolean ascending){

        List<Track> tracks = new ArrayList<>();

   //     Realm realm = Realm.getDefaultInstance();


        try {
            mRealm.beginTransaction();
            final RealmResults<Track> realmResults = mRealm.where(Track.class)
                    .findAll();


            if (ascending) {
                realmResults.sort("id" , Sort.ASCENDING);
            } else {
                realmResults.sort("id" , Sort.DESCENDING);
            }

            int len = realmResults.size();

            for (int i=0;i<len;i++){

                tracks.add(realmResults.get(i));
            }

/*
            for (Track realmResult : realmResults) {
                Track track = new Track();
               // copy(track, realmResult);
                tracks.add(realmResult[i]);
            }
*/

            mRealm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
        } finally {
            mRealm.close();
        }
        return tracks;






    }

    @Override
    public List<Track> getAllSortById(boolean ascending) {

        List<Track> tracks = getRealmSortedTracks(ascending);
        return tracks;//!= null ? mRealm.copyFromRealm(tracks) : null;

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




}
