package org.pasut.android.socialpricing;

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

public class InitActivity extends AppCompatActivity {
    public final static String LOCATION_EVENT = "location_event";
    public final static String FAVORITE_EVENT = "favorite_event";
    public final static String SEARCH_EVENT = "search_event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        View locationButton = findViewById(R.id.locationButton);
        View favoriteButton = findViewById(R.id.favoriteButton);
        View searchButton = findViewById(R.id.searchButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Por ahora mockeo la llamada al servicio
                Intent intent = new Intent(LOCATION_EVENT);
                intent.putExtra("data", new Parcelable[]{});
                LocalBroadcastManager.getInstance(InitActivity.this).sendBroadcast(intent);
            }
        });
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Por ahora mockeo la llamada al servicio
                Intent intent = new Intent(FAVORITE_EVENT);
                intent.putExtra("data", new Parcelable[]{});
                LocalBroadcastManager.getInstance(InitActivity.this).sendBroadcast(intent);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Por ahora mockeo la llamada al servicio
                Intent intent = new Intent(SEARCH_EVENT);
                intent.putExtra("data", new Parcelable[]{});
                LocalBroadcastManager.getInstance(InitActivity.this).sendBroadcast(intent);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver,
                new IntentFilter(LOCATION_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(favoriteReceiver,
                new IntentFilter(FAVORITE_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(searchReceiver,
                new IntentFilter(SEARCH_EVENT));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(favoriteReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(searchReceiver);
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

    private final BroadcastReceiver locationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            showEmptyDialog(R.string.no_market_location);
        }
    };

    private final BroadcastReceiver searchReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            showSearchByAddressDiaglo();
        }
    };

    private final BroadcastReceiver favoriteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            showEmptyDialog(R.string.no_market_favorite);
        }
    };
}
