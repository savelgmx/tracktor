package com.elegion.tracktor.ui.map;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;

import com.elegion.tracktor.ui.preferences.UserRepository;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import toothpick.Toothpick;

public class MainViewModel extends ViewModel {


    @Inject
    RealmRepository mRealmRepository;

    @Inject
    UserRepository mUserRepository;
    @Inject
    Context mContext;




    private MutableLiveData<Boolean> startEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> stopEnabled = new MutableLiveData<>();

    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mDistanceText = new MutableLiveData<>();

    private long mDurationRaw;
    private double mDistanceRaw;


    @Inject
    public MainViewModel() {
        Toothpick.inject(this, App.getAppScope());
        EventBus.getDefault().register(this);
        startEnabled.setValue(true);
        stopEnabled.setValue(false);

     //   getListUnitsValue();

    }

    public void switchButtons() {
        startEnabled.setValue(!startEnabled.getValue());
        stopEnabled.setValue(!stopEnabled.getValue());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTimer(UpdateTimerEvent event) {
        mTimeText.postValue(StringUtil.getTimeText(event.getSeconds()));
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
        mDurationRaw = event.getSeconds();
        mDistanceRaw = event.getDistance();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event) {
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
        mDistanceRaw = event.getDistance();

        startEnabled.postValue(false);
        stopEnabled.postValue(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event) {
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
    }

    public MutableLiveData<String> getTimeText() {
        return mTimeText;
    }

    public MutableLiveData<Boolean> getStartEnabled() {
        return startEnabled;
    }

    public MutableLiveData<Boolean> getStopEnabled() {
        return stopEnabled;
    }

    public MutableLiveData<String> getDistanceText() {
        return mDistanceText;
    }

    @Override
    protected void onCleared() {
        EventBus.getDefault().unregister(this);
        super.onCleared();
    }

    public void clear() {
        mTimeText.setValue("");
        mDistanceText.setValue("");
    }

    public long saveResults(String base54image) {

        return mRealmRepository.createAndInsertTrackFrom(mDurationRaw, mDistanceRaw, base54image);
    }
    public String getListUnitsValue(){
        return mUserRepository.getListOfDistanceUnits(mContext);
    }

    public String getListCompressionRatioValue(){
        return mUserRepository.getListOfCompressionRatio(mContext);
    }

    public String getListLineColorValue(){
        return mUserRepository.getListOfLineColorsValue(mContext);
    }
    public String getListOfLineWidthValue(){
        return mUserRepository.getListOfLineWidthValue(mContext);
    }


    public String getListOfStartMarkIcons(){
        return mUserRepository.getListOfStartMarkIcons(mContext);
    }
    public String getListOfStopMarkIcons(){
        return mUserRepository.getListOfStopMarkIcons(mContext);
    }

}