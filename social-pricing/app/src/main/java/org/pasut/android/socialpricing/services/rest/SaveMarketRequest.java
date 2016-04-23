package org.pasut.android.socialpricing.services.rest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;

import org.pasut.android.socialpricing.model.Market;

/**
 * Created by boot on 4/17/16.
 */
public class SaveMarketRequest extends AbstractRequest<Market> {
    private final static String PATH = "market";
    private final static Class<Market> getClazz() {
        return Market.class;
    }

    private final Market market;

    public SaveMarketRequest(final String protocol, final String host, final int port,
                             final Market market) {
        super(protocol, host, port, PATH, getClazz());
        this.market = market;
    }

    @Override
    public Market loadDataFromNetwork() throws Exception {
        HttpContent content = new JsonHttpContent(new GsonFactory(), market);
        HttpRequest request = getHttpRequestFactory().buildPostRequest(new GenericUrl(path), content);
        HttpHeaders headers = request.getHeaders();
        headers.setContentType("application/json");
        request.setParser(new GsonFactory().createJsonObjectParser());
        HttpResponse response = request.execute();
        Market result = response.parseAs(getClazz());
        return result;
    }
}
