package org.pasut.android.socialpricing.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boot on 4/17/16.
 */
public class GeoLocation implements Parcelable {
    private double lat;
    private double lon;

    public GeoLocation(final double lat, final double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
    }

    protected GeoLocation(Parcel in) {
        this.lat = in.readDouble();
        this.lon = in.readDouble();
    }

    public static final Parcelable.Creator<GeoLocation> CREATOR = new Parcelable.Creator<GeoLocation>() {
        @Override
        public GeoLocation createFromParcel(Parcel source) {
            return new GeoLocation(source);
        }

        @Override
        public GeoLocation[] newArray(int size) {
            return new GeoLocation[size];
        }
    };
}
