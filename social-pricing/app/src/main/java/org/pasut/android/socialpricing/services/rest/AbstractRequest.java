package org.pasut.android.socialpricing.services.rest;

import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

/**
 * Created by boot on 4/17/16.
 */
public abstract class AbstractRequest<T> extends GoogleHttpClientSpiceRequest<T> {
    protected final String url;

    public AbstractRequest(final String protocol, final String host, final int port, final Class<T> clazz) {
        super(clazz);
        this.url = protocol + host + ":" + port + "/";
    }

    public abstract String cacheKey();
}
