package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {

    public static class SettingsFragment extends PreferenceFragment {
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(C0099R.xml.preferences);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0099R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(16908290, new SettingsFragment()).commit();
    }
}
