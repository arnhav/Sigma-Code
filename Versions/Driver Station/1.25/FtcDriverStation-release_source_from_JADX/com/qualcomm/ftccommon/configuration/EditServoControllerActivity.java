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
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;

public class EditServoControllerActivity extends Activity {
    public static final String EDIT_SERVO_ACTIVITY = "Edit Servo ControllerConfiguration Activity";
    private Utility f348a;
    private ServoControllerConfiguration f349b;
    private ArrayList<DeviceConfiguration> f350c;
    private EditText f351d;
    private View f352e;
    private View f353f;
    private View f354g;
    private View f355h;
    private View f356i;
    private View f357j;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditServoControllerActivity.1 */
    class C00701 implements OnClickListener {
        final /* synthetic */ EditText f343a;
        final /* synthetic */ DeviceConfiguration f344b;
        final /* synthetic */ EditServoControllerActivity f345c;

        C00701(EditServoControllerActivity editServoControllerActivity, EditText editText, DeviceConfiguration deviceConfiguration) {
            this.f345c = editServoControllerActivity;
            this.f343a = editText;
            this.f344b = deviceConfiguration;
        }

        public void onClick(View view) {
            if (((CheckBox) view).isChecked()) {
                this.f343a.setEnabled(true);
                this.f343a.setText(BuildConfig.VERSION_NAME);
                this.f344b.setEnabled(true);
                this.f344b.setName(BuildConfig.VERSION_NAME);
                return;
            }
            this.f343a.setEnabled(false);
            this.f344b.setEnabled(false);
            this.f344b.setName(DeviceConfiguration.DISABLED_DEVICE_NAME);
            this.f343a.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditServoControllerActivity.a */
    private class C0071a implements TextWatcher {
        final /* synthetic */ EditServoControllerActivity f346a;
        private int f347b;

        private C0071a(EditServoControllerActivity editServoControllerActivity, View view) {
            this.f346a = editServoControllerActivity;
            this.f347b = Integer.parseInt(((TextView) view.findViewById(C0035R.id.port_number_servo)).getText().toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            ((DeviceConfiguration) this.f346a.f350c.get(this.f347b - 1)).setName(editable.toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.servos);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f348a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f351d = (EditText) findViewById(C0035R.id.servocontroller_name);
        this.f352e = getLayoutInflater().inflate(C0035R.layout.servo, (LinearLayout) findViewById(C0035R.id.linearLayout_servo1), true);
        ((TextView) this.f352e.findViewById(C0035R.id.port_number_servo)).setText("1");
        this.f353f = getLayoutInflater().inflate(C0035R.layout.servo, (LinearLayout) findViewById(C0035R.id.linearLayout_servo2), true);
        ((TextView) this.f353f.findViewById(C0035R.id.port_number_servo)).setText("2");
        this.f354g = getLayoutInflater().inflate(C0035R.layout.servo, (LinearLayout) findViewById(C0035R.id.linearLayout_servo3), true);
        ((TextView) this.f354g.findViewById(C0035R.id.port_number_servo)).setText("3");
        this.f355h = getLayoutInflater().inflate(C0035R.layout.servo, (LinearLayout) findViewById(C0035R.id.linearLayout_servo4), true);
        ((TextView) this.f355h.findViewById(C0035R.id.port_number_servo)).setText("4");
        this.f356i = getLayoutInflater().inflate(C0035R.layout.servo, (LinearLayout) findViewById(C0035R.id.linearLayout_servo5), true);
        ((TextView) this.f356i.findViewById(C0035R.id.port_number_servo)).setText("5");
        this.f357j = getLayoutInflater().inflate(C0035R.layout.servo, (LinearLayout) findViewById(C0035R.id.linearLayout_servo6), true);
        ((TextView) this.f357j.findViewById(C0035R.id.port_number_servo)).setText("6");
    }

    protected void onStart() {
        super.onStart();
        this.f348a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Serializable serializableExtra = getIntent().getSerializableExtra(EDIT_SERVO_ACTIVITY);
        if (serializableExtra != null) {
            this.f349b = (ServoControllerConfiguration) serializableExtra;
            this.f350c = (ArrayList) this.f349b.getServos();
        }
        this.f351d.setText(this.f349b.getName());
        TextView textView = (TextView) findViewById(C0035R.id.servo_controller_serialNumber);
        CharSequence serialNumber = this.f349b.getSerialNumber().toString();
        if (serialNumber.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
            serialNumber = "No serial number";
        }
        textView.setText(serialNumber);
        for (int i = 0; i < this.f350c.size(); i++) {
            m228c(i + 1);
            m226a(i + 1);
            m227b(i + 1);
        }
    }

    private void m226a(int i) {
        View d = m229d(i);
        ((EditText) d.findViewById(C0035R.id.editTextResult_servo)).addTextChangedListener(new C0071a(d, null));
    }

    private void m227b(int i) {
        View d = m229d(i);
        CheckBox checkBox = (CheckBox) d.findViewById(C0035R.id.checkbox_port_servo);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f350c.get(i - 1);
        if (deviceConfiguration.isEnabled()) {
            checkBox.setChecked(true);
            ((EditText) d.findViewById(C0035R.id.editTextResult_servo)).setText(deviceConfiguration.getName());
            return;
        }
        checkBox.setChecked(true);
        checkBox.performClick();
    }

    private void m228c(int i) {
        View d = m229d(i);
        ((CheckBox) d.findViewById(C0035R.id.checkbox_port_servo)).setOnClickListener(new C00701(this, (EditText) d.findViewById(C0035R.id.editTextResult_servo), (DeviceConfiguration) this.f350c.get(i - 1)));
    }

    private View m229d(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f352e;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return this.f353f;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return this.f354g;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                return this.f355h;
            case com.qualcomm.ftcdriverstation.BuildConfig.VERSION_CODE /*5*/:
                return this.f356i;
            case FT4222_STATUS.FT4222_INVALID_PARAMETER /*6*/:
                return this.f357j;
            default:
                return null;
        }
    }

    public void saveServoController(View v) {
        m225a();
    }

    private void m225a() {
        Intent intent = new Intent();
        this.f349b.addServos(this.f350c);
        this.f349b.setName(this.f351d.getText().toString());
        intent.putExtra(EDIT_SERVO_ACTIVITY, this.f349b);
        setResult(-1, intent);
        finish();
    }

    public void cancelServoController(View v) {
        setResult(0, new Intent());
        finish();
    }
}
