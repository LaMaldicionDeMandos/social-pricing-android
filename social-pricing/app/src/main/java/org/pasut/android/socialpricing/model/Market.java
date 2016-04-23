package org.pasut.android.socialpricing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Key;

/**
 * Created by boot on 4/17/16.
 */
public class Market implements Parcelable {
    @Key
    private String id;
    @Key
    private String name;
    @Key
    private String address;
    @Key
    private String normalizedAddress;
    @Key
    private String locale;
    @Key
    private GeoLocation geo;

    public Market(){}

    public Market(final String id, final String name, final String address, final String locale,
                  final GeoLocation geo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.normalizedAddress = address;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNormalizedAddress(String normalizedAddress) {
        this.normalizedAddress = normalizedAddress;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setGeo(GeoLocation geo) {
        this.geo = geo;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getNormalizedAddress() {
        return normalizedAddress;
    }

    public String getLocale() {
        return locale;
    }

    public GeoLocation getGeo() {
        return geo;
    }
}
