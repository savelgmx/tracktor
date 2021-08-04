package com.elegion.tracktor.ui.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.elegion.tracktor.App;

import javax.inject.Inject;

import toothpick.Toothpick;

public class ReadUserPreferences implements UserRepository{

    private static SharedPreferences sharedPreferences;
    //private  SharedPreferences sharedPreferences;

    @Inject
    public ReadUserPreferences(SharedPreferences sharedPreferences) {


        this.sharedPreferences = sharedPreferences;

    }

    @Override
    public String getListPreferenceValue(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("sex", "Man");

    }

    @Override
    public String getUserWeight(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("weight", "60");
    }

    @Override
    public String getUserHeight(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("height", "170");
    }

    @Override
    public String getUserAge(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("age", "33");
    }


    public String getCompressionRatio(Context context){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("compression_ratio","25");
    }

//хранит значение выбора вида активности (бег,ходьба,велосипед) на экране результатов
    public static boolean saveKindOfActivityId(int kindOfActivityID, Context context) {
        Log.i("saveSessionId", String.valueOf(kindOfActivityID));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("activity",kindOfActivityID);
        return editor.commit();
    }
    //читаем значение выбора
    public int getKindOfActivityId(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt("activity", 0);

     }

}
