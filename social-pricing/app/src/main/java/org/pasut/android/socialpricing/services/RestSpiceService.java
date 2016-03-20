package org.pasut.android.socialpricing.services;

import android.app.Application;

import com.octo.android.robospice.GoogleHttpClientSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.googlehttpclient.json.GsonObjectPersisterFactory;

/**
 * Created by boot on 3/20/16.
 */
public class RestSpiceService extends GoogleHttpClientSpiceService {
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        GsonObjectPersisterFactory gsonFactory = new GsonObjectPersisterFactory(application);

        cacheManager.addPersister(gsonFactory);

        return cacheManager;
    }

    @Override
    public int getThreadCount(){
        return 6;
    }
}
