package org.pasut.android.socialpricing.services.rest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Lists;

import org.pasut.android.socialpricing.model.Market;

import java.util.List;

/**
 * Created by boot on 4/17/16.
 */
public class MarketsByAddressRequest extends AbstractRequest<List<Market>> {
    private final static String TEMPLATE = "market/address?address=%s&locale=%s";
    private final static Class<List<Market>> getClazz() {
        List<Market> list = Lists.newArrayList();
        return (Class<List<Market>>)list.getClass();
    }

    public MarketsByAddressRequest(final String protocol, final String host, final int port,
                                   final String address, final String locale) {
        super(protocol, host, port, String.format(TEMPLATE, address, locale), getClazz());
    }

    @Override
    public List<Market> loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(path));
        request.setParser(new GsonFactory().createJsonObjectParser());
        HttpResponse response = request.execute();
        List<Market> result = response.parseAs(getClazz());
        return result;
    }
}
