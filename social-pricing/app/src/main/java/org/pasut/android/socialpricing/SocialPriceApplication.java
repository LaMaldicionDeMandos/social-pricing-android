package org.pasut.android.socialpricing;

import android.app.Application;

import com.octo.android.robospice.SpiceManager;

import org.pasut.android.socialpricing.services.DefaultPreferencesService;
import org.pasut.android.socialpricing.services.PreferencesService;
import org.pasut.android.socialpricing.services.RestService;
import org.pasut.android.socialpricing.services.RestSpiceService;

/**
 * Created by boot on 3/20/16.
 */
public class SocialPriceApplication extends Application {
    private PreferencesService preferencesService;

    @Override
    public void onCreate() {
        super.onCreate();
        preferencesService = new DefaultPreferencesService(this);
    }

    public RestService getRestService() {
        return new RestService(new SpiceManager(RestSpiceService.class), this);
    }
    public PreferencesService getPreferenceService() {
        return preferencesService;
    }
}
