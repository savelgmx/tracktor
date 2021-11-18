package com.elegion.tracktor.service;

import android.location.Location;

import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.greenrobot.eventbus.EventBus;

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

    public Location mLastLocation;
    public LatLng mLastPosition;



    public Position getPosition(LocationResult locationResult,List<LatLng>mRoute,Location LastLocation){

        Location newLocation = locationResult.getLastLocation();
        LatLng newPosition = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
        LatLng prevPosition=new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        if (positionChanged(newPosition)) {
            mRoute.add(newPosition);
            prevPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mDistance += SphericalUtil.computeDistanceBetween(prevPosition, newPosition);
         }


        return new Position(prevPosition, newPosition,mDistance);
    }




    private boolean positionChanged(LatLng newPosition) {
        return mLastLocation.getLongitude() != newPosition.longitude || mLastLocation.getLatitude() != newPosition.latitude;
    }

    public void addPointToRoute(Location lastLocation) {
        mLastLocation = lastLocation;
        mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mRoute.add(mLastPosition);
    }


    public boolean isFirstPoint() {
        return mRoute.size() == 0 && mLastLocation == null && mLastPosition == null;
    }


   final class Position{
        LatLng prevPosition;
        LatLng newPosition;
        double mDistance;

       public Position(LatLng prevPosition, LatLng newPosition, double mDistance) {

           this.prevPosition = prevPosition;
           this.newPosition = newPosition;
           this.mDistance = mDistance;

       }

   }


}
