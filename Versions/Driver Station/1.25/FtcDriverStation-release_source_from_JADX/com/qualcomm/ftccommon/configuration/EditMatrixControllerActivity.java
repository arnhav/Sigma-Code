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
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;

public class EditMatrixControllerActivity extends Activity {
    public static final String EDIT_MATRIX_ACTIVITY = "Edit Matrix ControllerConfiguration Activity";
    private Utility f307a;
    private MatrixControllerConfiguration f308b;
    private ArrayList<DeviceConfiguration> f309c;
    private ArrayList<DeviceConfiguration> f310d;
    private EditText f311e;
    private View f312f;
    private View f313g;
    private View f314h;
    private View f315i;
    private View f316j;
    private View f317k;
    private View f318l;
    private View f319m;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditMatrixControllerActivity.1 */
    class C00641 implements OnClickListener {
        final /* synthetic */ EditText f302a;
        final /* synthetic */ DeviceConfiguration f303b;
        final /* synthetic */ EditMatrixControllerActivity f304c;

        C00641(EditMatrixControllerActivity editMatrixControllerActivity, EditText editText, DeviceConfiguration deviceConfiguration) {
            this.f304c = editMatrixControllerActivity;
            this.f302a = editText;
            this.f303b = deviceConfiguration;
        }

        public void onClick(View view) {
            if (((CheckBox) view).isChecked()) {
                this.f302a.setEnabled(true);
                this.f302a.setText(BuildConfig.VERSION_NAME);
                this.f303b.setEnabled(true);
                this.f303b.setName(BuildConfig.VERSION_NAME);
                return;
            }
            this.f302a.setEnabled(false);
            this.f303b.setEnabled(false);
            this.f303b.setName(DeviceConfiguration.DISABLED_DEVICE_NAME);
            this.f302a.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditMatrixControllerActivity.a */
    private class C0065a implements TextWatcher {
        final /* synthetic */ EditMatrixControllerActivity f305a;
        private DeviceConfiguration f306b;

        private C0065a(EditMatrixControllerActivity editMatrixControllerActivity, DeviceConfiguration deviceConfiguration) {
            this.f305a = editMatrixControllerActivity;
            this.f306b = deviceConfiguration;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            this.f306b.setName(editable.toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.matrices);
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f307a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f311e = (EditText) findViewById(C0035R.id.matrixcontroller_name);
        this.f312f = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix1), true);
        ((TextView) this.f312f.findViewById(C0035R.id.port_number_matrix)).setText("1");
        this.f313g = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix2), true);
        ((TextView) this.f313g.findViewById(C0035R.id.port_number_matrix)).setText("2");
        this.f314h = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix3), true);
        ((TextView) this.f314h.findViewById(C0035R.id.port_number_matrix)).setText("3");
        this.f315i = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix4), true);
        ((TextView) this.f315i.findViewById(C0035R.id.port_number_matrix)).setText("4");
        this.f316j = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix5), true);
        ((TextView) this.f316j.findViewById(C0035R.id.port_number_matrix)).setText("1");
        this.f317k = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix6), true);
        ((TextView) this.f317k.findViewById(C0035R.id.port_number_matrix)).setText("2");
        this.f318l = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix7), true);
        ((TextView) this.f318l.findViewById(C0035R.id.port_number_matrix)).setText("3");
        this.f319m = getLayoutInflater().inflate(C0035R.layout.matrix_devices, (LinearLayout) findViewById(C0035R.id.linearLayout_matrix8), true);
        ((TextView) this.f319m.findViewById(C0035R.id.port_number_matrix)).setText("4");
    }

    protected void onStart() {
        int i = 0;
        super.onStart();
        this.f307a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        Serializable serializableExtra = getIntent().getSerializableExtra(EDIT_MATRIX_ACTIVITY);
        if (serializableExtra != null) {
            this.f308b = (MatrixControllerConfiguration) serializableExtra;
            this.f309c = (ArrayList) this.f308b.getMotors();
            this.f310d = (ArrayList) this.f308b.getServos();
        }
        this.f311e.setText(this.f308b.getName());
        for (int i2 = 0; i2 < this.f309c.size(); i2++) {
            View b = m206b(i2 + 1);
            m207b(i2 + 1, b, this.f309c);
            m205a(b, (DeviceConfiguration) this.f309c.get(i2));
            m204a(i2 + 1, b, this.f309c);
        }
        while (i < this.f310d.size()) {
            View a = m202a(i + 1);
            m207b(i + 1, a, this.f310d);
            m205a(a, (DeviceConfiguration) this.f310d.get(i));
            m204a(i + 1, a, this.f310d);
            i++;
        }
    }

    private void m205a(View view, DeviceConfiguration deviceConfiguration) {
        ((EditText) view.findViewById(C0035R.id.editTextResult_matrix)).addTextChangedListener(new C0065a(deviceConfiguration, null));
    }

    private void m204a(int i, View view, ArrayList<DeviceConfiguration> arrayList) {
        CheckBox checkBox = (CheckBox) view.findViewById(C0035R.id.checkbox_port_matrix);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) arrayList.get(i - 1);
        if (deviceConfiguration.isEnabled()) {
            checkBox.setChecked(true);
            ((EditText) view.findViewById(C0035R.id.editTextResult_matrix)).setText(deviceConfiguration.getName());
            return;
        }
        checkBox.setChecked(true);
        checkBox.performClick();
    }

    private void m207b(int i, View view, ArrayList<DeviceConfiguration> arrayList) {
        ((CheckBox) view.findViewById(C0035R.id.checkbox_port_matrix)).setOnClickListener(new C00641(this, (EditText) view.findViewById(C0035R.id.editTextResult_matrix), (DeviceConfiguration) arrayList.get(i - 1)));
    }

    private View m202a(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f312f;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return this.f313g;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return this.f314h;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                return this.f315i;
            default:
                return null;
        }
    }

    private View m206b(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f316j;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return this.f317k;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return this.f318l;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                return this.f319m;
            default:
                return null;
        }
    }

    public void saveMatrixController(View v) {
        m203a();
    }

    private void m203a() {
        Intent intent = new Intent();
        this.f308b.addServos(this.f310d);
        this.f308b.addMotors(this.f309c);
        this.f308b.setName(this.f311e.getText().toString());
        intent.putExtra(EDIT_MATRIX_ACTIVITY, this.f308b);
        setResult(-1, intent);
        finish();
    }

    public void cancelMatrixController(View v) {
        setResult(0, new Intent());
        finish();
    }
}
