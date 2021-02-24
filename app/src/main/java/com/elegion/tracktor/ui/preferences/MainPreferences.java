package com.elegion.tracktor.ui.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.elegion.tracktor.R;

public class MainPreferences extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainPreferences.class.getSimpleName();

    public static MainPreferences newInstance() {
        return new MainPreferences();
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.tr_pref, rootKey);
        Log.d(TAG, "OnCreatePreferences: " + rootKey);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureSummaryEntries();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummaryFor(findPreference(key));
        Log.d(TAG,"OnChange Shared Preferences key"+key);
    }

    private void configureSummaryEntries() {

        setSummaryFor(findPreference(getString(R.string.pref_key_unit)));
        setSummaryFor(findPreference(getString(R.string.pref_key_shutdown)));
        setSummaryFor(findPreference(getString(R.string.pref_key_weight)));
        setSummaryFor(findPreference(getString(R.string.pref_key_height)));
        setSummaryFor(findPreference(getString(R.string.pref_key_age)));
    }

    private void setSummaryFor(Preference preference) {

        Log.d(TAG, "SetSummaryForPreferences: " + preference);


        if (preference instanceof ListPreference) {
            preference.setSummary(((ListPreference) preference).getEntry());

            Log.d(TAG,"getEntry "+((ListPreference) preference).getEntry());

        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(((EditTextPreference) preference).getText());

            Log.d(TAG,"getText "+((EditTextPreference) preference).getText());


        } else {
            Log.d(TAG, "check preferences type");
        }
    }

    public String getAgeEntry(){

        return setSummaryForEditTextEntry(findPreference("age"));
    }

    private String setSummaryForEditTextEntry(Preference preference){

        Log.d(TAG,"getText AGE "+((EditTextPreference) preference).getText());

        preference.setSummary(((EditTextPreference) preference).getText());

        String value = ((EditTextPreference) preference).getText();


        return value;

    }

}
