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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditDeviceInterfaceModuleActivity extends Activity {
    public static final int EDIT_ANALOG_INPUT_REQUEST_CODE = 203;
    public static final int EDIT_ANALOG_OUTPUT_REQUEST_CODE = 205;
    public static final String EDIT_DEVICE_INTERFACE_MODULE_CONFIG = "EDIT_DEVICE_INTERFACE_MODULE_CONFIG";
    public static final int EDIT_DIGITAL_REQUEST_CODE = 204;
    public static final int EDIT_I2C_PORT_REQUEST_CODE = 202;
    public static final int EDIT_PWM_PORT_REQUEST_CODE = 201;
    private Utility f251a;
    private String f252b;
    private Context f253c;
    private DeviceInterfaceModuleConfiguration f254d;
    private EditText f255e;
    private ArrayList<DeviceConfiguration> f256f;
    private OnItemClickListener f257g;

    /* renamed from: com.qualcomm.ftccommon.configuration.EditDeviceInterfaceModuleActivity.1 */
    class C00561 implements OnItemClickListener {
        final /* synthetic */ EditDeviceInterfaceModuleActivity f249a;

        C00561(EditDeviceInterfaceModuleActivity editDeviceInterfaceModuleActivity) {
            this.f249a = editDeviceInterfaceModuleActivity;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case SpiSlaveResponseEvent.OK /*0*/:
                    this.f249a.m162a(EditDeviceInterfaceModuleActivity.EDIT_PWM_PORT_REQUEST_CODE, this.f249a.f254d.getPwmDevices(), EditPWMDevicesActivity.class);
                case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                    this.f249a.m162a(EditDeviceInterfaceModuleActivity.EDIT_I2C_PORT_REQUEST_CODE, this.f249a.f254d.getI2cDevices(), EditI2cDevicesActivity.class);
                case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                    this.f249a.m162a(EditDeviceInterfaceModuleActivity.EDIT_ANALOG_INPUT_REQUEST_CODE, this.f249a.f254d.getAnalogInputDevices(), EditAnalogInputDevicesActivity.class);
                case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                    this.f249a.m162a(EditDeviceInterfaceModuleActivity.EDIT_DIGITAL_REQUEST_CODE, this.f249a.f254d.getDigitalDevices(), EditDigitalDevicesActivity.class);
                case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                    this.f249a.m162a(EditDeviceInterfaceModuleActivity.EDIT_ANALOG_OUTPUT_REQUEST_CODE, this.f249a.f254d.getAnalogOutputDevices(), EditAnalogOutputDevicesActivity.class);
                default:
            }
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.EditDeviceInterfaceModuleActivity.a */
    private class C0057a implements TextWatcher {
        final /* synthetic */ EditDeviceInterfaceModuleActivity f250a;

        private C0057a(EditDeviceInterfaceModuleActivity editDeviceInterfaceModuleActivity) {
            this.f250a = editDeviceInterfaceModuleActivity;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            this.f250a.f254d.setName(editable.toString());
        }
    }

    public EditDeviceInterfaceModuleActivity() {
        this.f256f = new ArrayList();
        this.f257g = new C00561(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.device_interface_module);
        ListView listView = (ListView) findViewById(C0035R.id.listView_devices);
        listView.setAdapter(new ArrayAdapter(this, 17367043, getResources().getStringArray(C0035R.array.device_interface_module_options_array)));
        listView.setOnItemClickListener(this.f257g);
        this.f253c = this;
        PreferenceManager.setDefaultValues(this, C0035R.xml.preferences, false);
        this.f251a = new Utility(this);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f255e = (EditText) findViewById(C0035R.id.device_interface_module_name);
        this.f255e.addTextChangedListener(new C0057a());
    }

    protected void onStart() {
        super.onStart();
        this.f251a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        this.f252b = this.f251a.getFilenameFromPrefs(C0035R.string.pref_hardware_config_filename, Utility.NO_FILE);
        Serializable serializableExtra = getIntent().getSerializableExtra(EDIT_DEVICE_INTERFACE_MODULE_CONFIG);
        if (serializableExtra != null) {
            this.f254d = (DeviceInterfaceModuleConfiguration) serializableExtra;
            this.f256f = (ArrayList) this.f254d.getDevices();
            this.f255e.setText(this.f254d.getName());
            ((TextView) findViewById(C0035R.id.device_interface_module_serialNumber)).setText(this.f254d.getSerialNumber().toString());
        }
    }

    private void m162a(int i, List<DeviceConfiguration> list, Class cls) {
        Bundle bundle = new Bundle();
        for (int i2 = 0; i2 < list.size(); i2++) {
            bundle.putSerializable(String.valueOf(i2), (Serializable) list.get(i2));
        }
        Intent intent = new Intent(this.f253c, cls);
        intent.putExtras(bundle);
        setResult(-1, intent);
        startActivityForResult(intent, i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            Bundle extras;
            if (requestCode == EDIT_PWM_PORT_REQUEST_CODE) {
                extras = data.getExtras();
                if (extras != null) {
                    this.f254d.setPwmDevices(m160a(extras));
                }
            } else if (requestCode == EDIT_ANALOG_INPUT_REQUEST_CODE) {
                extras = data.getExtras();
                if (extras != null) {
                    this.f254d.setAnalogInputDevices(m160a(extras));
                }
            } else if (requestCode == EDIT_DIGITAL_REQUEST_CODE) {
                extras = data.getExtras();
                if (extras != null) {
                    this.f254d.setDigitalDevices(m160a(extras));
                }
            } else if (requestCode == EDIT_I2C_PORT_REQUEST_CODE) {
                extras = data.getExtras();
                if (extras != null) {
                    this.f254d.setI2cDevices(m160a(extras));
                }
            } else if (requestCode == EDIT_ANALOG_OUTPUT_REQUEST_CODE) {
                extras = data.getExtras();
                if (extras != null) {
                    this.f254d.setAnalogOutputDevices(m160a(extras));
                }
            }
            m161a();
        }
    }

    private ArrayList<DeviceConfiguration> m160a(Bundle bundle) {
        ArrayList<DeviceConfiguration> arrayList = new ArrayList();
        for (String str : bundle.keySet()) {
            arrayList.add(Integer.parseInt(str), (DeviceConfiguration) bundle.getSerializable(str));
        }
        return arrayList;
    }

    private void m161a() {
        if (!this.f252b.toLowerCase().contains(Utility.UNSAVED.toLowerCase())) {
            String str = "Unsaved " + this.f252b;
            this.f251a.saveToPreferences(str, C0035R.string.pref_hardware_config_filename);
            this.f252b = str;
        }
        this.f251a.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
    }

    public void saveDeviceInterface(View v) {
        m164b();
    }

    private void m164b() {
        Intent intent = new Intent();
        this.f254d.setName(this.f255e.getText().toString());
        intent.putExtra(EDIT_DEVICE_INTERFACE_MODULE_CONFIG, this.f254d);
        setResult(-1, intent);
        finish();
    }

    public void cancelDeviceInterface(View v) {
        setResult(0, new Intent());
        finish();
    }
}
