package com.qualcomm.robotcore.hardware.configuration;

import android.content.Context;
import android.util.Xml;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.xmlpull.v1.XmlSerializer;

public class WriteXMLFileHandler {
    private XmlSerializer f476a;
    private HashSet<String> f477b;
    private ArrayList<String> f478c;
    private String[] f479d;
    private int f480e;

    public WriteXMLFileHandler(Context context) {
        this.f477b = new HashSet();
        this.f478c = new ArrayList();
        this.f479d = new String[]{"    ", "        ", "            "};
        this.f480e = 0;
        this.f476a = Xml.newSerializer();
    }

    public String writeXml(ArrayList<ControllerConfiguration> deviceControllerConfigurations) {
        this.f478c = new ArrayList();
        this.f477b = new HashSet();
        Writer stringWriter = new StringWriter();
        try {
            this.f476a.setOutput(stringWriter);
            this.f476a.startDocument("UTF-8", Boolean.valueOf(true));
            this.f476a.ignorableWhitespace("\n");
            this.f476a.startTag(BuildConfig.VERSION_NAME, "Robot");
            this.f476a.ignorableWhitespace("\n");
            Iterator it = deviceControllerConfigurations.iterator();
            while (it.hasNext()) {
                ControllerConfiguration controllerConfiguration = (ControllerConfiguration) it.next();
                String configurationType = controllerConfiguration.getType().toString();
                if (configurationType.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString()) || configurationType.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString())) {
                    m302a(controllerConfiguration, true);
                }
                if (configurationType.equalsIgnoreCase(ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                    m306b(controllerConfiguration);
                }
                if (configurationType.equalsIgnoreCase(ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                    m301a(controllerConfiguration);
                }
            }
            this.f476a.endTag(BuildConfig.VERSION_NAME, "Robot");
            this.f476a.ignorableWhitespace("\n");
            this.f476a.endDocument();
            return stringWriter.toString();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void m304a(String str) {
        if (!str.equalsIgnoreCase(DeviceConfiguration.DISABLED_DEVICE_NAME)) {
            if (this.f477b.contains(str)) {
                this.f478c.add(str);
            } else {
                this.f477b.add(str);
            }
        }
    }

    private void m301a(ControllerConfiguration controllerConfiguration) throws IOException {
        this.f476a.ignorableWhitespace(this.f479d[this.f480e]);
        this.f476a.startTag(BuildConfig.VERSION_NAME, m305b(controllerConfiguration.getType().toString()));
        m304a(controllerConfiguration.getName());
        this.f476a.attribute(BuildConfig.VERSION_NAME, "name", controllerConfiguration.getName());
        this.f476a.attribute(BuildConfig.VERSION_NAME, "serialNumber", controllerConfiguration.getSerialNumber().toString());
        this.f476a.ignorableWhitespace("\n");
        this.f480e++;
        DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration) controllerConfiguration;
        Iterator it = ((ArrayList) deviceInterfaceModuleConfiguration.getPwmDevices()).iterator();
        while (it.hasNext()) {
            m303a((DeviceConfiguration) it.next());
        }
        it = ((ArrayList) deviceInterfaceModuleConfiguration.getI2cDevices()).iterator();
        while (it.hasNext()) {
            m303a((DeviceConfiguration) it.next());
        }
        it = ((ArrayList) deviceInterfaceModuleConfiguration.getAnalogInputDevices()).iterator();
        while (it.hasNext()) {
            m303a((DeviceConfiguration) it.next());
        }
        it = ((ArrayList) deviceInterfaceModuleConfiguration.getDigitalDevices()).iterator();
        while (it.hasNext()) {
            m303a((DeviceConfiguration) it.next());
        }
        Iterator it2 = ((ArrayList) deviceInterfaceModuleConfiguration.getAnalogOutputDevices()).iterator();
        while (it2.hasNext()) {
            m303a((DeviceConfiguration) it2.next());
        }
        this.f480e--;
        this.f476a.ignorableWhitespace(this.f479d[this.f480e]);
        this.f476a.endTag(BuildConfig.VERSION_NAME, m305b(controllerConfiguration.getType().toString()));
        this.f476a.ignorableWhitespace("\n");
    }

    private void m306b(ControllerConfiguration controllerConfiguration) throws IOException {
        this.f476a.ignorableWhitespace(this.f479d[this.f480e]);
        this.f476a.startTag(BuildConfig.VERSION_NAME, m305b(controllerConfiguration.getType().toString()));
        m304a(controllerConfiguration.getName());
        this.f476a.attribute(BuildConfig.VERSION_NAME, "name", controllerConfiguration.getName());
        this.f476a.attribute(BuildConfig.VERSION_NAME, "serialNumber", controllerConfiguration.getSerialNumber().toString());
        this.f476a.ignorableWhitespace("\n");
        this.f480e++;
        Iterator it = ((ArrayList) controllerConfiguration.getDevices()).iterator();
        while (it.hasNext()) {
            DeviceConfiguration deviceConfiguration = (DeviceConfiguration) it.next();
            String configurationType = deviceConfiguration.getType().toString();
            if (configurationType.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString()) || configurationType.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString()) || configurationType.equalsIgnoreCase(ConfigurationType.MATRIX_CONTROLLER.toString())) {
                m302a((ControllerConfiguration) deviceConfiguration, false);
            } else if (deviceConfiguration.isEnabled()) {
                m303a(deviceConfiguration);
            }
        }
        this.f480e--;
        this.f476a.ignorableWhitespace(this.f479d[this.f480e]);
        this.f476a.endTag(BuildConfig.VERSION_NAME, m305b(controllerConfiguration.getType().toString()));
        this.f476a.ignorableWhitespace("\n");
    }

    private void m302a(ControllerConfiguration controllerConfiguration, boolean z) throws IOException {
        this.f476a.ignorableWhitespace(this.f479d[this.f480e]);
        this.f476a.startTag(BuildConfig.VERSION_NAME, m305b(controllerConfiguration.getType().toString()));
        m304a(controllerConfiguration.getName());
        this.f476a.attribute(BuildConfig.VERSION_NAME, "name", controllerConfiguration.getName());
        if (z) {
            this.f476a.attribute(BuildConfig.VERSION_NAME, "serialNumber", controllerConfiguration.getSerialNumber().toString());
        } else {
            this.f476a.attribute(BuildConfig.VERSION_NAME, "port", String.valueOf(controllerConfiguration.getPort()));
        }
        this.f476a.ignorableWhitespace("\n");
        this.f480e++;
        Iterator it = ((ArrayList) controllerConfiguration.getDevices()).iterator();
        while (it.hasNext()) {
            DeviceConfiguration deviceConfiguration = (DeviceConfiguration) it.next();
            if (deviceConfiguration.isEnabled()) {
                m303a(deviceConfiguration);
            }
        }
        if (controllerConfiguration.getType() == ConfigurationType.MATRIX_CONTROLLER) {
            it = ((ArrayList) ((MatrixControllerConfiguration) controllerConfiguration).getMotors()).iterator();
            while (it.hasNext()) {
                deviceConfiguration = (DeviceConfiguration) it.next();
                if (deviceConfiguration.isEnabled()) {
                    m303a(deviceConfiguration);
                }
            }
            it = ((ArrayList) ((MatrixControllerConfiguration) controllerConfiguration).getServos()).iterator();
            while (it.hasNext()) {
                deviceConfiguration = (DeviceConfiguration) it.next();
                if (deviceConfiguration.isEnabled()) {
                    m303a(deviceConfiguration);
                }
            }
        }
        this.f480e--;
        this.f476a.ignorableWhitespace(this.f479d[this.f480e]);
        this.f476a.endTag(BuildConfig.VERSION_NAME, m305b(controllerConfiguration.getType().toString()));
        this.f476a.ignorableWhitespace("\n");
    }

    private void m303a(DeviceConfiguration deviceConfiguration) {
        if (deviceConfiguration.isEnabled()) {
            try {
                this.f476a.ignorableWhitespace(this.f479d[this.f480e]);
                this.f476a.startTag(BuildConfig.VERSION_NAME, m305b(deviceConfiguration.getType().toString()));
                m304a(deviceConfiguration.getName());
                this.f476a.attribute(BuildConfig.VERSION_NAME, "name", deviceConfiguration.getName());
                this.f476a.attribute(BuildConfig.VERSION_NAME, "port", String.valueOf(deviceConfiguration.getPort()));
                this.f476a.endTag(BuildConfig.VERSION_NAME, m305b(deviceConfiguration.getType().toString()));
                this.f476a.ignorableWhitespace("\n");
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeToFile(String data, String folderName, String filename) throws RobotCoreException, IOException {
        FileOutputStream fileOutputStream;
        Exception e;
        Throwable th;
        if (this.f478c.size() > 0) {
            throw new IOException("Duplicate names: " + this.f478c);
        }
        filename = filename.replaceFirst("[.][^.]+$", BuildConfig.VERSION_NAME);
        File file = new File(folderName);
        boolean z = true;
        if (!file.exists()) {
            z = file.mkdir();
        }
        if (z) {
            try {
                fileOutputStream = new FileOutputStream(new File(folderName + filename + Utility.FILE_EXT));
                try {
                    fileOutputStream.write(data.getBytes());
                    try {
                        fileOutputStream.close();
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return;
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.printStackTrace();
                        try {
                            fileOutputStream.close();
                            return;
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            return;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            fileOutputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                        throw th;
                    }
                }
            } catch (Exception e5) {
                e = e5;
                fileOutputStream = null;
                e.printStackTrace();
                fileOutputStream.close();
                return;
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
                fileOutputStream.close();
                throw th;
            }
        }
        throw new RobotCoreException("Unable to create directory");
    }

    private String m305b(String str) {
        String str2 = str.substring(0, 1) + str.substring(1).toLowerCase();
        int lastIndexOf = str.lastIndexOf("_");
        while (lastIndexOf > 0) {
            int i = lastIndexOf + 1;
            String substring = str2.substring(0, lastIndexOf);
            String toUpperCase = str2.substring(i, i + 1).toUpperCase();
            str2 = substring + toUpperCase + str2.substring(i + 1);
            lastIndexOf = str2.lastIndexOf("_");
        }
        return str2;
    }
}
