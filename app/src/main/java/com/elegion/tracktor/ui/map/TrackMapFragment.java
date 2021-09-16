package com.elegion.tracktor.ui.map;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.elegion.tracktor.R;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.GetRouteEvent;
import com.elegion.tracktor.event.StartTrackEvent;
import com.elegion.tracktor.event.StopTrackEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.ui.results.ResultsActivity;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class TrackMapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    public static final int DEFAULT_ZOOM = 15;

    private GoogleMap mMap;

    @Inject
    MainViewModel mMainViewModel;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);

    }

    public void configure() {
        getMapAsync(this);
        Scope scope = Toothpick.openScope(CounterFragment.class);
        scope.installModules(new ViewModelModule(this));

        Toothpick.inject(this, scope);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this::initMap);
    }

    private void initMap() {
        Context context = getContext();
        if (context != null && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new GetRouteEvent());
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event) {



        mMap.addPolyline(new PolylineOptions().add(event.getLastPosition(),
                event.getNewPosition())
                .color(getColorsValue())
                .width(getWidthValue())
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(event.getNewPosition(), DEFAULT_ZOOM));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event) {
        mMap.clear();

        List<LatLng> route = event.getRoute();
        mMap.addPolyline(new PolylineOptions().addAll(route)
                .color(getColorsValue())
                .width(getWidthValue())
        );
        addMarker(route.get(0), getString(R.string.start));
        zoomRoute(route);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRoute(StartTrackEvent event) {
        mMap.clear();
        addMarker(event.getStartPosition(), getString(R.string.start));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRoute(StopTrackEvent event) {
        List<LatLng> route = event.getRoute();
        if (route.isEmpty()) {
            Toast.makeText(getContext(), R.string.dont_stay, Toast.LENGTH_SHORT).show();
        } else {
            addMarker(route.get(route.size() - 1), getString(R.string.end));

            takeMapScreenshot(route, bitmap -> {
                String base64image = ScreenshotMaker.toBase64(bitmap, Bitmap.CompressFormat.PNG,getCompressionRatioValue());
                long resultId = mMainViewModel.saveResults(base64image);
                ResultsActivity.start(getContext(), resultId);
            });
        }
    }

    private void addMarker(LatLng position, String text) {


        if ( text.equals(getString(R.string.start)) ){

            mMap.addMarker(new MarkerOptions().position(position).title(text).icon(BitmapDescriptorFactory.fromBitmap(getStartLogo())) );

        }
        if(text.equals(getString(R.string.end)) ){

            mMap.addMarker(new MarkerOptions().position(position).title(text).icon(BitmapDescriptorFactory.fromBitmap(getStopLogo())) );
            //  mMap.addMarker(new MarkerOptions().position(position).title(text));
        }

    }

    private Bitmap getStartLogo(){

        Bitmap startLogo;

        int iconConstant=Integer.parseInt(mMainViewModel.getListOfStartMarkIcons());
        switch (iconConstant){
            case 1:
                startLogo = BitmapFactory.decodeResource(getResources(), R.drawable.start_green);
                break;
            case 2:
                startLogo = BitmapFactory.decodeResource(getResources(),R.drawable.start_green_slim);
                break;
            case 3:
                startLogo = BitmapFactory.decodeResource(getResources(),R.drawable.start_green_circle);
                break;
            case 4:
                startLogo = BitmapFactory.decodeResource(getResources(), R.drawable.start_red);
                break;
            case 5:
                startLogo = BitmapFactory.decodeResource(getResources(),R.drawable.start_banner_1);
                break;
            case 6:
                startLogo = BitmapFactory.decodeResource(getResources(),R.drawable.start_black_slim);
                break;

            default:
                startLogo = BitmapFactory.decodeResource(getResources(),R.drawable.start_green_slim);
                break;
        }

        return generateSmallIcon(startLogo);
    }

    private Bitmap getStopLogo(){
        Bitmap stopLogo;

        int iconConstant=1;//Integer.parseInt(mMainViewModel.getListOfStopMarkIcons());

        switch (iconConstant){

            case 1:
                stopLogo = BitmapFactory.decodeResource(getResources(), R.drawable.stop_banner_hand);//stop_banner_hand
                break;
            case 2:
                stopLogo = BitmapFactory.decodeResource(getResources(), R.drawable.stop_banner_red);//stop_banner_red
                break;
            case 3:
                stopLogo = BitmapFactory.decodeResource(getResources(), R.drawable.stop_red_circle);//stop_red_circle
                break;

            default:
                stopLogo = BitmapFactory.decodeResource(getResources(), R.drawable.stop_banner_hand);
        }
        return generateSmallIcon(stopLogo);
    }

    private Bitmap generateSmallIcon(Bitmap icon){
        return Bitmap.createScaledBitmap(icon, 64, 64, false);
    }

    private void takeMapScreenshot(List<LatLng> route, GoogleMap.SnapshotReadyCallback snapshotCallback) {
        zoomRoute(route);
        mMap.snapshot(snapshotCallback);
    }

    private void zoomRoute(List<LatLng> route) {
        if (route.size() == 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.get(0), DEFAULT_ZOOM));
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng point : route) {
                builder.include(point);
            }
            int padding = 100;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
            mMap.moveCamera(cu);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
    private int getCompressionRatioValue(){
        int compressionRatioValue; //это и есть настоящая величина в % которую
        //надо подставить в параметры bitmap.compress

        int compressionRatio=Integer.parseInt(mMainViewModel.getListCompressionRatioValue());

        switch(compressionRatio){
            case 1:
                compressionRatioValue=25;
                break;
            case 2:
                compressionRatioValue=50;
                break;
            case 3:
                compressionRatioValue=75;
                break;
            case 4:
                compressionRatioValue=100;
                break;
            default:
                compressionRatioValue=100;

        }
        return compressionRatioValue;

    }

    private int getColorsValue(){
        int colorConstantValue;//это величина константы из файла Colors.xml
        //которая будет позднее использвана
        int colorConstant=Integer.parseInt(mMainViewModel.getListLineColorValue());
        switch (colorConstant){
            case 1:
                colorConstantValue=R.color.color_line_red;
                break;
            case 2:
                colorConstantValue=R.color.color_line_green;
                break;
            case 3:
                colorConstantValue=R.color.color_Line_blue;
                break;
            case 4:
                colorConstantValue=R.color.color_line_yellow;
                break;
            case 5:
                colorConstantValue=R.color.color_line_black;
                break;
            default:
                colorConstantValue=R.color.color_line_black;
                break;

        }

        return getResources().getColor(colorConstantValue) ;
    }

    private float getWidthValue(){
        int width;
        width = Integer.parseInt(mMainViewModel.getListOfLineWidthValue());
        return Float.valueOf(width);
    }

}
