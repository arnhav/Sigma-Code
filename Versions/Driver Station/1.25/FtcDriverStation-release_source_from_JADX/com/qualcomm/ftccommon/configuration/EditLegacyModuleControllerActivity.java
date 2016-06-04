package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.ftcdriverstation.BuildConfig;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditLegacyModuleControllerActivity extends Activity {
    public static final String EDIT_LEGACY_CONFIG = "EDIT_LEGACY_CONFIG";
    public static final int EDIT_MATRIX_CONTROLLER_REQUEST_CODE = 103;
    public static final int EDIT_MOTOR_CONTROLLER_REQUEST_CODE = 101;
    public static final int EDIT_SERVO_CONTROLLER_REQUEST_CODE = 102;
    private static boolean f288a;
    private Utility f289b;
    private String f290c;
    private Context f291d;
    private LegacyModuleControllerConfiguration f292e;
    private EditText f293f;
    private ArrayList<DeviceConfiguration> f294g;
    private View f295h;
    private View f296i;
    private View f297j;
    private View f298k;
    private View f299l;
    private View f300m;
    private OnItemSelectedListener f301n;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditLegacyModuleControllerActivity.1 */
    class C00621 implements OnItemSelectedListener {
        final /* synthetic */ EditLegacyModuleControllerActivity f284a;

        C00621(EditLegacyModuleControllerActivity editLegacyModuleControllerActivity) {
            this.f284a = editLegacyModuleControllerActivity;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
            String obj = parent.getItemAtPosition(pos).toString();
            LinearLayout linearLayout = (LinearLayout) view.getParent().getParent().getParent();
            if (obj.equalsIgnoreCase(ConfigurationType.NOTHING.toString())) {
                this.f284a.m194a(linearLayout);
            } else {
                this.f284a.m195a(linearLayout, obj);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditLegacyModuleControllerActivity.a */
    private class C0063a implements TextWatcher {
        final /* synthetic */ EditLegacyModuleControllerActivity f285a;
        private int f286b;
        private boolean f287c;

        private C0063a(EditLegacyModuleControllerActivity editLegacyModuleControllerActivity) {
            this.f285a = editLegacyModuleControllerActivity;
            this.f287c = false;
            this.f287c = true;
        }

        private C0063a(EditLegacyModuleControllerActivity editLegacyModuleControllerActivity, View view) {
            this.f285a = editLegacyModuleControllerActivity;
            this.f287c = false;
            this.f286b = Integer.parseInt(((TextView) view.findViewById(C0035R.id.portNumber)).getText().toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String obj = editable.toString();
            if (this.f287c) {
                this.f285a.f292e.setName(obj);
            } else {
                ((DeviceConfiguration) this.f285a.f294g.get(this.f286b)).setName(obj);
            }
        }
    }

    public EditLegacyModuleControllerActivity() {
        this.f294g = new ArrayList();
        this.f301n = new C00621(this);
    }

    static {
        f288a = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.legacy);
        this.f295h = getLayoutInflater().inflate(C0035R.layout.simple_device, (LinearLayout) findViewById(C0035R.id.linearLayout0), true);
        ((TextView) this.f295h.findViewById(C0035R.id.portNumber)).setText("0");
        this.f296i = getLayoutInflater().inflate(C0035R.layout.simple_device, (LinearLayout) findViewById(C0035R.id.linearLayout1), true);
        ((TextView) this.f296i.findViewById(C0035R.id.portNumber)).setText("1");
        this.f297j = getLayoutInflater().inflate(C0035R.layout.simple_device, (LinearLayout) findViewById(C0035R.id.linearLayout2), true);
        ((TextView) this.f297j.findViewById(C0035R.id.portNumber)).setText("2");
        this.f298k = getLayoutInflater().inflate(C0035R.layout.simple_device, (LinearLayout) findViewById(C0035R.id.linearLayout3), true);
        ((TextView) this.f298k.findViewById(C0035R.id.portNumber)).setText("3");
        this.f299l = getLayoutInflater().inflate(C0035R.layout.simple_device, (LinearLayout) findViewById(C0035R.id.linearLayout4), true);
        ((TextView) this.f299l.findViewById(C0035R.id.portNumber)).setText("4");
        this.f300m = getLayoutInflater().inflate(C0035R.layout.simple_device, (LinearLayout) findViewById(C0035R.id.linearLayout5), true);
        ((TextView) this.f300m.findViewById(C0035R.id.portNumber)).setText("5");
        this.f291d = this;
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f289b = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f293f = (EditText) findViewById(C0035R.id.device_interface_module_name);
    }

    protected void onStart() {
        super.onStart();
        this.f289b.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        this.f290c = this.f289b.getFilenameFromPrefs(C0035R.string.pref_hardware_config_filename, Utility.NO_FILE);
        Serializable serializableExtra = getIntent().getSerializableExtra(EDIT_LEGACY_CONFIG);
        if (serializableExtra != null) {
            this.f292e = (LegacyModuleControllerConfiguration) serializableExtra;
            this.f294g = (ArrayList) this.f292e.getDevices();
            this.f293f.setText(this.f292e.getName());
            this.f293f.addTextChangedListener(new C0063a());
            ((TextView) findViewById(C0035R.id.legacy_serialNumber)).setText(this.f292e.getSerialNumber().toString());
            for (int i = 0; i < this.f294g.size(); i++) {
                DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f294g.get(i);
                if (f288a) {
                    RobotLog.m328e("[onStart] module name: " + deviceConfiguration.getName() + ", port: " + deviceConfiguration.getPort() + ", type: " + deviceConfiguration.getType());
                }
                m192a(m187a(i), deviceConfiguration);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Serializable serializable = null;
        if (resultCode == -1) {
            if (requestCode == EDIT_MOTOR_CONTROLLER_REQUEST_CODE) {
                serializable = data.getSerializableExtra(EditMotorControllerActivity.EDIT_MOTOR_CONTROLLER_CONFIG);
            } else if (requestCode == EDIT_SERVO_CONTROLLER_REQUEST_CODE) {
                serializable = data.getSerializableExtra(EditServoControllerActivity.EDIT_SERVO_ACTIVITY);
            } else if (requestCode == EDIT_MATRIX_CONTROLLER_REQUEST_CODE) {
                serializable = data.getSerializableExtra(EditMatrixControllerActivity.EDIT_MATRIX_ACTIVITY);
            }
            if (serializable != null) {
                DeviceConfiguration deviceConfiguration = (ControllerConfiguration) serializable;
                m200b(deviceConfiguration);
                m192a(m187a(deviceConfiguration.getPort()), (DeviceConfiguration) this.f294g.get(deviceConfiguration.getPort()));
                if (!this.f290c.toLowerCase().contains(Utility.UNSAVED.toLowerCase())) {
                    String str = "Unsaved " + this.f290c;
                    this.f289b.saveToPreferences(str, C0035R.string.pref_hardware_config_filename);
                    this.f290c = str;
                }
                this.f289b.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
            }
        }
    }

    public void saveLegacyController(View v) {
        m189a();
    }

    private void m189a() {
        Intent intent = new Intent();
        this.f292e.setName(this.f293f.getText().toString());
        intent.putExtra(EDIT_LEGACY_CONFIG, this.f292e);
        setResult(-1, intent);
        finish();
    }

    public void cancelLegacyController(View v) {
        setResult(0, new Intent());
        finish();
    }

    private void m192a(View view, DeviceConfiguration deviceConfiguration) {
        Spinner spinner = (Spinner) view.findViewById(C0035R.id.choiceSpinner_legacyModule);
        spinner.setSelection(((ArrayAdapter) spinner.getAdapter()).getPosition(deviceConfiguration.getType().toString()));
        spinner.setOnItemSelectedListener(this.f301n);
        Object name = deviceConfiguration.getName();
        EditText editText = (EditText) view.findViewById(C0035R.id.editTextResult_name);
        int parseInt = Integer.parseInt(((TextView) view.findViewById(C0035R.id.portNumber)).getText().toString());
        editText.addTextChangedListener(new C0063a(m187a(parseInt), null));
        editText.setText(name);
        if (f288a) {
            RobotLog.m328e("[populatePort] name: " + name + ", port: " + parseInt + ", type: " + deviceConfiguration.getType());
        }
    }

    private void m194a(LinearLayout linearLayout) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.portNumber)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_name);
        editText.setEnabled(false);
        editText.setText(DeviceConfiguration.DISABLED_DEVICE_NAME);
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration(ConfigurationType.NOTHING);
        deviceConfiguration.setPort(parseInt);
        m200b(deviceConfiguration);
        m190a(parseInt, 8);
    }

    private void m195a(LinearLayout linearLayout, String str) {
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.portNumber)).getText().toString());
        EditText editText = (EditText) linearLayout.findViewById(C0035R.id.editTextResult_name);
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f294g.get(parseInt);
        editText.setEnabled(true);
        m193a(editText, deviceConfiguration);
        ConfigurationType typeFromString = deviceConfiguration.typeFromString(str);
        if (typeFromString == ConfigurationType.MOTOR_CONTROLLER || typeFromString == ConfigurationType.SERVO_CONTROLLER || typeFromString == ConfigurationType.MATRIX_CONTROLLER) {
            m191a(parseInt, str);
            m190a(parseInt, 0);
        } else {
            deviceConfiguration.setType(typeFromString);
            if (typeFromString == ConfigurationType.NOTHING) {
                deviceConfiguration.setEnabled(false);
            } else {
                deviceConfiguration.setEnabled(true);
            }
            m190a(parseInt, 8);
        }
        if (f288a) {
            DeviceConfiguration deviceConfiguration2 = (DeviceConfiguration) this.f294g.get(parseInt);
            RobotLog.m328e("[changeDevice] modules.get(port) name: " + deviceConfiguration2.getName() + ", port: " + deviceConfiguration2.getPort() + ", type: " + deviceConfiguration2.getType());
        }
    }

    private void m191a(int i, String str) {
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f294g.get(i);
        String name = deviceConfiguration.getName();
        List arrayList = new ArrayList();
        SerialNumber serialNumber = ControllerConfiguration.NO_SERIAL_NUMBER;
        if (!deviceConfiguration.getType().toString().equalsIgnoreCase(str)) {
            deviceConfiguration = new ControllerConfiguration("dummy module", arrayList, serialNumber, ConfigurationType.NOTHING);
            int i2;
            if (str.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString())) {
                for (i2 = 1; i2 <= 2; i2++) {
                    arrayList.add(new MotorConfiguration(i2));
                }
                deviceConfiguration = new MotorControllerConfiguration(name, arrayList, serialNumber);
                deviceConfiguration.setPort(i);
            } else if (str.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString())) {
                for (i2 = 1; i2 <= 6; i2++) {
                    arrayList.add(new ServoConfiguration(i2));
                }
                deviceConfiguration = new ServoControllerConfiguration(name, arrayList, serialNumber);
                deviceConfiguration.setPort(i);
            } else if (str.equalsIgnoreCase(ConfigurationType.MATRIX_CONTROLLER.toString())) {
                arrayList = new ArrayList();
                for (i2 = 1; i2 <= 4; i2++) {
                    arrayList.add(new MotorConfiguration(i2));
                }
                List arrayList2 = new ArrayList();
                for (i2 = 1; i2 <= 4; i2++) {
                    arrayList2.add(new ServoConfiguration(i2));
                }
                deviceConfiguration = new MatrixControllerConfiguration(name, arrayList, arrayList2, serialNumber);
                deviceConfiguration.setPort(i);
            }
            deviceConfiguration.setEnabled(true);
            m200b(deviceConfiguration);
        }
    }

    public void editController_portALL(View v) {
        LinearLayout linearLayout = (LinearLayout) v.getParent().getParent();
        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(C0035R.id.portNumber)).getText().toString());
        DeviceConfiguration deviceConfiguration = (DeviceConfiguration) this.f294g.get(parseInt);
        if (!m201c(deviceConfiguration)) {
            m191a(parseInt, ((Spinner) linearLayout.findViewById(C0035R.id.choiceSpinner_legacyModule)).getSelectedItem().toString());
        }
        m198a(deviceConfiguration);
    }

    private void m198a(DeviceConfiguration deviceConfiguration) {
        deviceConfiguration.setName(((EditText) ((LinearLayout) m187a(deviceConfiguration.getPort())).findViewById(C0035R.id.editTextResult_name)).getText().toString());
        Intent intent;
        if (deviceConfiguration.getType() == ConfigurationType.MOTOR_CONTROLLER) {
            intent = new Intent(this.f291d, EditMotorControllerActivity.class);
            intent.putExtra(EditMotorControllerActivity.EDIT_MOTOR_CONTROLLER_CONFIG, deviceConfiguration);
            intent.putExtra("requestCode", EDIT_MOTOR_CONTROLLER_REQUEST_CODE);
            setResult(-1, intent);
            startActivityForResult(intent, EDIT_MOTOR_CONTROLLER_REQUEST_CODE);
        } else if (deviceConfiguration.getType() == ConfigurationType.SERVO_CONTROLLER) {
            intent = new Intent(this.f291d, EditServoControllerActivity.class);
            intent.putExtra(EditServoControllerActivity.EDIT_SERVO_ACTIVITY, deviceConfiguration);
            setResult(-1, intent);
            startActivityForResult(intent, EDIT_SERVO_CONTROLLER_REQUEST_CODE);
        } else if (deviceConfiguration.getType() == ConfigurationType.MATRIX_CONTROLLER) {
            intent = new Intent(this.f291d, EditMatrixControllerActivity.class);
            intent.putExtra(EditMatrixControllerActivity.EDIT_MATRIX_ACTIVITY, deviceConfiguration);
            setResult(-1, intent);
            startActivityForResult(intent, EDIT_MATRIX_CONTROLLER_REQUEST_CODE);
        }
    }

    private void m200b(DeviceConfiguration deviceConfiguration) {
        this.f294g.set(deviceConfiguration.getPort(), deviceConfiguration);
    }

    private View m187a(int i) {
        switch (i) {
            case SpiSlaveResponseEvent.OK /*0*/:
                return this.f295h;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return this.f296i;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return this.f297j;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return this.f298k;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                return this.f299l;
            case BuildConfig.VERSION_CODE /*5*/:
                return this.f300m;
            default:
                return null;
        }
    }

    private void m190a(int i, int i2) {
        ((Button) m187a(i).findViewById(C0035R.id.edit_controller_btn)).setVisibility(i2);
    }

    private boolean m201c(DeviceConfiguration deviceConfiguration) {
        return deviceConfiguration.getType() == ConfigurationType.MOTOR_CONTROLLER || deviceConfiguration.getType() == ConfigurationType.SERVO_CONTROLLER;
    }

    private void m193a(EditText editText, DeviceConfiguration deviceConfiguration) {
        if (editText.getText().toString().equalsIgnoreCase(DeviceConfiguration.DISABLED_DEVICE_NAME)) {
            editText.setText(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            deviceConfiguration.setName(com.qualcomm.robotcore.BuildConfig.VERSION_NAME);
            return;
        }
        editText.setText(deviceConfiguration.getName());
    }
}
