package org.pasut.android.socialpricing.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.pasut.android.socialpricing.R;
import org.pasut.android.socialpricing.model.Market;

public class MarketActivity extends AppCompatActivity {
    private static final String TAG = MarketActivity.class.getSimpleName();
    public static final String MARKET = "market";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        Market market = getIntent().getParcelableExtra(MARKET);
        this.setTitle(market.getName());
    }
}
