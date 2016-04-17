package org.pasut.android.socialpricing.services;

import android.content.Context;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.listener.RequestListener;

import org.pasut.android.socialpricing.R;
import org.pasut.android.socialpricing.model.Market;
import org.pasut.android.socialpricing.services.rest.AbstractRequest;

import java.util.List;

import static com.octo.android.robospice.persistence.DurationInMillis.ONE_DAY;

/**
 * Created by boot on 3/20/16.
 */
public class RestService {
    private final SpiceManager spice;
    private final String protocol;
    private final String host;
    private final int port;

    public RestService(final SpiceManager spice, final Context context) {
        this.spice = spice;
        this.protocol = context.getString(R.string.protocol);
        this.host = context.getString(R.string.host);
        this.port = context.getResources().getInteger(R.integer.port);

    }

    private <T> void executeRequest(AbstractRequest<T> request, RequestListener<T> listener) {
        spice.execute(request, request.cacheKey(), ONE_DAY, listener);
    }

    public void marketsByAddress(final String address, final String locale,
                                 final RequestListener<List<Market>> listener) {

    }
}
