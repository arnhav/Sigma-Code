package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.List;

public class MatrixControllerConfiguration extends ControllerConfiguration {
    private List<DeviceConfiguration> f730a;
    private List<DeviceConfiguration> f731b;

    public MatrixControllerConfiguration(String name, List<DeviceConfiguration> motors, List<DeviceConfiguration> servos, SerialNumber serialNumber) {
        super(name, serialNumber, ConfigurationType.MATRIX_CONTROLLER);
        this.f730a = servos;
        this.f731b = motors;
    }

    public List<DeviceConfiguration> getServos() {
        return this.f730a;
    }

    public void addServos(ArrayList<DeviceConfiguration> servos) {
        this.f730a = servos;
    }

    public List<DeviceConfiguration> getMotors() {
        return this.f731b;
    }

    public void addMotors(ArrayList<DeviceConfiguration> motors) {
        this.f731b = motors;
    }
}
