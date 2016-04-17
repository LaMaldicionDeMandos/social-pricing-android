package org.pasut.android.socialpricing.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boot on 4/17/16.
 */
public class Market implements Parcelable {
    private final String id;
    private final String name;
    private final String address;
    private final String normalizedAddress;
    private final String locale;
    private final GeoLocation geo;

    public Market(final String id, final String name, final String address, final String locale,
                  final String normalizedAddress, final GeoLocation geo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.normalizedAddress = normalizedAddress;
        this.locale = locale;
        this.geo = geo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.normalizedAddress);
        dest.writeString(this.locale);
        dest.writeParcelable(this.geo, flags);
    }

    protected Market(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.normalizedAddress = in.readString();
        this.locale = in.readString();
        this.geo = in.readParcelable(GeoLocation.class.getClassLoader());
    }

    public static final Parcelable.Creator<Market> CREATOR = new Parcelable.Creator<Market>() {
        @Override
        public Market createFromParcel(Parcel source) {
            return new Market(source);
        }

        @Override
        public Market[] newArray(int size) {
            return new Market[size];
        }
    };
}
