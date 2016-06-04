package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.DeviceInfoAdapter;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FtcConfigurationActivity extends Activity {
    OnClickListener f373a;
    OnClickListener f374b;
    OnClickListener f375c;
    private Thread f376d;
    private Map<SerialNumber, ControllerConfiguration> f377e;
    private Context f378f;
    private DeviceManager f379g;
    private Button f380h;
    private String f381i;
    private Utility f382j;
    protected SharedPreferences preferences;
    protected Map<SerialNumber, DeviceType> scannedDevices;
    protected Set<Entry<SerialNumber, DeviceType>> scannedEntries;

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.11 */
    class AnonymousClass11 implements OnClickListener {
        final /* synthetic */ EditText f359a;
        final /* synthetic */ FtcConfigurationActivity f360b;

        AnonymousClass11(FtcConfigurationActivity ftcConfigurationActivity, EditText editText) {
            this.f360b = ftcConfigurationActivity;
            this.f359a = editText;
        }

        public void onClick(DialogInterface dialog, int button) {
            String trim = (this.f359a.getText().toString() + Utility.FILE_EXT).trim();
            if (trim.equals(Utility.FILE_EXT)) {
                this.f360b.f382j.complainToast("File not saved: Please entire a filename.", this.f360b.f378f);
                return;
            }
            try {
                this.f360b.f382j.writeToFile(trim);
                this.f360b.m248g();
                this.f360b.f382j.saveToPreferences(this.f359a.getText().toString(), C0035R.string.pref_hardware_config_filename);
                this.f360b.f381i = this.f359a.getText().toString();
                this.f360b.f382j.updateHeader(Utility.DEFAULT_ROBOT_CONFIG, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
                this.f360b.f382j.confirmSave();
            } catch (RobotCoreException e) {
                this.f360b.f382j.complainToast(e.getMessage(), this.f360b.f378f);
                DbgLog.error(e.getMessage());
            } catch (IOException e2) {
                this.f360b.m235a(e2.getMessage());
                DbgLog.error(e2.getMessage());
            }
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.1 */
    class C00721 implements OnClickListener {
        final /* synthetic */ FtcConfigurationActivity f361a;

        C00721(FtcConfigurationActivity ftcConfigurationActivity) {
            this.f361a = ftcConfigurationActivity;
        }

        public void onClick(DialogInterface dialog, int button) {
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.2 */
    class C00732 implements OnClickListener {
        final /* synthetic */ FtcConfigurationActivity f362a;

        C00732(FtcConfigurationActivity ftcConfigurationActivity) {
            this.f362a = ftcConfigurationActivity;
        }

        public void onClick(DialogInterface dialog, int button) {
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.3 */
    static /* synthetic */ class C00743 {
        static final /* synthetic */ int[] f363a;

        static {
            f363a = new int[DeviceType.values().length];
            try {
                f363a[DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f363a[DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f363a[DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f363a[DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.4 */
    class C00754 implements View.OnClickListener {
        final /* synthetic */ FtcConfigurationActivity f364a;

        C00754(FtcConfigurationActivity ftcConfigurationActivity) {
            this.f364a = ftcConfigurationActivity;
        }

        public void onClick(View view) {
            Builder buildBuilder = this.f364a.f382j.buildBuilder("Devices", "These are the devices discovered by the Hardware Wizard. You can click on the name of each device to edit the information relating to that device. Make sure each device has a unique name. The names should match what is in the Op mode code. Scroll down to see more devices if there are any.");
            buildBuilder.setPositiveButton("Ok", this.f364a.f373a);
            AlertDialog create = buildBuilder.create();
            create.show();
            ((TextView) create.findViewById(16908299)).setTextSize(14.0f);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.5 */
    class C00765 implements View.OnClickListener {
        final /* synthetic */ FtcConfigurationActivity f365a;

        C00765(FtcConfigurationActivity ftcConfigurationActivity) {
            this.f365a = ftcConfigurationActivity;
        }

        public void onClick(View view) {
            Builder buildBuilder = this.f365a.f382j.buildBuilder("Save Configuration", "Clicking the save button will create an xml file in: \n      /sdcard/FIRST/  \nThis file will be used to initialize the robot.");
            buildBuilder.setPositiveButton("Ok", this.f365a.f373a);
            AlertDialog create = buildBuilder.create();
            create.show();
            ((TextView) create.findViewById(16908299)).setTextSize(14.0f);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.6 */
    class C00796 implements View.OnClickListener {
        final /* synthetic */ FtcConfigurationActivity f368a;

        /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.6.1 */
        class C00781 implements Runnable {
            final /* synthetic */ C00796 f367a;

            /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.6.1.1 */
            class C00771 implements Runnable {
                final /* synthetic */ C00781 f366a;

                C00771(C00781 c00781) {
                    this.f366a = c00781;
                }

                public void run() {
                    this.f366a.f367a.f368a.f382j.resetCount();
                    this.f366a.f367a.f368a.m248g();
                    this.f366a.f367a.f368a.m252i();
                    this.f366a.f367a.f368a.f382j.updateHeader(this.f366a.f367a.f368a.f381i, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
                    this.f366a.f367a.f368a.scannedEntries = this.f366a.f367a.f368a.scannedDevices.entrySet();
                    this.f366a.f367a.f368a.f377e = this.f366a.f367a.f368a.m239b();
                    this.f366a.f367a.f368a.m250h();
                    this.f366a.f367a.f368a.m247f();
                }
            }

            C00781(C00796 c00796) {
                this.f367a = c00796;
            }

            public void run() {
                try {
                    DbgLog.msg("Scanning USB bus");
                    this.f367a.f368a.scannedDevices = this.f367a.f368a.f379g.scanForUsbDevices();
                } catch (RobotCoreException e) {
                    DbgLog.error("Device scan failed: " + e.toString());
                }
                this.f367a.f368a.runOnUiThread(new C00771(this));
            }
        }

        C00796(FtcConfigurationActivity ftcConfigurationActivity) {
            this.f368a = ftcConfigurationActivity;
        }

        public void onClick(View view) {
            this.f368a.f376d = new Thread(new C00781(this));
            this.f368a.m240c();
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.7 */
    class C00807 implements OnClickListener {
        final /* synthetic */ FtcConfigurationActivity f369a;

        C00807(FtcConfigurationActivity ftcConfigurationActivity) {
            this.f369a = ftcConfigurationActivity;
        }

        public void onClick(DialogInterface dialog, int button) {
            this.f369a.f376d.start();
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.8 */
    class C00818 implements OnItemClickListener {
        final /* synthetic */ FtcConfigurationActivity f370a;

        C00818(FtcConfigurationActivity ftcConfigurationActivity) {
            this.f370a = ftcConfigurationActivity;
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int pos, long arg3) {
            ControllerConfiguration controllerConfiguration = (ControllerConfiguration) adapterView.getItemAtPosition(pos);
            ConfigurationType type = controllerConfiguration.getType();
            if (type.equals(ConfigurationType.MOTOR_CONTROLLER)) {
                Intent intent = new Intent(this.f370a.f378f, EditMotorControllerActivity.class);
                intent.putExtra(EditMotorControllerActivity.EDIT_MOTOR_CONTROLLER_CONFIG, controllerConfiguration);
                intent.putExtra("requestCode", 1);
                this.f370a.setResult(-1, intent);
                this.f370a.startActivityForResult(intent, 1);
            }
            if (type.equals(ConfigurationType.SERVO_CONTROLLER)) {
                intent = new Intent(this.f370a.f378f, EditServoControllerActivity.class);
                intent.putExtra(EditServoControllerActivity.EDIT_SERVO_ACTIVITY, controllerConfiguration);
                intent.putExtra("requestCode", 2);
                this.f370a.setResult(-1, intent);
                this.f370a.startActivityForResult(intent, 2);
            }
            if (type.equals(ConfigurationType.LEGACY_MODULE_CONTROLLER)) {
                intent = new Intent(this.f370a.f378f, EditLegacyModuleControllerActivity.class);
                intent.putExtra(EditLegacyModuleControllerActivity.EDIT_LEGACY_CONFIG, controllerConfiguration);
                intent.putExtra("requestCode", 3);
                this.f370a.setResult(-1, intent);
                this.f370a.startActivityForResult(intent, 3);
            }
            if (type.equals(ConfigurationType.DEVICE_INTERFACE_MODULE)) {
                Intent intent2 = new Intent(this.f370a.f378f, EditDeviceInterfaceModuleActivity.class);
                intent2.putExtra(EditDeviceInterfaceModuleActivity.EDIT_DEVICE_INTERFACE_MODULE_CONFIG, controllerConfiguration);
                intent2.putExtra("requestCode", 4);
                this.f370a.setResult(-1, intent2);
                this.f370a.startActivityForResult(intent2, 4);
            }
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcConfigurationActivity.9 */
    class C00829 implements OnClickListener {
        final /* synthetic */ EditText f371a;
        final /* synthetic */ FtcConfigurationActivity f372b;

        C00829(FtcConfigurationActivity ftcConfigurationActivity, EditText editText) {
            this.f372b = ftcConfigurationActivity;
            this.f371a = editText;
        }

        public void onClick(DialogInterface dialog, int button) {
            String trim = (this.f371a.getText().toString() + Utility.FILE_EXT).trim();
            if (trim.equals(Utility.FILE_EXT)) {
                this.f372b.f382j.complainToast("File not saved: Please entire a filename.", this.f372b.f378f);
                return;
            }
            try {
                this.f372b.f382j.writeToFile(trim);
                this.f372b.m248g();
                this.f372b.f382j.saveToPreferences(this.f371a.getText().toString(), C0035R.string.pref_hardware_config_filename);
                this.f372b.f381i = this.f371a.getText().toString();
                this.f372b.f382j.updateHeader(Utility.DEFAULT_ROBOT_CONFIG, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
                this.f372b.f382j.confirmSave();
                this.f372b.finish();
            } catch (RobotCoreException e) {
                this.f372b.f382j.complainToast(e.getMessage(), this.f372b.f378f);
                DbgLog.error(e.getMessage());
            } catch (IOException e2) {
                this.f372b.m235a(e2.getMessage());
                DbgLog.error(e2.getMessage());
            }
        }
    }

    public FtcConfigurationActivity() {
        this.f377e = new HashMap();
        this.f381i = Utility.NO_FILE;
        this.scannedDevices = new HashMap();
        this.scannedEntries = new HashSet();
        this.f373a = new C00721(this);
        this.f374b = new OnClickListener() {
            final /* synthetic */ FtcConfigurationActivity f358a;

            {
                this.f358a = r1;
            }

            public void onClick(DialogInterface dialog, int button) {
                this.f358a.f382j.saveToPreferences(this.f358a.f381i.substring(7).trim(), C0035R.string.pref_hardware_config_filename);
                this.f358a.finish();
            }
        };
        this.f375c = new C00732(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.activity_ftc_configuration);
        RobotLog.writeLogcatToDisk(this, 1024);
        this.f378f = this;
        this.f382j = new Utility(this);
        this.f380h = (Button) findViewById(C0035R.id.scanButton);
        m233a();
        try {
            this.f379g = new HardwareDeviceManager(this.f378f, null);
        } catch (Exception e) {
            this.f382j.complainToast("Failed to open the Device Manager", this.f378f);
            DbgLog.error("Failed to open deviceManager: " + e.toString());
            DbgLog.logStacktrace(e);
        }
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void m233a() {
        ((Button) findViewById(C0035R.id.devices_holder).findViewById(C0035R.id.info_btn)).setOnClickListener(new C00754(this));
        ((Button) findViewById(C0035R.id.save_holder).findViewById(C0035R.id.info_btn)).setOnClickListener(new C00765(this));
    }

    protected void onStart() {
        super.onStart();
        this.f381i = this.f382j.getFilenameFromPrefs(C0035R.string.pref_hardware_config_filename, Utility.NO_FILE);
        if (this.f381i.equalsIgnoreCase(Utility.NO_FILE)) {
            this.f382j.createConfigFolder();
        }
        this.f382j.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        m245e();
        if (!this.f381i.toLowerCase().contains(Utility.UNSAVED.toLowerCase())) {
            m242d();
        }
        this.f380h.setOnClickListener(new C00796(this));
    }

    private HashMap<SerialNumber, ControllerConfiguration> m239b() {
        HashMap<SerialNumber, ControllerConfiguration> hashMap = new HashMap();
        for (Entry entry : this.scannedEntries) {
            SerialNumber serialNumber = (SerialNumber) entry.getKey();
            if (!this.f377e.containsKey(serialNumber)) {
                switch (C00743.f363a[((DeviceType) entry.getValue()).ordinal()]) {
                    case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                        hashMap.put(serialNumber, this.f382j.buildMotorController(serialNumber));
                        break;
                    case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                        hashMap.put(serialNumber, this.f382j.buildServoController(serialNumber));
                        break;
                    case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                        hashMap.put(serialNumber, this.f382j.buildLegacyModule(serialNumber));
                        break;
                    case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                        hashMap.put(serialNumber, this.f382j.buildDeviceInterfaceModule(serialNumber));
                        break;
                    default:
                        break;
                }
            }
            hashMap.put(serialNumber, this.f377e.get(serialNumber));
        }
        return hashMap;
    }

    private void m240c() {
        if (this.f381i.toLowerCase().contains(Utility.UNSAVED.toLowerCase())) {
            View editText = new EditText(this.f378f);
            editText.setEnabled(false);
            editText.setText(BuildConfig.VERSION_NAME);
            Builder buildBuilder = this.f382j.buildBuilder("Unsaved changes", "If you scan, your current unsaved changes will be lost.");
            buildBuilder.setView(editText);
            buildBuilder.setPositiveButton("Ok", new C00807(this));
            buildBuilder.setNegativeButton("Cancel", this.f375c);
            buildBuilder.show();
            return;
        }
        this.f376d.start();
    }

    private void m242d() {
        ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler(this.f378f);
        if (!this.f381i.equalsIgnoreCase(Utility.NO_FILE)) {
            try {
                m236a((ArrayList) readXMLFileHandler.parse(new FileInputStream(Utility.CONFIG_FILES_DIR + this.f381i + Utility.FILE_EXT)));
                m250h();
                m247f();
            } catch (RobotCoreException e) {
                RobotLog.m328e("Error parsing XML file");
                RobotLog.logStacktrace(e);
                this.f382j.complainToast("Error parsing XML file: " + this.f381i, this.f378f);
            } catch (Exception e2) {
                DbgLog.error("File was not found: " + this.f381i);
                DbgLog.logStacktrace(e2);
                this.f382j.complainToast("That file was not found: " + this.f381i, this.f378f);
            }
        }
    }

    private void m245e() {
        if (this.f377e.size() == 0) {
            this.f382j.setOrangeText("Scan to find devices.", "In order to find devices: \n    1. Attach a robot \n    2. Press the 'Scan' button", C0035R.id.empty_devicelist, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
            m248g();
            return;
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(C0035R.id.empty_devicelist);
        linearLayout.removeAllViews();
        linearLayout.setVisibility(8);
    }

    private void m247f() {
        if (this.f377e.size() == 0) {
            this.f382j.setOrangeText("No devices found!", "In order to find devices: \n    1. Attach a robot \n    2. Press the 'Scan' button", C0035R.id.empty_devicelist, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
            m248g();
            return;
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(C0035R.id.empty_devicelist);
        linearLayout.removeAllViews();
        linearLayout.setVisibility(8);
    }

    private void m235a(String str) {
        this.f382j.setOrangeText("Found " + str, "Please fix and re-save.", C0035R.id.warning_layout, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
    }

    private void m248g() {
        LinearLayout linearLayout = (LinearLayout) findViewById(C0035R.id.warning_layout);
        linearLayout.removeAllViews();
        linearLayout.setVisibility(8);
    }

    private void m250h() {
        ListView listView = (ListView) findViewById(C0035R.id.controllersList);
        listView.setAdapter(new DeviceInfoAdapter(this, 17367044, this.f377e));
        listView.setOnItemClickListener(new C00818(this));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            Serializable serializable = null;
            if (requestCode == 1) {
                serializable = data.getSerializableExtra(EditMotorControllerActivity.EDIT_MOTOR_CONTROLLER_CONFIG);
            } else if (requestCode == 2) {
                serializable = data.getSerializableExtra(EditServoControllerActivity.EDIT_SERVO_ACTIVITY);
            } else if (requestCode == 3) {
                serializable = data.getSerializableExtra(EditLegacyModuleControllerActivity.EDIT_LEGACY_CONFIG);
            } else if (requestCode == 4) {
                serializable = data.getSerializableExtra(EditDeviceInterfaceModuleActivity.EDIT_DEVICE_INTERFACE_MODULE_CONFIG);
            }
            if (serializable != null) {
                ControllerConfiguration controllerConfiguration = (ControllerConfiguration) serializable;
                this.scannedDevices.put(controllerConfiguration.getSerialNumber(), controllerConfiguration.configTypeToDeviceType(controllerConfiguration.getType()));
                this.f377e.put(controllerConfiguration.getSerialNumber(), controllerConfiguration);
                m250h();
                m252i();
                return;
            }
            DbgLog.error("Received Result with an incorrect request code: " + String.valueOf(requestCode));
        }
    }

    private void m252i() {
        if (!this.f381i.toLowerCase().contains(Utility.UNSAVED.toLowerCase())) {
            String str = "Unsaved " + this.f381i;
            this.f382j.saveToPreferences(str, C0035R.string.pref_hardware_config_filename);
            this.f381i = str;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.f382j.resetCount();
    }

    public void onBackPressed() {
        if (!this.f381i.toLowerCase().contains(Utility.UNSAVED.toLowerCase())) {
            super.onBackPressed();
        } else if (!this.f382j.writeXML(this.f377e)) {
            View editText = new EditText(this);
            editText.setText(this.f382j.prepareFilename(this.f381i));
            Builder buildBuilder = this.f382j.buildBuilder("Unsaved changes", "Please save your file before exiting the Hardware Wizard! \n If you click 'Cancel' your changes will be lost.");
            buildBuilder.setView(editText);
            buildBuilder.setPositiveButton("Ok", new C00829(this, editText));
            buildBuilder.setNegativeButton("Cancel", this.f374b);
            buildBuilder.show();
        }
    }

    public void saveConfiguration(View v) {
        if (!this.f382j.writeXML(this.f377e)) {
            View editText = new EditText(this);
            editText.setText(this.f382j.prepareFilename(this.f381i));
            Builder buildBuilder = this.f382j.buildBuilder("Enter file name", "Please enter the file name");
            buildBuilder.setView(editText);
            buildBuilder.setPositiveButton("Ok", new AnonymousClass11(this, editText));
            buildBuilder.setNegativeButton("Cancel", this.f375c);
            buildBuilder.show();
        }
    }

    private void m236a(ArrayList<ControllerConfiguration> arrayList) {
        this.f377e = new HashMap();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ControllerConfiguration controllerConfiguration = (ControllerConfiguration) it.next();
            this.f377e.put(controllerConfiguration.getSerialNumber(), controllerConfiguration);
        }
    }
}
