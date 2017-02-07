package com.auto.jarvis.libraryicognite.estimote;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

/**
 * Created by HaVH on 2/6/17.
 */

public class BeaconID {
    private String macAddress;
    private int minor;
    private int major;
    private double distance;

    public BeaconID() {
    }

    public BeaconID(String macAddress, int minor, int major) {
        this.macAddress = macAddress;
        this.minor = minor;
        this.major = major;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public static BeaconID fromEstimote(Beacon beacon) {
        BeaconID beaconID = new BeaconID();
        beaconID.macAddress = beacon.getMacAddress().toStandardString();
        beaconID.distance = Utils.computeAccuracy(beacon);
        beaconID.major = beacon.getMajor();
        beaconID.minor = beacon.getMinor();
        return beaconID;

    }
}
