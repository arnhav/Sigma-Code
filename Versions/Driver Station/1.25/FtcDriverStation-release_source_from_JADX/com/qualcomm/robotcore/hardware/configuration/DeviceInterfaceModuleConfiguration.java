package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class DeviceInterfaceModuleConfiguration extends ControllerConfiguration {
    private List<DeviceConfiguration> f725a;
    private List<DeviceConfiguration> f726b;
    private List<DeviceConfiguration> f727c;
    private List<DeviceConfiguration> f728d;
    private List<DeviceConfiguration> f729e;

    public DeviceInterfaceModuleConfiguration(String name, SerialNumber serialNumber) {
        super(name, serialNumber, ConfigurationType.DEVICE_INTERFACE_MODULE);
    }

    public void setPwmDevices(List<DeviceConfiguration> pwmDevices) {
        this.f725a = pwmDevices;
    }

    public List<DeviceConfiguration> getPwmDevices() {
        return this.f725a;
    }

    public List<DeviceConfiguration> getI2cDevices() {
        return this.f726b;
    }

    public void setI2cDevices(List<DeviceConfiguration> i2cDevices) {
        this.f726b = i2cDevices;
    }

    public List<DeviceConfiguration> getAnalogInputDevices() {
        return this.f727c;
    }

    public void setAnalogInputDevices(List<DeviceConfiguration> analogInputDevices) {
        this.f727c = analogInputDevices;
    }

    public List<DeviceConfiguration> getDigitalDevices() {
        return this.f728d;
    }

    public void setDigitalDevices(List<DeviceConfiguration> digitalDevices) {
        this.f728d = digitalDevices;
    }

    public List<DeviceConfiguration> getAnalogOutputDevices() {
        return this.f729e;
    }

    public void setAnalogOutputDevices(List<DeviceConfiguration> analogOutputDevices) {
        this.f729e = analogOutputDevices;
    }
}
