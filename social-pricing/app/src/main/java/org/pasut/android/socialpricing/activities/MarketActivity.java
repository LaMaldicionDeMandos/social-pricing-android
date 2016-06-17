package org.pasut.android.socialpricing.activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.Lists;
import com.google.zxing.client.android.CaptureActivity;

import org.pasut.android.socialpricing.R;
import org.pasut.android.socialpricing.model.Market;
import org.pasut.android.socialpricing.model.ProductPackage;
import org.pasut.android.socialpricing.services.MarketService;
import org.pasut.android.socialpricing.services.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.pasut.android.socialpricing.services.MarketService.ARRIVE_MARKETS_EVENT;

public class MarketActivity extends AppCompatActivity {
    static final int SCAN_REQUEST = 1;
    static final int NEW_PRODUCT_REQUEST = 2;
    private static final String TAG = MarketActivity.class.getSimpleName();
    public static final String MARKET = "market";

    private String[] prices = new String[]{};

    private PricesAdapter adapter;

    private ImageView doneEdit;
    private AnimatedVectorDrawable doneToEdit;
    private AnimatedVectorDrawable editToDone;
    private boolean done = false;

    private Market market;
    private ProductPackage product;

    private ProductService productService;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productService = new ProductService(this);
        setContentView(R.layout.activity_market);
        doneEdit = (ImageView)findViewById(R.id.done_edit);
        market = getIntent().getParcelableExtra(MARKET);
        if (market == null) {
            finish();
            return;
        }
        this.setTitle(market.getName());
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setSubtitle(market.getAddress());
        populatePrices();
        LocalBroadcastManager.getInstance(this).registerReceiver(productReceiver,
                new IntentFilter(ProductService.FOUND_PRODUCT_EVENT));
        populateProduct(product);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_market, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_product) {
            Toast.makeText(this, "Oops todavia no esta implementado", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save its state
        outState.putParcelable(MARKET, market);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(productReceiver);

        productService.destroy();

        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void editDonePrice(View view) {
        ImageView doneEdit = (ImageView) findViewById(R.id.done_edit);
        AnimatedVectorDrawable doneToEdit = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_done_edit);
        AnimatedVectorDrawable editToDone = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_edit_done);

        AnimatedVectorDrawable drawable = done ? doneToEdit : editToDone;
        doneEdit.setImageDrawable(drawable);
        drawable.start();
        done = !done;
        changeToEditPrice(done);
    }

    public void editPrice(View view) {
        ImageView doneEdit = (ImageView) findViewById(R.id.done_edit);
        Drawable editDrawable = getResources().getDrawable(R.drawable.ic_edit_white_24dp);
        Drawable doneDrawable = getResources().getDrawable(R.drawable.ic_done_white_24dp);
        Drawable drawable = !done ? doneDrawable : editDrawable;
        doneEdit.setImageDrawable(drawable);
        done = !done;
        changeToEditPrice(done);
    }

    public void scan(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, SCAN_REQUEST);
    }

    private void changeToEditPrice(boolean toEdit) {
        View label = findViewById(R.id.product_price);
        View edit = findViewById(R.id.product_price_edit);
        View toMakeVisible = toEdit ? edit : label;
        View toMakeInvisible = toEdit ? label : edit;
        toMakeVisible.setVisibility(View.VISIBLE);
        toMakeInvisible.setVisibility(View.INVISIBLE);
    }

    private void populatePrices() {
        adapter = new PricesAdapter(this, prices);
        RecyclerView pricesView = (RecyclerView)findViewById(R.id.prices);
        pricesView.setLayoutManager(new LinearLayoutManager(this));
        pricesView.setAdapter(adapter);

        pricesView.setItemAnimator(new DefaultItemAnimator());
    }

    private void populateProduct(final ProductPackage product) {
        if (product == null) {
            doneEdit.setVisibility(View.INVISIBLE);
        } else {
            doneEdit.setVisibility(View.VISIBLE);
            //TODO me falta popular todos los datos
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SCAN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                scanCode(data.getStringExtra("result"));
            }
        }
    }

    private void scanCode(final String code) {
        productService.search(code, market);
    }

    private void showNotFoundDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        new NewProductDialogFragment().show(ft, "new Product Dialog");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }

    private final BroadcastReceiver productReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            product = intent.getExtras().getParcelable("data");
            populateProduct(product);
            if (product == null) {
                showNotFoundDialog();
            }
        }
    };

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

    public static class NewProductDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.not_found_product_title)
                    .setMessage(R.string.not_found_product_desc)
                    .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(), ProductFormActivity.class);
                            startActivityForResult(intent, NEW_PRODUCT_REQUEST);
                        }
                    })
                    .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
        }
    }
}
