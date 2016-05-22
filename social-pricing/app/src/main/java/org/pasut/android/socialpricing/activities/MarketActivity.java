package org.pasut.android.socialpricing.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.util.Lists;

import org.pasut.android.socialpricing.R;
import org.pasut.android.socialpricing.model.Market;

import java.util.Arrays;
import java.util.List;

public class MarketActivity extends AppCompatActivity {
    private static final String TAG = MarketActivity.class.getSimpleName();
    public static final String MARKET = "market";

    private String[] prices = new String[]{
            "a",
            "a",
            "a",
            "a",
            "a",
            "a",
            "a",
            "a",
            "a",
            "a"
    };

    private PricesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        Market market = getIntent().getParcelableExtra(MARKET);
        this.setTitle(market.getName());
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setSubtitle(market.getAddress());
        populatePrices();
    }

    private void populatePrices() {
        adapter = new PricesAdapter(this, prices);
        RecyclerView pricesView = (RecyclerView)findViewById(R.id.prices);
        pricesView.setLayoutManager(new LinearLayoutManager(this));
        pricesView.setAdapter(adapter);

        pricesView.setItemAnimator(new DefaultItemAnimator());
    }

    static class PricesAdapter extends RecyclerView.Adapter<PricesAdapter.ViewHolder> {
        private final List<String> prices;
        private final Context context;

        public PricesAdapter(final Context context, final String[] prices) {
            this.context = context;
            this.prices = Lists.newArrayList(Arrays.asList(prices));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.price_item, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
        }

        @Override
        public int getItemCount() {
            return prices.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                text = (TextView)itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
