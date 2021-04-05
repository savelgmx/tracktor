package com.elegion.tracktor.ui.results;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Bitmap;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.ui.preferences.UserRepository;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import java.util.List;

import javax.inject.Inject;

import toothpick.Toothpick;



/**
 * ResultsViewModel. В нем также нужно сделать инжект репозитория
 */
public class ResultsViewModel extends ViewModel {

    @Inject
    IRepository<Track> mRepository;
    @Inject
    UserRepository mUserRepository;
    @Inject
    RealmRepository mRealmRepository;

    @Inject
    Context mContext;




    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();
    private MutableLiveData<Bitmap> mImage = new MutableLiveData<>();

    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mDistanceText = new MutableLiveData<>();
    private MutableLiveData<String> mAverageSpeed = new MutableLiveData<>();
    private MutableLiveData<String> mSpentCalories = new MutableLiveData<>();

    private MutableLiveData<String> mDateText = new MutableLiveData<>();

    private MutableLiveData<String> mComment = new MutableLiveData<>();

    private String TAG =ResultsViewModel.class.getSimpleName();

    private Double spentCalories;


    public ResultsViewModel() {
        super();

        Toothpick.inject(this, App.getAppScope());


    }



    public void loadTracks() {
        if (mTracks.getValue() == null || mTracks.getValue().isEmpty()) {
            mTracks.postValue(mRepository.getAll());
        }
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }

    public void loadImage(long mTrackId) {
        Track track = mRepository.getItem(mTrackId);
        String distance = StringUtil.getDistanceText(track.getDistance());
        String time = StringUtil.getTimeText(track.getDuration());
        Bitmap bitmapImage = ScreenshotMaker.fromBase64(track.getImageBase64());
        String averageSpeed = StringUtil.getAverageSpeedText(track.getDistance(), track.getDuration());
        String comment = StringUtil.getCommentsText(track.getComment());

        mAverageSpeed.postValue(averageSpeed);
        mTimeText.postValue(time);
        mDistanceText.postValue(distance);
        mImage.postValue(bitmapImage);
        mComment.postValue(comment);
    }




    public void calculateSpentCalories(int checkedIndexId ){
 /*
        https://calorizator.ru/article/body/bmr-calculation
        Вычисляем калории согласно формуле Миффлина-Джеора
        Эта формула появилась на свет в 1990 году.
         Её считают одной из наиболее точных.
                Для расчета также необходимо знать вес, рост и возраст.

        Мужчины: BMR = (10 × вес в кг) + (6,25 × рост в см) - (5 × возраст) + 5
        Женщины: BMR = (10 × вес в кг) + (6,25 × рост в см) - (5 × возраст) - 161

        https://www.calc.ru/Formula-Mifflinasan-Zheora.html
         Доработанный вариант формулы Миффлина-Сан Жеора, в отличие от упрощенного дает
         более точную информацию и учитывает степень физической активности человека:

        для мужчин: (10 x вес (кг) + 6.25 x рост (см) – 5 x возраст (г) + 5) x A;
        для женщин: (10 x вес (кг) + 6.25 x рост (см) – 5 x возраст (г) – 161) x A.
        A – это уровень активности человека, его различают обычно по пяти степеням физических нагрузок в сутки:

        Минимальная активность: A = 1,2.
        Слабая активность: A = 1,375.
        Средняя активность: A = 1,55.
        Высокая активность: A = 1,725.
        Экстра-активность: A = 1,9 (под эту категорию обычно подпадают люди, занимающиеся, например,
        тяжелой атлетикой, или другими силовыми видами спорта с ежедневными тренировками,
        а также те, кто выполняет тяжелую физическую работу).

*/
        Double levelOfActivity=0.0;
        //int checkedIndexId = 0;

        switch(checkedIndexId){
            case 0:
                levelOfActivity=1.375;//0 Велосипед Слабая активность
                break;
            case 1:
                levelOfActivity=1.55;//1 Ходьба Средняя активность:
                break;
            case 2:
                levelOfActivity=1.725;//2 Бег Высокая активность

        }

        String genderValue=mUserRepository.getListPreferenceValue(mContext);

        switch (genderValue){

            case "Man":
                spentCalories= (10 * Double.valueOf(mUserRepository.getUserWeight(mContext)))
                        + (6.25 * Double.valueOf(mUserRepository.getUserHeight(mContext)))
                        + (5*Double.valueOf(mUserRepository.getUserAge(mContext)))+5;
                break;
            case "Woman":
                spentCalories= (10 * Double.valueOf(mUserRepository.getUserWeight(mContext)))
                        + (6.25 * Double.valueOf(mUserRepository.getUserHeight(mContext)))
                        + (5*Double.valueOf(mUserRepository.getUserAge(mContext)))-161;
                break;
            default:
                spentCalories= (10 * Double.valueOf(mUserRepository.getUserWeight(mContext)))
                        + (6.25 * Double.valueOf(mUserRepository.getUserHeight(mContext)))
                        + (5*Double.valueOf(mUserRepository.getUserAge(mContext)))+5;
        }

        spentCalories= spentCalories*levelOfActivity;

        mSpentCalories.postValue(StringUtil.getSpentCaloriesText(spentCalories));


    }

    public void getStringDate(long mTrackId){
        Track track = mRepository.getItem(mTrackId);
        String date = StringUtil.getDateText(track.getDate());
        mDateText.postValue(date);
    }


    public MutableLiveData<Bitmap> getImage() {
        return mImage;
    }
    public MutableLiveData<String> getTime() {
        return mTimeText;
    }
    public MutableLiveData<String> getDistance() {
        return mDistanceText;
    }
    public MutableLiveData<String> getAverageSpeed() {
        return mAverageSpeed;
    }
    public MutableLiveData<String> getSpentCalories(){ return mSpentCalories; }

    public MutableLiveData<String > getDate(){return mDateText; }
    public MutableLiveData<String> getComment(){return mComment;}

    public void updateComment(long mTrackId,String comment){
        Track track = mRepository.getItem(mTrackId);



        mRealmRepository.createAndUpdateTrackFrom(mTrackId,
                track.getDuration(),
                track.getDistance(),
                track.getImageBase64(),
                comment

        );
    }




}

