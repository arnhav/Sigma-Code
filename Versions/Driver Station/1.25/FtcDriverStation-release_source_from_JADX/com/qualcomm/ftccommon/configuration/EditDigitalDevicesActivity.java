package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.ftcdriverstation.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;

public class EditDigitalDevicesActivity extends Activity {
    private Utility f261a;
    private View f262b;
    private View f263c;
    private View f264d;
    private View f265e;
    private View f266f;
    private View f267g;
    private View f268h;
    private View f269i;
    private ArrayList<DeviceConfiguration> f270j;
    private OnItemSelectedListener f271k;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditDigitalDevicesActivity.1 */
    class C00581 implements OnItemSelectedListener {
        final /* synthetic */ EditDigitalDevicesActivity f258a;

        C00581(EditDigitalDevicesActivity editDigitalDevicesActivity) {
            this.f258a = editDigitalDevicesActivity;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String obj = parent.getItemAtPosition(pos).toString();
            LinearLayout linearLayout = (LinearLayout) view.getParent().getParent().getParent();
            if (obj.equalsIgnoreCase(ConfigurationType.NOTHING.toString())) {
                this.f258a.m171a(linearLayout);
            } else {
                this.f258a.m172a(linearLayout, obj);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditDigitalDevicesActivity.a */
    private class C0059a implements TextWatcher {
        final /* synthetic */ EditDigitalDevicesActivity f259a;
        private int f260b;

        private C0059a(EditDigitalDevicesActivity editDigitalDevicesActivity, View view) {
            this.f259a = editDigitalDevicesActivity;
            this.f260b = Integer.parseInt(((TextView) view.findViewById(C0035R.id.port_number_digital_device)).getText().toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            ((DeviceConfiguration) this.f259a.f270j.get(this.f260b)).setName(editable.toString());
        }
    }

    public EditDigitalDevicesActivity() {
        this.f270j = new ArrayList();
        this.f271k = new C00581(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.digital_devices);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f261a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f262b = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device0), true);
        ((TextView) this.f262b.findViewById(C0035R.id.port_number_digital_device)).setText("0");
        this.f263c = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device1), true);
        ((TextView) this.f263c.findViewById(C0035R.id.port_number_digital_device)).setText("1");
        this.f264d = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device2), true);
        ((TextView) this.f264d.findViewById(C0035R.id.port_number_digital_device)).setText("2");
        this.f265e = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device3), true);
        ((TextView) this.f265e.findViewById(C0035R.id.port_number_digital_device)).setText("3");
        this.f266f = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device4), true);
        ((TextView) this.f266f.findViewById(C0035R.id.port_number_digital_device)).setText("4");
        this.f267g = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device5), true);
        ((TextView) this.f267g.findViewById(C0035R.id.port_number_digital_device)).setText("5");
        this.f268h = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device6), true);
        ((TextView) this.f268h.findViewById(C0035R.id.port_number_digital_device)).setText("6");
        this.f269i = getLayoutInflater().inflate(C0035R.layout.digital_device, (LinearLayout) findViewById(C0035R.id.linearLayout_digital_device7), true);
        ((TextView) this.f269i.findViewById(C0035R.id.port_number_digital_device)).setText("7");
    }

    protected void onStart() {
        super.onStart();
        this.f261a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String str : extras.keySet()) {
                this.f270j.add(Integer.parseInt(str), (DeviceConfiguration) extras.getSerializable(str));
            }
            for (int i = 0; i < this.f270j.size(); i++) {
                View a = m165a(i);
                DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f270j.get(i);
                m168a(a);
                m175b(a, deviceConfiguration);
                m169a(a, deviceConfiguration);
            }
        }
    }

    private void m169a(View view, DeviceConfiguration deviceConfiguration) {
        Spinner spinner = (Spinner) view.findViewById(C0035R.id.choiceSpinner_digital_device);
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
        if (deviceConfiguration.isEnabled()) {
            spinner.setSelection(arrayAdapter.getPosition(deviceConfiguration.getType().toString()));
        } else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(this.f271k);
    }

    private void m175b(View view, DeviceConfiguration deviceConfiguration) {
        EditText editText = (EditText) view.findViewById(C0035R.id.editTextResult_digital_device);
        if (deviceConfiguration.isEnabled()) {
            editText.setText(deviceConfiguration.getName());
            editText.setEnabled(true);
            return;
        }
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        editText.setEnabled(false);
    }

    private void m168a(View view) {
        ((EditText) view.findViewById(C0035R.id.editTextResult_digital_device)).addTextChangedListener(new C0059a(view, null));
    }

    private View m165a(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.OK /*0*/:
                return this.f262b;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f263c;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return this.f264d;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return this.f265e;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                return this.f266f;
            case BuildConfig.VERSION_CODE /*5*/:
                return this.f267g;
            case FT4222_STATUS.FT4222_INVALID_PARAMETER /*6*/:
                return this.f268h;
            case FT4222_STATUS.FT4222_INVALID_BAUD_RATE /*7*/:
                return this.f269i;
            default:
                return null;
        }
    }

    public void saveDigitalDevices(View v) {
        m167a();
    }

    private void m167a() {
        Bundle bundle = new Bundle();
        for (int i = 0; i < this.f270j.size(); i++) {
            bundle.putSerializable(String.valueOf(i), (Serializable) this.f270j.get(i));
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        setResult(-1, intent);
        finish();
    }

    public void cancelDigitalDevices(View v) {
        setResult(0, new Intent());
        finish();
    }

    private void m171a(LinearLayout linearLayout) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_digital_device)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_digital_device);
        editText.setEnabled(false);
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        ((DeviceConfiguration) this.f270j.get(parseInt)).setEnabled(false);
    }

    private void m172a(LinearLayout linearLayout, String str) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_digital_device)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_digital_device);
        editText.setEnabled(true);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f270j.get(parseInt);
        deviceConfiguration.setType(deviceConfiguration.typeFromString(str));
        deviceConfiguration.setEnabled(true);
        m170a(editText, deviceConfiguration);
    }

    private void m170a(EditText editText, DeviceConfiguration deviceConfiguration) {
        if (editText.getText().toString().equalsIgnoreCase(DeviceConfiguration.DISABLED_DEVICE_NAME)) {
            editText.setText(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            deviceConfiguration.setName(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            return;
        }
        editText.setText(deviceConfiguration.getName());
    }
}
