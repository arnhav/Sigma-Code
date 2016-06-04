package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AutoConfigureActivity extends Activity {
    private Context f220a;
    private Button f221b;
    private Button f222c;
    private DeviceManager f223d;
    private Map<SerialNumber, ControllerConfiguration> f224e;
    protected Set<Entry<SerialNumber, DeviceType>> entries;
    private Thread f225f;
    private Utility f226g;
    protected Map<SerialNumber, DeviceType> scannedDevices;

    /* renamed from: com.qualcomm.ftccommon.configuration.AutoConfigureActivity.1 */
    class C00481 implements OnClickListener {
        final /* synthetic */ AutoConfigureActivity f216a;

        /* renamed from: com.qualcomm.ftccommon.configuration.AutoConfigureActivity.1.1 */
        class C00471 implements Runnable {
            final /* synthetic */ C00481 f215a;

            /* renamed from: com.qualcomm.ftccommon.configuration.AutoConfigureActivity.1.1.1 */
            class C00461 implements Runnable {
                final /* synthetic */ C00471 f214a;

                C00461(C00471 c00471) {
                    this.f214a = c00471;
                }

                public void run() {
                    this.f214a.f215a.f216a.f226g.resetCount();
                    if (this.f214a.f215a.f216a.scannedDevices.size() == 0) {
                        this.f214a.f215a.f216a.f226g.saveToPreferences(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename);
                        this.f214a.f215a.f216a.f226g.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
                        this.f214a.f215a.f216a.m118a();
                    }
                    this.f214a.f215a.f216a.entries = this.f214a.f215a.f216a.scannedDevices.entrySet();
                    this.f214a.f215a.f216a.f224e = new HashMap();
                    this.f214a.f215a.f216a.f226g.createLists(this.f214a.f215a.f216a.entries, this.f214a.f215a.f216a.f224e);
                    if (this.f214a.f215a.f216a.m134g()) {
                        this.f214a.f215a.f216a.m121a(Utility.AUTOCONFIGURE_K9LEGACYBOT);
                        return;
                    }
                    this.f214a.f215a.f216a.f226g.saveToPreferences(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename);
                    this.f214a.f215a.f216a.f226g.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
                    this.f214a.f215a.f216a.m123b();
                }
            }

            C00471(C00481 c00481) {
                this.f215a = c00481;
            }

            public void run() {
                try {
                    DbgLog.msg("Scanning USB bus");
                    this.f215a.f216a.scannedDevices = this.f215a.f216a.f223d.scanForUsbDevices();
                } catch (RobotCoreException e) {
                    DbgLog.error("Device scan failed");
                }
                this.f215a.f216a.runOnUiThread(new C00461(this));
            }
        }

        C00481(AutoConfigureActivity autoConfigureActivity) {
            this.f216a = autoConfigureActivity;
        }

        public void onClick(View view) {
            this.f216a.f225f = new Thread(new C00471(this));
            this.f216a.f225f.start();
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.AutoConfigureActivity.2 */
    class C00512 implements OnClickListener {
        final /* synthetic */ AutoConfigureActivity f219a;

        /* renamed from: com.qualcomm.ftccommon.configuration.AutoConfigureActivity.2.1 */
        class C00501 implements Runnable {
            final /* synthetic */ C00512 f218a;

            /* renamed from: com.qualcomm.ftccommon.configuration.AutoConfigureActivity.2.1.1 */
            class C00491 implements Runnable {
                final /* synthetic */ C00501 f217a;

                C00491(C00501 c00501) {
                    this.f217a = c00501;
                }

                public void run() {
                    this.f217a.f218a.f219a.f226g.resetCount();
                    if (this.f217a.f218a.f219a.scannedDevices.size() == 0) {
                        this.f217a.f218a.f219a.f226g.saveToPreferences(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename);
                        this.f217a.f218a.f219a.f226g.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
                        this.f217a.f218a.f219a.m118a();
                    }
                    this.f217a.f218a.f219a.entries = this.f217a.f218a.f219a.scannedDevices.entrySet();
                    this.f217a.f218a.f219a.f224e = new HashMap();
                    this.f217a.f218a.f219a.f226g.createLists(this.f217a.f218a.f219a.entries, this.f217a.f218a.f219a.f224e);
                    if (this.f217a.f218a.f219a.m129e()) {
                        this.f217a.f218a.f219a.m121a(Utility.AUTOCONFIGURE_K9USBBOT);
                        return;
                    }
                    this.f217a.f218a.f219a.f226g.saveToPreferences(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename);
                    this.f217a.f218a.f219a.f226g.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
                    this.f217a.f218a.f219a.m125c();
                }
            }

            C00501(C00512 c00512) {
                this.f218a = c00512;
            }

            public void run() {
                try {
                    DbgLog.msg("Scanning USB bus");
                    this.f218a.f219a.scannedDevices = this.f218a.f219a.f223d.scanForUsbDevices();
                } catch (RobotCoreException e) {
                    DbgLog.error("Device scan failed");
                }
                this.f218a.f219a.runOnUiThread(new C00491(this));
            }
        }

        C00512(AutoConfigureActivity autoConfigureActivity) {
            this.f219a = autoConfigureActivity;
        }

        public void onClick(View view) {
            this.f219a.f225f = new Thread(new C00501(this));
            this.f219a.f225f.start();
        }
    }

    public AutoConfigureActivity() {
        this.scannedDevices = new HashMap();
        this.entries = new HashSet();
        this.f224e = new HashMap();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.f220a = this;
        setContentView(C0035R.layout.activity_autoconfigure);
        this.f226g = new Utility(this);
        this.f221b = (Button) findViewById(C0035R.id.configureLegacy);
        this.f222c = (Button) findViewById(C0035R.id.configureUSB);
        try {
            this.f223d = new HardwareDeviceManager(this.f220a, null);
        } catch (Exception e) {
            this.f226g.complainToast("Failed to open the Device Manager", this.f220a);
            DbgLog.error("Failed to open deviceManager: " + e.toString());
            DbgLog.logStacktrace(e);
        }
    }

    protected void onStart() {
        super.onStart();
        this.f226g.updateHeader(Utility.AUTOCONFIGURE_K9LEGACYBOT, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        String filenameFromPrefs = this.f226g.getFilenameFromPrefs(C0035R.string.pref_hardware_config_filename, Utility.NO_FILE);
        if (filenameFromPrefs.equals(Utility.AUTOCONFIGURE_K9LEGACYBOT) || filenameFromPrefs.equals(Utility.AUTOCONFIGURE_K9USBBOT)) {
            m128d();
        } else {
            m118a();
        }
        this.f221b.setOnClickListener(new C00481(this));
        this.f222c.setOnClickListener(new C00512(this));
    }

    private void m121a(String str) {
        this.f226g.writeXML(this.f224e);
        try {
            this.f226g.writeToFile(str + Utility.FILE_EXT);
            this.f226g.saveToPreferences(str, C0035R.string.pref_hardware_config_filename);
            this.f226g.updateHeader(str, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
            Toast.makeText(this.f220a, "AutoConfigure " + str + " Successful", 0).show();
        } catch (RobotCoreException e) {
            this.f226g.complainToast(e.getMessage(), this.f220a);
            DbgLog.error(e.getMessage());
        } catch (IOException e2) {
            this.f226g.complainToast("Found " + e2.getMessage() + "\n Please fix and re-save", this.f220a);
            DbgLog.error(e2.getMessage());
        }
    }

    private void m118a() {
        this.f226g.setOrangeText("No devices found!", "To configure K9LegacyBot, please: \n   1. Attach a LegacyModuleController, \n       with \n       a. MotorController in port 0, with a \n         motor in port 1 and port 2 \n       b. ServoController in port 1, with a \n         servo in port 1 and port 6 \n      c. IR seeker in port 2\n      d. Light sensor in port 3 \n   2. Press the K9LegacyBot button\n \nTo configure K9USBBot, please: \n   1. Attach a USBMotorController, with a \n       motor in port 1 and port 2 \n    2. USBServoController in port 1, with a \n      servo in port 1 and port 6 \n   3. LegacyModule, with \n      a. IR seeker in port 2\n      b. Light sensor in port 3 \n   4. Press the K9USBBot button", C0035R.id.autoconfigure_info, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
    }

    private void m123b() {
        String str = "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. LegacyModuleController, with \n " + "      a. MotorController in port 0, with a \n" + "          motor in port 1 and port 2 \n " + "      b. ServoController in port 1, with a \n" + "          servo in port 1 and port 6 \n" + "       c. IR seeker in port 2\n" + "       d. Light sensor in port 3 ";
        this.f226g.setOrangeText("Wrong devices found!", str, C0035R.id.autoconfigure_info, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
    }

    private void m125c() {
        String str = "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. USBMotorController with a \n" + "      motor in port 1 and port 2 \n " + "   2. USBServoController with a \n" + "      servo in port 1 and port 6 \n" + "   3. LegacyModuleController, with \n " + "       a. IR seeker in port 2\n" + "       b. Light sensor in port 3 ";
        this.f226g.setOrangeText("Wrong devices found!", str, C0035R.id.autoconfigure_info, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
    }

    private void m128d() {
        String str = BuildConfig.VERSION_NAME;
        this.f226g.setOrangeText("Already configured!", str, C0035R.id.autoconfigure_info, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
    }

    private boolean m129e() {
        boolean z = true;
        boolean z2 = true;
        boolean z3 = true;
        for (Entry entry : this.entries) {
            boolean z4;
            boolean z5;
            DeviceType deviceType = (DeviceType) entry.getValue();
            if (deviceType == DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && z3) {
                m120a((SerialNumber) entry.getKey(), "sensors");
                z4 = false;
            } else {
                z4 = z3;
            }
            if (deviceType == DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER && z2) {
                m114a((SerialNumber) entry.getKey(), "motor_1", "motor_2", "wheels");
                z3 = false;
            } else {
                z3 = z2;
            }
            if (deviceType == DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER && z) {
                m115a((SerialNumber) entry.getKey(), m131f(), "servos");
                z5 = false;
            } else {
                z5 = z;
            }
            z = z5;
            z2 = z3;
            z3 = z4;
        }
        if (z3 || z2 || z) {
            return false;
        }
        ((LinearLayout) findViewById(C0035R.id.autoconfigure_info)).removeAllViews();
        return true;
    }

    private ArrayList<String> m131f() {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("servo_1");
        arrayList.add(DeviceConfiguration.DISABLED_DEVICE_NAME);
        arrayList.add(DeviceConfiguration.DISABLED_DEVICE_NAME);
        arrayList.add(DeviceConfiguration.DISABLED_DEVICE_NAME);
        arrayList.add(DeviceConfiguration.DISABLED_DEVICE_NAME);
        arrayList.add("servo_6");
        return arrayList;
    }

    private void m120a(SerialNumber serialNumber, String str) {
        LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = (LegacyModuleControllerConfiguration) this.f224e.get(serialNumber);
        legacyModuleControllerConfiguration.setName(str);
        DeviceConfiguration a = m113a(ConfigurationType.IR_SEEKER, "ir_seeker", 2);
        DeviceConfiguration a2 = m113a(ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
        List arrayList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            if (i == 2) {
                arrayList.add(a);
            }
            if (i == 3) {
                arrayList.add(a2);
            } else {
                arrayList.add(new DeviceConfiguration(i));
            }
        }
        legacyModuleControllerConfiguration.addDevices(arrayList);
    }

    private boolean m134g() {
        boolean z = true;
        for (Entry entry : this.entries) {
            boolean z2;
            if (((DeviceType) entry.getValue()) == DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && z) {
                m124b((SerialNumber) entry.getKey(), "devices");
                z2 = false;
            } else {
                z2 = z;
            }
            z = z2;
        }
        if (z) {
            return false;
        }
        ((LinearLayout) findViewById(C0035R.id.autoconfigure_info)).removeAllViews();
        return true;
    }

    private void m124b(SerialNumber serialNumber, String str) {
        LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = (LegacyModuleControllerConfiguration) this.f224e.get(serialNumber);
        legacyModuleControllerConfiguration.setName(str);
        MotorControllerConfiguration a = m114a(ControllerConfiguration.NO_SERIAL_NUMBER, "motor_1", "motor_2", "wheels");
        a.setPort(0);
        ServoControllerConfiguration a2 = m115a(ControllerConfiguration.NO_SERIAL_NUMBER, m131f(), "servos");
        a2.setPort(1);
        DeviceConfiguration a3 = m113a(ConfigurationType.IR_SEEKER, "ir_seeker", 2);
        DeviceConfiguration a4 = m113a(ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
        List arrayList = new ArrayList();
        arrayList.add(a);
        arrayList.add(a2);
        arrayList.add(a3);
        arrayList.add(a4);
        for (int i = 4; i < 6; i++) {
            arrayList.add(new DeviceConfiguration(i));
        }
        legacyModuleControllerConfiguration.addDevices(arrayList);
    }

    private DeviceConfiguration m113a(ConfigurationType configurationType, String str, int i) {
        return new DeviceConfiguration(i, configurationType, str, true);
    }

    private MotorControllerConfiguration m114a(SerialNumber serialNumber, String str, String str2, String str3) {
        MotorControllerConfiguration motorControllerConfiguration;
        if (serialNumber.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
            motorControllerConfiguration = new MotorControllerConfiguration();
        } else {
            motorControllerConfiguration = (MotorControllerConfiguration) this.f224e.get(serialNumber);
        }
        motorControllerConfiguration.setName(str3);
        List arrayList = new ArrayList();
        MotorConfiguration motorConfiguration = new MotorConfiguration(1, str, true);
        MotorConfiguration motorConfiguration2 = new MotorConfiguration(2, str2, true);
        arrayList.add(motorConfiguration);
        arrayList.add(motorConfiguration2);
        motorControllerConfiguration.addMotors(arrayList);
        return motorControllerConfiguration;
    }

    private ServoControllerConfiguration m115a(SerialNumber serialNumber, ArrayList<String> arrayList, String str) {
        ServoControllerConfiguration servoControllerConfiguration;
        if (serialNumber.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
            servoControllerConfiguration = new ServoControllerConfiguration();
        } else {
            servoControllerConfiguration = (ServoControllerConfiguration) this.f224e.get(serialNumber);
        }
        servoControllerConfiguration.setName(str);
        ArrayList arrayList2 = new ArrayList();
        for (int i = 1; i <= 6; i++) {
            boolean z;
            String str2 = (String) arrayList.get(i - 1);
            if (str2.equals(DeviceConfiguration.DISABLED_DEVICE_NAME)) {
                z = false;
            } else {
                z = true;
            }
            arrayList2.add(new ServoConfiguration(i, str2, z));
        }
        servoControllerConfiguration.addServos(arrayList2);
        return servoControllerConfiguration;
    }
}
