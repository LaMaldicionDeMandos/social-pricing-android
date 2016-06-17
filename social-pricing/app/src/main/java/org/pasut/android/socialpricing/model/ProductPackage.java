package org.pasut.android.socialpricing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by boot on 6/17/16.
 */
public class ProductPackage implements Parcelable {
    @Key
    private ProductSpec spec;
    @Key
    private Product localProduct;
    @Key
    private List<Product> near;

    public ProductPackage() {}

    public ProductPackage(final ProductSpec spec, final Product localProduct,
                          final List<Product> products) {
        this.spec = spec;
        this.localProduct = localProduct;
        this.near = products;
    }

    public ProductPackage(final Parcel in) {
        this.spec = in.readParcelable(ProductSpec.class.getClassLoader());
        this.localProduct = in.readParcelable(Product.class.getClassLoader());
        this.near = in.readArrayList(Product.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.spec, flags);
        dest.writeParcelable(this.localProduct, flags);
        dest.writeParcelableArray(this.near.toArray(new Product[0]), flags);
    }

    public static final Parcelable.Creator<ProductPackage> CREATOR = new Parcelable.Creator<ProductPackage>() {
        @Override
        public ProductPackage createFromParcel(Parcel source) {
            return new ProductPackage(source);
        }

        @Override
        public ProductPackage[] newArray(int size) {
            return new ProductPackage[size];
        }
    };

    public ProductSpec getSpec() {
        return spec;
    }

    public void setSpec(ProductSpec spec) {
        this.spec = spec;
    }

    public Product getLocalProduct() {
        return localProduct;
    }

    public void setLocalProduct(Product localProduct) {
        this.localProduct = localProduct;
    }

    public List<Product> getNear() {
        return near;
    }

    public void setNear(List<Product> near) {
        this.near = near;
    }
}
