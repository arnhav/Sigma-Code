package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Version;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

public class AboutActivity extends Activity {
    WifiDirectAssistant f161a;

    /* renamed from: com.qualcomm.ftccommon.AboutActivity.1 */
    class C00211 extends ArrayAdapter<Item> {
        final /* synthetic */ AboutActivity f160a;

        C00211(AboutActivity aboutActivity, Context context, int i, int i2) {
            this.f160a = aboutActivity;
            super(context, i, i2);
        }

        public /* synthetic */ Object getItem(int i) {
            return m96a(i);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = (TextView) view.findViewById(16908308);
            TextView textView2 = (TextView) view.findViewById(16908309);
            Item a = m96a(position);
            textView.setText(a.title);
            textView2.setText(a.info);
            return view;
        }

        public int getCount() {
            return 3;
        }

        public Item m96a(int i) {
            switch (i) {
                case SpiSlaveResponseEvent.OK /*0*/:
                    return m93a();
                case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                    return m94b();
                case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                    return m95c();
                default:
                    return new Item();
            }
        }

        private Item m93a() {
            Item item = new Item();
            item.title = "App Version";
            try {
                item.info = this.f160a.getPackageManager().getPackageInfo(this.f160a.getPackageName(), 0).versionName;
            } catch (NameNotFoundException e) {
                item.info = e.getMessage();
            }
            return item;
        }

        private Item m94b() {
            Item item = new Item();
            item.title = "Library Version";
            item.info = Version.getLibraryVersion();
            return item;
        }

        private Item m95c() {
            Item item = new Item();
            item.title = "Wifi Direct Information";
            item.info = "unavailable";
            StringBuilder stringBuilder = new StringBuilder();
            if (this.f160a.f161a != null && this.f160a.f161a.isEnabled()) {
                stringBuilder.append("Name: ").append(this.f160a.f161a.getDeviceName());
                if (this.f160a.f161a.isGroupOwner()) {
                    stringBuilder.append("\nIP Address").append(this.f160a.f161a.getGroupOwnerAddress().getHostAddress());
                    stringBuilder.append("\nPassphrase: ").append(this.f160a.f161a.getPassphrase());
                    stringBuilder.append("\nGroup Owner");
                } else if (this.f160a.f161a.isConnected()) {
                    stringBuilder.append("\nGroup Owner: ").append(this.f160a.f161a.getGroupOwnerName());
                    stringBuilder.append("\nConnected");
                } else {
                    stringBuilder.append("\nNo connection information");
                }
                item.info = stringBuilder.toString();
            }
            return item;
        }
    }

    public static class Item {
        public String info;
        public String title;

        public Item() {
            this.title = BuildConfig.VERSION_NAME;
            this.info = BuildConfig.VERSION_NAME;
        }
    }

    public AboutActivity() {
        this.f161a = null;
    }

    protected void onStart() {
        super.onStart();
        setContentView(C0035R.layout.activity_about);
        ListView listView = (ListView) findViewById(C0035R.id.aboutList);
        try {
            this.f161a = WifiDirectAssistant.getWifiDirectAssistant(null);
            this.f161a.enable();
        } catch (NullPointerException e) {
            RobotLog.m329i("Cannot start Wifi Direct Assistant");
            this.f161a = null;
        }
        listView.setAdapter(new C00211(this, this, 17367044, 16908308));
    }

    protected void onStop() {
        super.onStop();
        if (this.f161a != null) {
            this.f161a.disable();
        }
    }
}
