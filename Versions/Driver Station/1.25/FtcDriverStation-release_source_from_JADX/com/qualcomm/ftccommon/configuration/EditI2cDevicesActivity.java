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
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.ftcdriverstation.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;

public class EditI2cDevicesActivity extends Activity {
    private Utility f275a;
    private View f276b;
    private View f277c;
    private View f278d;
    private View f279e;
    private View f280f;
    private View f281g;
    private ArrayList<DeviceConfiguration> f282h;
    private OnItemSelectedListener f283i;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditI2cDevicesActivity.1 */
    class C00601 implements OnItemSelectedListener {
        final /* synthetic */ EditI2cDevicesActivity f272a;

        C00601(EditI2cDevicesActivity editI2cDevicesActivity) {
            this.f272a = editI2cDevicesActivity;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String obj = parent.getItemAtPosition(pos).toString();
            LinearLayout linearLayout = (LinearLayout) view.getParent().getParent().getParent();
            if (obj.equalsIgnoreCase(ConfigurationType.NOTHING.toString())) {
                this.f272a.m182a(linearLayout);
            } else {
                this.f272a.m183a(linearLayout, obj);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditI2cDevicesActivity.a */
    private class C0061a implements TextWatcher {
        final /* synthetic */ EditI2cDevicesActivity f273a;
        private int f274b;

        private C0061a(EditI2cDevicesActivity editI2cDevicesActivity, View view) {
            this.f273a = editI2cDevicesActivity;
            this.f274b = Integer.parseInt(((TextView) view.findViewById(C0035R.id.port_number_i2c)).getText().toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            ((DeviceConfiguration) this.f273a.f282h.get(this.f274b)).setName(editable.toString());
        }
    }

    public EditI2cDevicesActivity() {
        this.f282h = new ArrayList();
        this.f283i = new C00601(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.i2cs);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f275a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f276b = getLayoutInflater().inflate(C0035R.layout.i2c_device, (LinearLayout) findViewById(C0035R.id.linearLayout_i2c0), true);
        ((TextView) this.f276b.findViewById(C0035R.id.port_number_i2c)).setText("0");
        this.f277c = getLayoutInflater().inflate(C0035R.layout.i2c_device, (LinearLayout) findViewById(C0035R.id.linearLayout_i2c1), true);
        ((TextView) this.f277c.findViewById(C0035R.id.port_number_i2c)).setText("1");
        this.f278d = getLayoutInflater().inflate(C0035R.layout.i2c_device, (LinearLayout) findViewById(C0035R.id.linearLayout_i2c2), true);
        ((TextView) this.f278d.findViewById(C0035R.id.port_number_i2c)).setText("2");
        this.f279e = getLayoutInflater().inflate(C0035R.layout.i2c_device, (LinearLayout) findViewById(C0035R.id.linearLayout_i2c3), true);
        ((TextView) this.f279e.findViewById(C0035R.id.port_number_i2c)).setText("3");
        this.f280f = getLayoutInflater().inflate(C0035R.layout.i2c_device, (LinearLayout) findViewById(C0035R.id.linearLayout_i2c4), true);
        ((TextView) this.f280f.findViewById(C0035R.id.port_number_i2c)).setText("4");
        this.f281g = getLayoutInflater().inflate(C0035R.layout.i2c_device, (LinearLayout) findViewById(C0035R.id.linearLayout_i2c5), true);
        ((TextView) this.f281g.findViewById(C0035R.id.port_number_i2c)).setText("5");
    }

    protected void onStart() {
        super.onStart();
        this.f275a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String str : extras.keySet()) {
                this.f282h.add(Integer.parseInt(str), (DeviceConfiguration) extras.getSerializable(str));
            }
            for (int i = 0; i < this.f282h.size(); i++) {
                View a = m176a(i);
                DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f282h.get(i);
                m179a(a);
                m186b(a, deviceConfiguration);
                m180a(a, deviceConfiguration);
            }
        }
    }

    private void m180a(View view, DeviceConfiguration deviceConfiguration) {
        Spinner spinner = (Spinner) view.findViewById(C0035R.id.choiceSpinner_i2c);
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
        if (deviceConfiguration.isEnabled()) {
            spinner.setSelection(arrayAdapter.getPosition(deviceConfiguration.getType().toString()));
        } else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(this.f283i);
    }

    private void m186b(View view, DeviceConfiguration deviceConfiguration) {
        EditText editText = (EditText) view.findViewById(C0035R.id.editTextResult_i2c);
        if (deviceConfiguration.isEnabled()) {
            editText.setText(deviceConfiguration.getName());
            editText.setEnabled(true);
            return;
        }
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        editText.setEnabled(false);
    }

    private void m179a(View view) {
        ((EditText) view.findViewById(C0035R.id.editTextResult_i2c)).addTextChangedListener(new C0061a(view, null));
    }

    private View m176a(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.OK /*0*/:
                return this.f276b;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f277c;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return this.f278d;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return this.f279e;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                return this.f280f;
            case BuildConfig.VERSION_CODE /*5*/:
                return this.f281g;
            default:
                return null;
        }
    }

    public void saveI2cDevices(View v) {
        m178a();
    }

    private void m178a() {
        Bundle bundle = new Bundle();
        for (int i = 0; i < this.f282h.size(); i++) {
            bundle.putSerializable(String.valueOf(i), (DeviceConfiguration) this.f282h.get(i));
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        setResult(-1, intent);
        finish();
    }

    public void cancelI2cDevices(View v) {
        setResult(0, new Intent());
        finish();
    }

    private void m182a(LinearLayout linearLayout) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_i2c)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_i2c);
        editText.setEnabled(false);
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        ((DeviceConfiguration) this.f282h.get(parseInt)).setEnabled(false);
    }

    private void m183a(LinearLayout linearLayout, String str) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_i2c)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_i2c);
        editText.setEnabled(true);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f282h.get(parseInt);
        deviceConfiguration.setType(deviceConfiguration.typeFromString(str));
        deviceConfiguration.setEnabled(true);
        m181a(editText, deviceConfiguration);
    }

    private void m181a(EditText editText, DeviceConfiguration deviceConfiguration) {
        if (editText.getText().toString().equalsIgnoreCase(DeviceConfiguration.DISABLED_DEVICE_NAME)) {
            editText.setText(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            deviceConfiguration.setName(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            return;
        }
        editText.setText(deviceConfiguration.getName());
    }
}
