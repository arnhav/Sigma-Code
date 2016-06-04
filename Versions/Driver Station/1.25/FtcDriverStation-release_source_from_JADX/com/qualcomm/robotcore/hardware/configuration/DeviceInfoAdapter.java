package com.qualcomm.robotcore.hardware.configuration;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.HashMap;
import java.util.Map;

public class DeviceInfoAdapter extends BaseAdapter implements ListAdapter {
    private Map<SerialNumber, ControllerConfiguration> f452a;
    private SerialNumber[] f453b;
    private Context f454c;
    private int f455d;
    private int f456e;

    public DeviceInfoAdapter(Activity context, int list_id, Map<SerialNumber, ControllerConfiguration> deviceControllers) {
        this.f452a = new HashMap();
        this.f454c = context;
        this.f452a = deviceControllers;
        this.f453b = (SerialNumber[]) deviceControllers.keySet().toArray(new SerialNumber[deviceControllers.size()]);
        this.f455d = list_id;
        this.f456e = this.f456e;
    }

    public int getCount() {
        return this.f452a.size();
    }

    public Object getItem(int arg0) {
        return this.f452a.get(this.f453b[arg0]);
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) this.f454c).getLayoutInflater().inflate(this.f455d, parent, false);
        }
        ((TextView) convertView.findViewById(16908309)).setText(this.f453b[pos].toString());
        ((TextView) convertView.findViewById(16908308)).setText(((ControllerConfiguration) this.f452a.get(this.f453b[pos])).getName());
        return convertView;
    }
}
