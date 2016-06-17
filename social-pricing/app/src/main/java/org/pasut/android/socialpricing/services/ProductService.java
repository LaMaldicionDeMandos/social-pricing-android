package org.pasut.android.socialpricing.services;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.pasut.android.socialpricing.model.Market;

/**
 * Created by boot on 6/14/16.
 */
public class ProductService extends ModelService {
    private final static String TAG = ProductService.class.getSimpleName();
    public final static String FOUND_PRODUCT_EVENT = "found_product_event";

    public ProductService(final Context context) {
        super(context);
    }

    public void search(final String code, final Market market) {
        restService.productsNear(code, market, new RequestListener<Object>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Searching Markets: " + spiceException.getMessage());
                send(FOUND_PRODUCT_EVENT, (String)null);
            }

            @Override
            public void onRequestSuccess(Object product) {
                send(FOUND_PRODUCT_EVENT, product == null ? null : product.toString());
            }
        });
    }

}
