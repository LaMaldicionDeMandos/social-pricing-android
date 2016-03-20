package org.pasut.socialpricing.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.pasut.android.socialpricing.R;
import org.pasut.socialpricing.services.MarketService;

import static org.pasut.socialpricing.services.MarketService.FAVORITE_SEARCH_EVENT;
import static org.pasut.socialpricing.services.MarketService.LOCATION_SEARCH_EVENT;
import static org.pasut.socialpricing.services.MarketService.SEARCH_SEARCH_EVENT;

public class InitActivity extends AppCompatActivity {
    private MarketService marketService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marketService = new MarketService(this);
        setContentView(R.layout.activity_init);
        View locationButton = findViewById(R.id.locationButton);
        View favoriteButton = findViewById(R.id.favoriteButton);
        View searchButton = findViewById(R.id.searchButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Por ahora mockeo la llamada al servicio
                Intent intent = new Intent(LOCATION_SEARCH_EVENT);
                intent.putExtra("data", new Parcelable[]{});
                LocalBroadcastManager.getInstance(InitActivity.this).sendBroadcast(intent);
            }
        });
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Por ahora mockeo la llamada al servicio
                Intent intent = new Intent(FAVORITE_SEARCH_EVENT);
                intent.putExtra("data", new Parcelable[]{});
                LocalBroadcastManager.getInstance(InitActivity.this).sendBroadcast(intent);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchByAddressDiaglo();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver,
                new IntentFilter(LOCATION_SEARCH_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(favoriteReceiver,
                new IntentFilter(FAVORITE_SEARCH_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(searchReceiver,
                new IntentFilter(SEARCH_SEARCH_EVENT));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(favoriteReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(searchReceiver);

        marketService.destroy();

        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_init, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEmptyDialog(int messageId) {
        new AlertDialog.Builder(InitActivity.this)
                .setMessage(messageId)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(InitActivity.this, "Sorry, not implemented yet :(", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void showSearchByAddressDiaglo() {
        new AlertDialog.Builder(InitActivity.this)
                .setTitle(R.string.search_by_search_title)
                .setMessage(R.string.search_by_address_explanation)
                .setView(R.layout.address_search_dialog)
                .setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //TODO Como obtengo los valores de los cuadros de texto?
                        marketService.searchByAddress("Bla", "Ble");
                    }
                })
                .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private final BroadcastReceiver locationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            showEmptyDialog(R.string.no_market_location);
        }
    };

    private final BroadcastReceiver searchReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO ver que me viene jeje
            showEmptyDialog(R.string.no_market_location);
        }
    };

    private final BroadcastReceiver favoriteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            showEmptyDialog(R.string.no_market_favorite);
        }
    };
}
