package org.pasut.android.socialpricing;

import android.app.Application;

import com.octo.android.robospice.SpiceManager;

import org.pasut.android.socialpricing.services.RestService;
import org.pasut.android.socialpricing.services.RestSpiceService;

/**
 * Created by boot on 3/20/16.
 */
public class SocialPriceApplication extends Application {
    private RestService restService;

    @Override
    public void onCreate() {
        super.onCreate();
        SpiceManager spiceManager = new SpiceManager(RestSpiceService.class);
        restService = new RestService(spiceManager);
    }

    public RestService getRestService() {
        return restService;
    }
}
