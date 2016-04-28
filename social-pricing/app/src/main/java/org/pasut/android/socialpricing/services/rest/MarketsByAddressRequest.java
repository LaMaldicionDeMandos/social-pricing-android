package org.pasut.android.socialpricing.services.rest;

/**
 * Created by boot on 4/17/16.
 */
public class MarketsByAddressRequest extends MarketSearchRequest {
    private final static String TEMPLATE = "market/address?address=%s&locale=%s";

    public MarketsByAddressRequest(final String protocol, final String host, final int port,
                                   final String address, final String locale) {
        super(protocol, host, port, String.format(TEMPLATE, address, locale));
    }
}
