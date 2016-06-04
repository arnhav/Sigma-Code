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
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;

public class EditAnalogOutputDevicesActivity extends Activity {
    private Utility f244a;
    private View f245b;
    private View f246c;
    private ArrayList<DeviceConfiguration> f247d;
    private OnItemSelectedListener f248e;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditAnalogOutputDevicesActivity.1 */
    class C00541 implements OnItemSelectedListener {
        final /* synthetic */ EditAnalogOutputDevicesActivity f241a;

        C00541(EditAnalogOutputDevicesActivity editAnalogOutputDevicesActivity) {
            this.f241a = editAnalogOutputDevicesActivity;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String obj = parent.getItemAtPosition(pos).toString();
            LinearLayout linearLayout = (LinearLayout) view.getParent().getParent().getParent();
            if (obj.equalsIgnoreCase(ConfigurationType.NOTHING.toString())) {
                this.f241a.m154a(linearLayout);
            } else {
                this.f241a.m155a(linearLayout, obj);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditAnalogOutputDevicesActivity.a */
    private class C0055a implements TextWatcher {
        final /* synthetic */ EditAnalogOutputDevicesActivity f242a;
        private int f243b;

        private C0055a(EditAnalogOutputDevicesActivity editAnalogOutputDevicesActivity, View view) {
            this.f242a = editAnalogOutputDevicesActivity;
            this.f243b = Integer.parseInt(((TextView) view.findViewById(C0035R.id.port_number_analogOutput)).getText().toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            ((DeviceConfiguration) this.f242a.f247d.get(this.f243b)).setName(editable.toString());
        }
    }

    public EditAnalogOutputDevicesActivity() {
        this.f247d = new ArrayList();
        this.f248e = new C00541(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.analog_outputs);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f244a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f245b = getLayoutInflater().inflate(C0035R.layout.analog_output_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogOutput0), true);
        ((TextView) this.f245b.findViewById(C0035R.id.port_number_analogOutput)).setText("0");
        this.f246c = getLayoutInflater().inflate(C0035R.layout.analog_output_device, (LinearLayout) findViewById(C0035R.id.linearLayout_analogOutput1), true);
        ((TextView) this.f246c.findViewById(C0035R.id.port_number_analogOutput)).setText("1");
    }

    protected void onStart() {
        super.onStart();
        this.f244a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String str : extras.keySet()) {
                this.f247d.add(Integer.parseInt(str), (DeviceConfiguration) extras.getSerializable(str));
            }
            for (int i = 0; i < this.f247d.size(); i++) {
                View a = m148a(i);
                DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f247d.get(i);
                m151a(a);
                m158b(a, deviceConfiguration);
                m152a(a, deviceConfiguration);
            }
        }
    }

    private void m152a(View view, DeviceConfiguration deviceConfiguration) {
        Spinner spinner = (Spinner) view.findViewById(C0035R.id.choiceSpinner_analogOutput);
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
        if (deviceConfiguration.isEnabled()) {
            spinner.setSelection(arrayAdapter.getPosition(deviceConfiguration.getType().toString()));
        } else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(this.f248e);
    }

    private void m158b(View view, DeviceConfiguration deviceConfiguration) {
        EditText editText = (EditText) view.findViewById(C0035R.id.editTextResult_analogOutput);
        if (deviceConfiguration.isEnabled()) {
            editText.setText(deviceConfiguration.getName());
            editText.setEnabled(true);
            return;
        }
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        editText.setEnabled(false);
    }

    private void m151a(View view) {
        ((EditText) view.findViewById(C0035R.id.editTextResult_analogOutput)).addTextChangedListener(new C0055a(view, null));
    }

    private View m148a(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.OK /*0*/:
                return this.f245b;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f246c;
            default:
                return null;
        }
    }

    public void saveanalogOutputDevices(View v) {
        m150a();
    }

    private void m150a() {
        Bundle bundle = new Bundle();
        for (int i = 0; i < this.f247d.size(); i++) {
            bundle.putSerializable(String.valueOf(i), (DeviceConfiguration) this.f247d.get(i));
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        setResult(-1, intent);
        finish();
    }

    public void cancelanalogOutputDevices(View v) {
        setResult(0, new Intent());
        finish();
    }

    private void m154a(LinearLayout linearLayout) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_analogOutput)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_analogOutput);
        editText.setEnabled(false);
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        ((DeviceConfiguration) this.f247d.get(parseInt)).setEnabled(false);
    }

    private void m155a(LinearLayout linearLayout, String str) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.port_number_analogOutput)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_analogOutput);
        editText.setEnabled(true);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f247d.get(parseInt);
        deviceConfiguration.setType(deviceConfiguration.typeFromString(str));
        deviceConfiguration.setEnabled(true);
        m153a(editText, deviceConfiguration);
    }

    private void m153a(EditText editText, DeviceConfiguration deviceConfiguration) {
        if (editText.getText().toString().equalsIgnoreCase(DeviceConfiguration.DISABLED_DEVICE_NAME)) {
            editText.setText(BuildConfig.VERSION_NAME);
            deviceConfiguration.setName(BuildConfig.VERSION_NAME);
            return;
        }
        editText.setText(deviceConfiguration.getName());
    }
}
