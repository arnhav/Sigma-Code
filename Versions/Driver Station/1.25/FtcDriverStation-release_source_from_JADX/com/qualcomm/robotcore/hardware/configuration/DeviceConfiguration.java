package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.BuildConfig;
import java.io.Serializable;

public class DeviceConfiguration implements Serializable {
    public static final String DISABLED_DEVICE_NAME = "NO DEVICE ATTACHED";
    private ConfigurationType f449a;
    private int f450b;
    private boolean f451c;
    protected String name;

    public enum ConfigurationType {
        MOTOR,
        SERVO,
        GYRO,
        COMPASS,
        IR_SEEKER,
        LIGHT_SENSOR,
        ACCELEROMETER,
        MOTOR_CONTROLLER,
        SERVO_CONTROLLER,
        LEGACY_MODULE_CONTROLLER,
        DEVICE_INTERFACE_MODULE,
        I2C_DEVICE,
        ANALOG_INPUT,
        TOUCH_SENSOR,
        OPTICAL_DISTANCE_SENSOR,
        ANALOG_OUTPUT,
        DIGITAL_DEVICE,
        PULSE_WIDTH_DEVICE,
        IR_SEEKER_V3,
        TOUCH_SENSOR_MULTIPLEXER,
        MATRIX_CONTROLLER,
        ULTRASONIC_SENSOR,
        ADAFRUIT_COLOR_SENSOR,
        COLOR_SENSOR,
        LED,
        OTHER,
        NOTHING
    }

    public DeviceConfiguration(int port, ConfigurationType type, String name, boolean enabled) {
        this.f449a = ConfigurationType.NOTHING;
        this.f451c = false;
        this.f450b = port;
        this.f449a = type;
        this.name = name;
        this.f451c = enabled;
    }

    public DeviceConfiguration(int port) {
        this.f449a = ConfigurationType.NOTHING;
        this.f451c = false;
        this.name = DISABLED_DEVICE_NAME;
        this.f449a = ConfigurationType.NOTHING;
        this.f450b = port;
        this.f451c = false;
    }

    public DeviceConfiguration(ConfigurationType type) {
        this.f449a = ConfigurationType.NOTHING;
        this.f451c = false;
        this.name = BuildConfig.VERSION_NAME;
        this.f449a = type;
        this.f451c = false;
    }

    public DeviceConfiguration(int port, ConfigurationType type) {
        this.f449a = ConfigurationType.NOTHING;
        this.f451c = false;
        this.name = DISABLED_DEVICE_NAME;
        this.f449a = type;
        this.f450b = port;
        this.f451c = false;
    }

    public boolean isEnabled() {
        return this.f451c;
    }

    public void setEnabled(boolean enabled) {
        this.f451c = enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setType(ConfigurationType type) {
        this.f449a = type;
    }

    public ConfigurationType getType() {
        return this.f449a;
    }

    public int getPort() {
        return this.f450b;
    }

    public void setPort(int port) {
        this.f450b = port;
    }

    public ConfigurationType typeFromString(String type) {
        for (ConfigurationType configurationType : ConfigurationType.values()) {
            if (type.equalsIgnoreCase(configurationType.toString())) {
                return configurationType;
            }
        }
        return ConfigurationType.NOTHING;
    }
}
