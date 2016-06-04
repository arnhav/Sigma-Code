package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;

public class EditMotorControllerActivity extends Activity {
    public static final String EDIT_MOTOR_CONTROLLER_CONFIG = "EDIT_MOTOR_CONTROLLER_CONFIG";
    private Utility f322a;
    private MotorControllerConfiguration f323b;
    private ArrayList<DeviceConfiguration> f324c;
    private MotorConfiguration f325d;
    private MotorConfiguration f326e;
    private EditText f327f;
    private boolean f328g;
    private boolean f329h;
    private CheckBox f330i;
    private CheckBox f331j;
    private EditText f332k;
    private EditText f333l;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditMotorControllerActivity.1 */
    class C00661 implements OnClickListener {
        final /* synthetic */ EditMotorControllerActivity f320a;

        C00661(EditMotorControllerActivity editMotorControllerActivity) {
            this.f320a = editMotorControllerActivity;
        }

        public void onClick(View view) {
            if (((CheckBox) view).isChecked()) {
                this.f320a.f328g = true;
                this.f320a.f332k.setEnabled(true);
                this.f320a.f332k.setText(BuildConfig.VERSION_NAME);
                this.f320a.f325d.setPort(1);
                this.f320a.f325d.setType(ConfigurationType.MOTOR);
                return;
            }
            this.f320a.f328g = false;
            this.f320a.f332k.setEnabled(false);
            this.f320a.f332k.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
            this.f320a.f325d.setType(ConfigurationType.NOTHING);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditMotorControllerActivity.2 */
    class C00672 implements OnClickListener {
        final /* synthetic */ EditMotorControllerActivity f321a;

        C00672(EditMotorControllerActivity editMotorControllerActivity) {
            this.f321a = editMotorControllerActivity;
        }

        public void onClick(View view) {
            if (((CheckBox) view).isChecked()) {
                this.f321a.f329h = true;
                this.f321a.f333l.setEnabled(true);
                this.f321a.f333l.setText(BuildConfig.VERSION_NAME);
                this.f321a.f326e.setPort(2);
                this.f321a.f325d.setType(ConfigurationType.MOTOR);
                return;
            }
            this.f321a.f329h = false;
            this.f321a.f333l.setEnabled(false);
            this.f321a.f333l.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
            this.f321a.f325d.setType(ConfigurationType.NOTHING);
        }
    }

    public EditMotorControllerActivity() {
        this.f324c = new ArrayList();
        this.f325d = new MotorConfiguration(1);
        this.f326e = new MotorConfiguration(2);
        this.f328g = true;
        this.f329h = true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.motors);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f322a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f327f = (EditText) findViewById(C0035R.id.controller_name);
        this.f330i = (CheckBox) findViewById(C0035R.id.checkbox_port7);
        this.f331j = (CheckBox) findViewById(C0035R.id.checkbox_port6);
        this.f332k = (EditText) findViewById(C0035R.id.editTextResult_analogInput7);
        this.f333l = (EditText) findViewById(C0035R.id.editTextResult_analogInput6);
    }

    protected void onStart() {
        super.onStart();
        this.f322a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Serializable serializableExtra = getIntent().getSerializableExtra(EDIT_MOTOR_CONTROLLER_CONFIG);
        if (serializableExtra != null) {
            this.f323b = (MotorControllerConfiguration) serializableExtra;
            this.f324c = (ArrayList) this.f323b.getMotors();
            this.f325d = (MotorConfiguration) this.f324c.get(0);
            this.f326e = (MotorConfiguration) this.f324c.get(1);
            this.f327f.setText(this.f323b.getName());
            TextView textView = (TextView) findViewById(C0035R.id.motor_controller_serialNumber);
            CharSequence serialNumber = this.f323b.getSerialNumber().toString();
            if (serialNumber.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
                serialNumber = "No serial number";
            }
            textView.setText(serialNumber);
            this.f332k.setText(this.f325d.getName());
            this.f333l.setText(this.f326e.getName());
            m209a();
            m210a(this.f325d, this.f330i);
            m213b();
            m210a(this.f326e, this.f331j);
        }
    }

    private void m210a(MotorConfiguration motorConfiguration, CheckBox checkBox) {
        if (motorConfiguration.getName().equals(DeviceConfiguration.DISABLED_DEVICE_NAME) || motorConfiguration.getType() == ConfigurationType.NOTHING) {
            checkBox.setChecked(true);
            checkBox.performClick();
            return;
        }
        checkBox.setChecked(true);
    }

    private void m209a() {
        this.f330i.setOnClickListener(new C00661(this));
    }

    private void m213b() {
        this.f331j.setOnClickListener(new C00672(this));
    }

    public void saveMotorController(View v) {
        m216c();
    }

    private void m216c() {
        Intent intent = new Intent();
        ArrayList arrayList = new ArrayList();
        if (this.f328g) {
            MotorConfiguration motorConfiguration = new MotorConfiguration(this.f332k.getText().toString());
            motorConfiguration.setEnabled(true);
            motorConfiguration.setPort(1);
            arrayList.add(motorConfiguration);
        } else {
            arrayList.add(new MotorConfiguration(1));
        }
        if (this.f329h) {
            motorConfiguration = new MotorConfiguration(this.f333l.getText().toString());
            motorConfiguration.setEnabled(true);
            motorConfiguration.setPort(2);
            arrayList.add(motorConfiguration);
        } else {
            arrayList.add(new MotorConfiguration(2));
        }
        this.f323b.addMotors(arrayList);
        this.f323b.setName(this.f327f.getText().toString());
        intent.putExtra(EDIT_MOTOR_CONTROLLER_CONFIG, this.f323b);
        setResult(-1, intent);
        finish();
    }

    public void cancelMotorController(View v) {
        setResult(0, new Intent());
        finish();
    }
}
