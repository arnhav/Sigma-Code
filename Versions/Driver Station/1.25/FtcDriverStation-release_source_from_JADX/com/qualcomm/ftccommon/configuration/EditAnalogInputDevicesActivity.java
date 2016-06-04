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

public class EditAnalogInputDevicesActivity extends Activity {
    private Utility f230a;
    private View f231b;
    private View f232c;
    private View f233d;
    private View f234e;
    private View f235f;
    private View f236g;
    private View f237h;
    private View f238i;
    private ArrayList<DeviceConfiguration> f239j;
    private OnItemSelectedListener f240k;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditAnalogInputDevicesActivity.1 */
    class C00521 implements OnItemSelectedListener {
        final /* synthetic */ EditAnalogInputDevicesActivity f227a;

        C00521(EditAnalogInputDevicesActivity editAnalogInputDevicesActivity) {
            this.f227a = editAnalogInputDevicesActivity;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String obj = parent.getItemAtPosition(pos).toString();
            LinearLayout linearLayout = (LinearLayout) view.getParent().getParent().getParent();
            if (obj.equalsIgnoreCase(ConfigurationType.NOTHING.toString())) {
                this.f227a.m143a(linearLayout);
            } else {
                this.f227a.m144a(linearLayout, obj);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditAnalogInputDevicesActivity.a */
    private class C0053a implements TextWatcher {
        final /* synthetic */ EditAnalogInputDevicesActivity f228a;
        private int f229b;

        private C0053a(EditAnalogInputDevicesActivity editAnalogInputDevicesActivity, View view) {
            this.f228a = editAnalogInputDevicesActivity;
            this.f229b = Integer.parseInt(((TextView) view.findViewById(C0035R.id.port_number_analogInput)).getText().toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            ((DeviceConfiguration) this.f228a.f239j.get(this.f229b)).setName(editable.toString());
        }
    }

    public EditAnalogInputDevicesActivity() {
        this.f239j = new ArrayList();
        this.f240k = new C00521(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.analog_inputs);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f230a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f231b = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput0), true);
        ((TextView) this.f231b.findViewById(C0035R.id.port_number_analogInput)).setText("0");
        this.f232c = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput1), true);
        ((TextView) this.f232c.findViewById(C0035R.id.port_number_analogInput)).setText("1");
        this.f233d = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput2), true);
        ((TextView) this.f233d.findViewById(C0035R.id.port_number_analogInput)).setText("2");
        this.f234e = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput3), true);
        ((TextView) this.f234e.findViewById(C0035R.id.port_number_analogInput)).setText("3");
        this.f235f = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput4), true);
        ((TextView) this.f235f.findViewById(C0035R.id.port_number_analogInput)).setText("4");
        this.f236g = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput5), true);
        ((TextView) this.f236g.findViewById(C0035R.id.port_number_analogInput)).setText("5");
        this.f237h = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput6), true);
        ((TextView) this.f237h.findViewById(C0035R.id.port_number_analogInput)).setText("6");
        this.f238i = getLayoutInflater().inflate(C0035R.layout.analog_input_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogInput7), true);
        ((TextView) this.f238i.findViewById(C0035R.id.port_number_analogInput)).setText("7");
    }

    protected void onStart() {
        super.onStart();
        this.f230a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String str : extras.keySet()) {
                this.f239j.add(Integer.parseInt(str), (DeviceConfiguration) extras.getSerializable(str));
            }
            for (int i = 0; i < this.f239j.size(); i++) {
                View a = m137a(i);
                DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f239j.get(i);
                m140a(a);
                m147b(a, deviceConfiguration);
                m141a(a, deviceConfiguration);
            }
        }
    }

    private void m141a(View view, DeviceConfiguration deviceConfiguration) {
        Spinner spinner = (Spinner) view.findViewById(C0035R.id.choiceSpinner_analogInput);
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
        if (deviceConfiguration.isEnabled()) {
            spinner.setSelection(arrayAdapter.getPosition(deviceConfiguration.getType().toString()));
        } else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(this.f240k);
    }

    private void m147b(View view, DeviceConfiguration deviceConfiguration) {
        EditText editText = (EditText) view.findViewById(C0035R.id.editTextResult_analogInput);
        if (deviceConfiguration.isEnabled()) {
            editText.setText(deviceConfiguration.getName());
            editText.setEnabled(true);
            return;
        }
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        editText.setEnabled(false);
    }

    private void m140a(View view) {
        ((EditText) view.findViewById(C0035R.id.editTextResult_analogInput)).addTextChangedListener(new C0053a(view, null));
    }

    private View m137a(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.OK /*0*/:
                return this.f231b;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f232c;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return this.f233d;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return this.f234e;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                return this.f235f;
            case BuildConfig.VERSION_CODE /*5*/:
                return this.f236g;
            case FT4222_STATUS.FT4222_INVALID_PARAMETER /*6*/:
                return this.f237h;
            case FT4222_STATUS.FT4222_INVALID_BAUD_RATE /*7*/:
                return this.f238i;
            default:
                return null;
        }
    }

    public void saveAnalogInputDevices(View v) {
        m139a();
    }

    private void m139a() {
        Bundle bundle = new Bundle();
        for (int i = 0; i < this.f239j.size(); i++) {
            bundle.putSerializable(String.valueOf(i), (Serializable) this.f239j.get(i));
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        setResult(-1, intent);
        finish();
    }

    public void cancelAnalogInputDevices(View v) {
        setResult(0, new Intent());
        finish();
    }

    private void m143a(LinearLayout linearLayout) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_analogInput)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_analogInput);
        editText.setEnabled(false);
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        ((DeviceConfiguration) this.f239j.get(parseInt)).setEnabled(false);
    }

    private void m144a(LinearLayout linearLayout, String str) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_analogInput)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_analogInput);
        editText.setEnabled(true);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f239j.get(parseInt);
        deviceConfiguration.setType(deviceConfiguration.typeFromString(str));
        deviceConfiguration.setEnabled(true);
        m142a(editText, deviceConfiguration);
    }

    private void m142a(EditText editText, DeviceConfiguration deviceConfiguration) {
        if (editText.getText().toString().equalsIgnoreCase(DeviceConfiguration.DISABLED_DEVICE_NAME)) {
            editText.setText(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            deviceConfiguration.setName(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            return;
        }
        editText.setText(deviceConfiguration.getName());
    }
}
