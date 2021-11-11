package com.elegion.tracktor.service;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;

/*
Всю логику, связанную с расчетом и обновлением маршрута
переносим из CounterService в отдельный класс - TrackHelper
 */
public class TrackHelper {

    public static final int UPDATE_INTERVAL = 15_000;
    public static final int UPDATE_FASTEST_INTERVAL = 5_000;
    public static final int UPDATE_MIN_DISTANCE = 20;
    private double mDistance;
    private Disposable mTimerDisposable;
    private List<LatLng> mRoute = new ArrayList<>();

    private Location mLastLocation;
    private LatLng mLastPosition;



    private boolean positionChanged(LatLng newPosition) {
        return mLastLocation.getLongitude() != newPosition.longitude || mLastLocation.getLatitude() != newPosition.latitude;
    }

    private void addPointToRoute(Location lastLocation) {
        mLastLocation = lastLocation;
        mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mRoute.add(mLastPosition);
    }


    public boolean isFirstPoint() {
        return mRoute.size() == 0 && mLastLocation == null && mLastPosition == null;
    }





}
