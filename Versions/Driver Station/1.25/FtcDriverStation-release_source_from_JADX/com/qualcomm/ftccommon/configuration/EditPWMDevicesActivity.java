package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;

public class EditPWMDevicesActivity extends Activity {
    public static final String EDIT_PWM_DEVICES = "EDIT_PWM_DEVICES";
    private Utility f339a;
    private View f340b;
    private View f341c;
    private ArrayList<DeviceConfiguration> f342d;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditPWMDevicesActivity.1 */
    class C00681 implements OnClickListener {
        final /* synthetic */ EditText f334a;
        final /* synthetic */ DeviceConfiguration f335b;
        final /* synthetic */ EditPWMDevicesActivity f336c;

        C00681(EditPWMDevicesActivity editPWMDevicesActivity, EditText editText, DeviceConfiguration deviceConfiguration) {
            this.f336c = editPWMDevicesActivity;
            this.f334a = editText;
            this.f335b = deviceConfiguration;
        }

        public void onClick(View view) {
            if (((CheckBox) view).isChecked()) {
                this.f334a.setEnabled(true);
                this.f334a.setText(BuildConfig.VERSION_NAME);
                this.f335b.setEnabled(true);
                this.f335b.setName(BuildConfig.VERSION_NAME);
                return;
            }
            this.f334a.setEnabled(false);
            this.f335b.setEnabled(false);
            this.f335b.setName(DeviceConfiguration.DISABLED_DEVICE_NAME);
            this.f334a.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditPWMDevicesActivity.a */
    private class C0069a implements TextWatcher {
        final /* synthetic */ EditPWMDevicesActivity f337a;
        private int f338b;

        private C0069a(EditPWMDevicesActivity editPWMDevicesActivity, View view) {
            this.f337a = editPWMDevicesActivity;
            this.f338b = Integer.parseInt(((TextView) view.findViewById(C0035R.id.port_number_pwm)).getText().toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            ((DeviceConfiguration) this.f337a.f342d.get(this.f338b)).setName(editable.toString());
        }
    }

    public EditPWMDevicesActivity() {
        this.f342d = new ArrayList();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.pwms);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f339a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f340b = getLayoutInflater().inflate(C0035R.layout.pwm_device, (LinearLayout) findViewById(C0035R.id.linearLayout_pwm0), true);
        ((TextView) this.f340b.findViewById(C0035R.id.port_number_pwm)).setText("0");
        this.f341c = getLayoutInflater().inflate(C0035R.layout.pwm_device, (LinearLayout) findViewById(C0035R.id.linearLayout_pwm1), true);
        ((TextView) this.f341c.findViewById(C0035R.id.port_number_pwm)).setText("1");
    }

    protected void onStart() {
        super.onStart();
        this.f339a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String str : extras.keySet()) {
                this.f342d.add(Integer.parseInt(str), (DeviceConfiguration) extras.getSerializable(str));
            }
            for (int i = 0; i < this.f342d.size(); i++) {
                m222c(i);
                m221b(i);
                m220a(i);
            }
        }
    }

    private void m220a(int i) {
        View d = m223d(i);
        CheckBox checkBox = (CheckBox) d.findViewById(C0035R.id.checkbox_port_pwm);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f342d.get(i);
        if (deviceConfiguration.isEnabled()) {
            checkBox.setChecked(true);
            ((EditText) d.findViewById(C0035R.id.editTextResult_pwm)).setText(deviceConfiguration.getName());
            return;
        }
        checkBox.setChecked(true);
        checkBox.performClick();
    }

    private void m221b(int i) {
        View d = m223d(i);
        ((EditText) d.findViewById(C0035R.id.editTextResult_pwm)).addTextChangedListener(new C0069a(d, null));
    }

    private void m222c(int i) {
        View d = m223d(i);
        ((CheckBox) d.findViewById(C0035R.id.checkbox_port_pwm)).setOnClickListener(new C00681(this, (EditText) d.findViewById(C0035R.id.editTextResult_pwm), (DeviceConfiguration) this.f342d.get(i)));
    }

    private View m223d(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.OK /*0*/:
                return this.f340b;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f341c;
            default:
                return null;
        }
    }

    public void savePWMDevices(View v) {
        m219a();
    }

    private void m219a() {
        Bundle bundle = new Bundle();
        for (int i = 0; i < this.f342d.size(); i++) {
            bundle.putSerializable(String.valueOf(i), (Serializable) this.f342d.get(i));
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        setResult(-1, intent);
        finish();
    }

    public void cancelPWMDevices(View v) {
        setResult(0, new Intent());
        finish();
    }
}
