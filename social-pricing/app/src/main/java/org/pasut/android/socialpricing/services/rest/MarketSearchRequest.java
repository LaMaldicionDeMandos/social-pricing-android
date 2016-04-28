package org.pasut.android.socialpricing.services.rest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Lists;
import com.google.common.reflect.TypeToken;

import org.pasut.android.socialpricing.model.Market;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by boot on 4/28/16.
 */
public abstract class MarketSearchRequest extends AbstractRequest<List<Market>> {
    private final static Class<List<Market>> getClazz() {
        List<Market> list = Lists.newArrayList();
        return (Class<List<Market>>)list.getClass();
    }

    private final static Type type = new TypeToken<List<Market>>(){}.getType();

    public MarketSearchRequest(final String protocol, final String host, final int port,
                                    final String template) {
        super(protocol, host, port, template, getClazz());
    }

    @Override
    public List<Market> loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(path));
        request.setParser(new GsonFactory().createJsonObjectParser());
        HttpResponse response = request.execute();
        List<Market> result = (List<Market>) response.parseAs(type);
        return result;
    }
}
