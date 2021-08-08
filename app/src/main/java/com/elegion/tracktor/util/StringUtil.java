package com.elegion.tracktor.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.ui.preferences.ReadUserPreferences;
import com.elegion.tracktor.ui.preferences.UserRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class StringUtil {

    private static UserRepository mUserRepository;
    private static ReadUserPreferences mUserPreferences;
    private static Context mContext;



    private  static Double averageSpeed;
    private static String unit;

    public static String getTimeText(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String getDistanceText(double value) {

        return round(value, 0) + " м.";
    }
    private static String getUnitDistance(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int iUnit = Integer.valueOf(preferences.getString("unit", "-1"));
        switch (iUnit) {
            case 1:
                return " км.";
            case 2:
                return " мили";
            case 3:
                return " м.";
            case 4:
                return " feets";
            default:
                return " м.";
        }
    }


    public static String round(double value, int places) {
        return String.format("%." + places + "f", value);
    }
    //auto translating m/s to km / hour or mile/hour
    public static String getVelocityText(Context context, double value) {
        String unit = getUnitDistance(context) ;
        switch (unit.trim()){
            case "км.":
                return " "+round(DistanceConverter.convertFromMetersToKm(value),1) + " км/час";
            case "мили":
                return " "+round(DistanceConverter.convertFromMetersToMiles(value),1 ) + " миль/час";
            case " м.":
                return " " +round(value,1) + "/сек.";
            case "feets":
                return " " +round(DistanceConverter.convertFromMetersToFeets(value),1) + " фут./час";
            default:
                return " " +" м."+"/сек.";
        }
    }
    public static double getVelocity(Track track) {
        return track.getDuration() != 0 ? track.getDistance() / track.getDuration() : 0.0;
    }

    public static String getAverageSpeedText(Double distance, long time){

        if ((Integer.valueOf((int) time)>0)&(distance>0)){
            averageSpeed =(Double.valueOf(distance)/Double.valueOf(time));
        }else {averageSpeed=0.0;}


        return round(averageSpeed,0)+" м/с.";
    }

    public static String  getSpentCaloriesText(Double spentCalories){
        return round(spentCalories,2)+" калорий";
    }

    public static String getDateText(Date date){
        String pattern = "dd.MM.yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String getCommentsText(String comment){
        if (comment==null){
            return "Введите комментарий";
        }else {
            if (comment.length() == 0) {
                return "Введите комментарий";
            } else return comment.trim();
        }
    }
    public static String getActionText(int codeAction) {
        String actionText="";
        switch (codeAction) {
            case 0:
                actionText = "Велосипед";
                break;
            case 1:
                actionText = "Ходьба";
                break;
            case 2:
                actionText = "Бег";
                break;
        }
        return actionText;
    }


}
