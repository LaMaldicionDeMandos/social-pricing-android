package org.pasut.android.socialpricing.services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.common.base.Preconditions;

import org.pasut.android.socialpricing.SocialPriceApplication;

/**
 * Created by boot on 3/20/16.
 */
public class MarketService {
    public final static String LOCATION_SEARCH_EVENT = "location_event";
    public final static String FAVORITE_SEARCH_EVENT = "favorite_event";
    public final static String SEARCH_SEARCH_EVENT = "search_event";

    private Context context;
    private RestService restService;

    public MarketService(final Context context) {
        SocialPriceApplication app = (SocialPriceApplication) context.getApplicationContext();
        this.restService = app.getRestService();
        this.context = Preconditions.checkNotNull(context);
    }

    public void searchByAddress(final String address, final String locale) {
        //TODO Implement request ya tengo el rest service, solo tengo que implementar la llamada
        Intent intent = new Intent(SEARCH_SEARCH_EVENT);
        intent.putExtra("data", new Parcelable[]{});
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void destroy() {
        this.context = null;
        this.restService = null;
    }
}
