package com.auto.jarvis.libraryicognite.estimote;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.UUID;

/**
 * Created by HaVH on 2/6/17.
 */

public class BeaconID {
    private UUID proximityUUID;
    private String macAddress;
    private int minor;
    private int major;
    private double distance;

    public BeaconID() {
    }

    public BeaconID(String UUIDString, int major, int minor) {
        this(UUID.fromString(UUIDString), major, minor);
    }

    public BeaconID(UUID proximityUUID, int major, int minor) {
        this.proximityUUID = proximityUUID;
        this.major = major;
        this.minor = minor;
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

    public UUID getProximityUUID() {
        return proximityUUID;
    }

    public Region toBeaconRegion() {
        return new Region(toString(), getProximityUUID(), getMajor(), getMinor());
    }

    public String toString() {
        return getProximityUUID().toString() + ":" + getMinor() + ":" + getMinor();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this ) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return super.equals(obj);
        }

        BeaconID other = (BeaconID) obj;

        return getProximityUUID().equals(other.getProximityUUID())
                && getMajor() == other.getMajor()
                && getMinor() == other.getMinor();
    }
}
