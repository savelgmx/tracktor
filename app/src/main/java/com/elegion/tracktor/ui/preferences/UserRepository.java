package com.elegion.tracktor.ui.preferences;

import android.content.Context;

public interface UserRepository {

    String getListPreferenceValue(Context context);

    String getUserWeight(Context context) ; // editTextPreferece [weight]

    String getUserHeight(Context context);// editTextPreferece [height]

    String getUserAge(Context context);// editTextPreferece [age]

    String getListOfDistanceUnits(Context context);

    String  getListOfCompressionRatio(Context context);

    String getListOfLineColorsValue(Context context);

    String getListOfLineWidthValue(Context context);

    String getListOfStartMarkIcons(Context context);

    String getListOfStopMarkIcons(Context context);


}
