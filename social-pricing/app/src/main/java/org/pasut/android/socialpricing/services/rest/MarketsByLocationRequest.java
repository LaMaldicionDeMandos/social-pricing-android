package org.pasut.android.socialpricing.services.rest;

/**
 * Created by boot on 4/17/16.
 */
public class MarketsByLocationRequest extends MarketSearchRequest {
    private final static String TEMPLATE = "market/geo?lat=%s&lon=%s";


    public MarketsByLocationRequest(final String protocol, final String host, final int port,
                                    final Double lat, final Double lon) {
        super(protocol, host, port, String.format(TEMPLATE, lat.toString(), lon.toString()));
    }

}
