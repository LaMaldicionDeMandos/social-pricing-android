package org.pasut.android.socialpricing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.Key;

/**
 * Created by boot on 6/17/16.
 */
public class ProductSpec implements Parcelable {
    @Key
    private String code;
    @Key
    private String tradeMark;
    @Key
    private String subtype;
    @Key
    private float measure;
    @Key
    private MeasureType measureType;
    @Key
    private String description;

    public enum MeasureType {
        ML, L, G, KG, CM3, U
    }

    public ProductSpec(){}

    public ProductSpec(final String code, final String tradeMark, final String subtype,
                       final float measure, final MeasureType measureType, final String description) {
        this.code = code;
        this.tradeMark = tradeMark;
        this.subtype = subtype;
        this.measure = measure;
        this.measureType = measureType;
        this.description = description;
    }

    public ProductSpec(Parcel in) {
        this.code = in.readString();
        this.tradeMark = in.readString();
        this.subtype = in.readString();
        this.measure = in.readFloat();
        this.description = in.readString();
        this.measureType = MeasureType.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.tradeMark);
        dest.writeString(this.subtype);
        dest.writeFloat(this.measure);
        dest.writeString(this.description);
        dest.writeString(this.measureType.name());
    }

    public static final Parcelable.Creator<ProductSpec> CREATOR = new Parcelable.Creator<ProductSpec>() {
        @Override
        public ProductSpec createFromParcel(Parcel source) {
            return new ProductSpec(source);
        }

        @Override
        public ProductSpec[] newArray(int size) {
            return new ProductSpec[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(String tradeMark) {
        this.tradeMark = tradeMark;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public float getMeasure() {
        return measure;
    }

    public void setMeasure(float measure) {
        this.measure = measure;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
