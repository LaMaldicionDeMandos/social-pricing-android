package org.pasut.android.socialpricing.services;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.common.base.Preconditions;

import org.pasut.android.socialpricing.SocialPriceApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boot on 6/17/16.
 */
public abstract class ModelService implements Destroyable {
    protected Context context;
    protected RestService restService;

    public ModelService(final Context context) {
        SocialPriceApplication app = (SocialPriceApplication) context.getApplicationContext();
        this.restService = app.getRestService();
        this.restService.start(context);
        this.context = Preconditions.checkNotNull(context);
    }

    @Override
    public void destroy() {
        this.context = null;
        this.restService.shouldStop();
        this.restService = null;
    }

    protected <T> void sendList(final String event, List<T> list) {
        final Intent intent = new Intent(event);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) list);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    protected void send(final String event, String result) {
        final Intent intent = new Intent(event);
        intent.putExtra("data", result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    protected <T extends Parcelable> void send(final String event, T result) {
        final Intent intent = new Intent(event);
        intent.putExtra("data", result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
