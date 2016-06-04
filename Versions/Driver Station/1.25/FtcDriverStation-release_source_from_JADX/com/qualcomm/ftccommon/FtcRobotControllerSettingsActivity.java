package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class FtcRobotControllerSettingsActivity extends Activity {

    public static class SettingsFragment extends PreferenceFragment {
        OnPreferenceClickListener f175a;

        /* renamed from: com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity.SettingsFragment.1 */
        class C00281 implements OnPreferenceClickListener {
            final /* synthetic */ SettingsFragment f171a;

            C00281(SettingsFragment settingsFragment) {
                this.f171a = settingsFragment;
            }

            public boolean onPreferenceClick(Preference preference) {
                try {
                    this.f171a.startActivity(this.f171a.getActivity().getPackageManager().getLaunchIntentForPackage(LaunchActivityConstantsList.ZTE_WIFI_CHANNEL_EDITOR_PACKAGE));
                } catch (NullPointerException e) {
                    Toast.makeText(this.f171a.getActivity(), "Unable to launch ZTE WifiChannelEditor", 0).show();
                }
                return true;
            }
        }

        /* renamed from: com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity.SettingsFragment.2 */
        class C00292 implements OnPreferenceClickListener {
            final /* synthetic */ SettingsFragment f172a;

            C00292(SettingsFragment settingsFragment) {
                this.f172a = settingsFragment;
            }

            public boolean onPreferenceClick(Preference preference) {
                this.f172a.startActivity(new Intent(preference.getIntent().getAction()));
                return true;
            }
        }

        /* renamed from: com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity.SettingsFragment.3 */
        class C00303 implements OnPreferenceClickListener {
            final /* synthetic */ SettingsFragment f173a;

            C00303(SettingsFragment settingsFragment) {
                this.f173a = settingsFragment;
            }

            public boolean onPreferenceClick(Preference preference) {
                this.f173a.startActivity(new Intent("android.settings.SETTINGS"));
                return true;
            }
        }

        /* renamed from: com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity.SettingsFragment.4 */
        class C00314 implements OnPreferenceClickListener {
            final /* synthetic */ SettingsFragment f174a;

            C00314(SettingsFragment settingsFragment) {
                this.f174a = settingsFragment;
            }

            public boolean onPreferenceClick(Preference preference) {
                this.f174a.startActivityForResult(new Intent(preference.getIntent().getAction()), 3);
                return true;
            }
        }

        public SettingsFragment() {
            this.f175a = new C00314(this);
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(C0035R.xml.preferences);
            findPreference(getString(C0035R.string.pref_launch_configure)).setOnPreferenceClickListener(this.f175a);
            findPreference(getString(C0035R.string.pref_launch_autoconfigure)).setOnPreferenceClickListener(this.f175a);
            if (Build.MANUFACTURER.equalsIgnoreCase(Device.MANUFACTURER_ZTE) && Build.MODEL.equalsIgnoreCase(Device.MODEL_ZTE_SPEED)) {
                findPreference(getString(C0035R.string.pref_launch_settings)).setOnPreferenceClickListener(new C00281(this));
            } else {
                findPreference(getString(C0035R.string.pref_launch_settings)).setOnPreferenceClickListener(new C00292(this));
            }
            if (Build.MODEL.equals(Device.MODEL_FOXDA_FL7007)) {
                findPreference(getString(C0035R.string.pref_launch_settings)).setOnPreferenceClickListener(new C00303(this));
            }
        }

        public void onActivityResult(int request, int result, Intent intent) {
            if (request == 3 && result == -1) {
                getActivity().setResult(-1, intent);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(16908290, new SettingsFragment()).commit();
    }
}
