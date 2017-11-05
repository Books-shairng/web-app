package com.ninjabooks.utils;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class TestDevice implements Device
{
    public static Device createDevice() {
        return new TestDevice();
    }

    @Override
    public boolean isNormal() {
        return false;
    }

    @Override
    public boolean isMobile() {
        return false;
    }

    @Override
    public boolean isTablet() {
        return false;
    }

    @Override
    public DevicePlatform getDevicePlatform() {
        return null;
    }
}
