package org.pasut.android.socialpricing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.Key;

/**
 * Created by boot on 6/17/16.
 */
public class Product implements Parcelable {
    @Key
    private String id;
    @Key
    private String code;
    @Key
    private String marketId;
    @Key
    private float price;
    @Key
    private int updateValue;
    @Key
    private String updateUnit;
    @Key
    private Market market;

    public Product() {}

    public Product(final String id, final String code, final String marketId, final float price,
                   final int updateValue, final String updateUnit, final Market market) {
        this.id = id;
        this.code = code;
        this.marketId = marketId;
        this.price = price;
        this.updateValue = updateValue;
        this.updateUnit = updateUnit;
        this.market = market;
    }

    public Product(Parcel in) {
        this.id = in.readString();
        this.code = in.readString();
        this.marketId = in.readString();
        this.price = in.readFloat();
        this.updateValue = in.readInt();
        this.updateUnit = in.readString();
        this.market = in.readParcelable(Market.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.code);
        dest.writeString(this.marketId);
        dest.writeFloat(this.price);
        dest.writeInt(this.updateValue);
        dest.writeString(this.updateUnit);
        dest.writeParcelable(this.market, flags);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getUpdateValue() {
        return updateValue;
    }

    public void setUpdateValue(int updateValue) {
        this.updateValue = updateValue;
    }

    public String getUpdateUnit() {
        return updateUnit;
    }

    public void setUpdateUnit(String updateUnit) {
        this.updateUnit = updateUnit;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }
}
