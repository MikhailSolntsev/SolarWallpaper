package com.debugg3r.android.solarwallpaper.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.debugg3r.android.solarwallpaper.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    static public class SettingsFragment extends PreferenceFragment
            implements SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_solar);

            PreferenceScreen preferenceScreen = getPreferenceScreen();
            SharedPreferences preferences = preferenceScreen.getSharedPreferences();
            preferences.registerOnSharedPreferenceChangeListener(this);

            for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
                Preference pref = preferenceScreen.getPreference(i);
                if (!(pref instanceof SwitchPreference)) {
                    setPreferenceSummary(pref, preferences.getString(pref.getKey(), ""));
                }
            }
        }

        private void setPreferenceSummary(Preference pref, String value) {
            if (pref instanceof ListPreference) {
                // For list preferences, figure out the label of the selected value
                ListPreference listPreference = (ListPreference) pref;
                int prefIndex = listPreference.findIndexOfValue(value);
                if (prefIndex >= 0) {
                    // Set the summary to that label
                    listPreference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            }
        }

        @Override
        public void onDestroy() {
            SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
            preferences.unregisterOnSharedPreferenceChangeListener(this);

            super.onDestroy();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference = findPreference(key);
            if (preference != null) {
                if (!(preference instanceof SwitchPreference)) {
                    setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
                }
            }
        }
    }

}
