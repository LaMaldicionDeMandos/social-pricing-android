package org.pasut.android.socialpricing.services.rest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;

import org.pasut.android.socialpricing.model.Market;
import org.pasut.android.socialpricing.model.ProductPackage;

/**
 * Created by boot on 4/28/16.
 */
public class SearchProductNearRequest extends AbstractRequest<ProductPackage> {
    private final static String TEMPLATE = "product/%s/market/%s";
    private final static Class<ProductPackage> getClazz() {
        return ProductPackage.class;
    }

    public SearchProductNearRequest(final String protocol, final String host, final int port,
                                    final String code, final Market market) {
        super(protocol, host, port, String.format(TEMPLATE, code, market.getId()), getClazz());
    }

    @Override
    public ProductPackage loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(path));
        request.setParser(new GsonFactory().createJsonObjectParser());
        request.setThrowExceptionOnExecuteError(false);
        HttpResponse response = request.execute();
        return (response.getStatusCode() == 404)
                ? null
                : response.parseAs(getClazz());
    }
}
