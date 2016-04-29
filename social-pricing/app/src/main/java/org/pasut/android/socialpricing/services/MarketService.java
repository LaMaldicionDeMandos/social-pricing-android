package org.pasut.android.socialpricing.services;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.api.client.util.Lists;
import com.google.common.base.Preconditions;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.pasut.android.socialpricing.SocialPriceApplication;
import org.pasut.android.socialpricing.model.Market;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by boot on 3/20/16.
 */
public class MarketService {
    private final static String TAG = MarketService.class.getSimpleName();
    public final static String LOCATION_SEARCH_EVENT = "location_event";
    public final static String FAVORITE_SEARCH_EVENT = "favorite_event";
    public final static String ARRIVE_MARKETS_EVENT = "arrive_markets_event";

    private Context context;
    private RestService restService;

    public MarketService(final Context context) {
        SocialPriceApplication app = (SocialPriceApplication) context.getApplicationContext();
        this.restService = app.getRestService();
        this.restService.start(context);
        this.context = Preconditions.checkNotNull(context);
    }

    public void searchByAddress(final String address, final String locale) {
        restService.marketsByAddress(address, locale, new MarketsListener());
    }

    public void searchByLocation(final double lat, final double lon) {
        restService.marketsByLocation(lat, lon, new MarketsListener());
    }

    public void save(final Market market) {
        final MarketsListener listener = new MarketsListener();
        restService.saveMarket(market, new RequestListener<Market>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(Market market) {
                ArrayList<Market> list = new ArrayList<>(1);
                list.add(market);
                listener.onRequestSuccess(list);
            }
        });
    }

    private void sendList(final String event, List<Market> list) {
        final Intent intent = new Intent(event);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) list);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void destroy() {
        this.context = null;
        this.restService.shouldStop();
        this.restService = null;
    }

    class MarketsListener implements RequestListener<List<Market>> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.e(TAG, "Searching Markets: " + spiceException.getMessage());
            sendList(ARRIVE_MARKETS_EVENT, new ArrayList<Market>());
        }

        @Override
        public void onRequestSuccess(List<Market> markets) {
            sendList(ARRIVE_MARKETS_EVENT, markets);
        }
    }
}
