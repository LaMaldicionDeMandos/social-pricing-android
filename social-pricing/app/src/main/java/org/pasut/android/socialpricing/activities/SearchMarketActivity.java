package org.pasut.android.socialpricing.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;

import org.pasut.android.socialpricing.R;
import org.pasut.android.socialpricing.SocialPriceApplication;
import org.pasut.android.socialpricing.model.GeoLocation;
import org.pasut.android.socialpricing.model.Market;
import org.pasut.android.socialpricing.services.MarketService;
import org.pasut.android.socialpricing.services.PreferencesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;
import static org.pasut.android.socialpricing.services.MarketService.ARRIVE_MARKETS_EVENT;

public class SearchMarketActivity extends AppCompatActivity implements LocationListener {
    private final static String TAG = SearchMarketActivity.class.getSimpleName();
    private final static String FAVORITES = "favorites";
    private final static String FAVORITES_IDS = "favorites.";
    private final static int PERMISSION_LOCATION_REQUEST = 1556;

    private double lat;
    private double lon;
    private boolean hasLocation = false;

    private MarketService marketService;

    @Override
    public void onLocationChanged(Location location) {
        locationFound(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    interface CreateMarketStrategy {
        void execute(Context context);
    }

    class ManualCreateMarketStrategy implements CreateMarketStrategy {

        @Override
        public void execute(Context context) {
            showCreateManualMarketDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marketService = new MarketService(this);
        setContentView(R.layout.activity_search_market);
        prepareLocationService();
        View locationButton = findViewById(R.id.locationButton);
        View favoriteButton = findViewById(R.id.favoriteButton);
        View searchButton = findViewById(R.id.searchButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMarketByLocation();
            }
        });
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesService preferences = getPreferences();
                ArrayList<Market> markets = preferences.contain(FAVORITES)
                        ? (ArrayList<Market>) preferences.get(FAVORITES, new TypeToken<ArrayList<Market>>() {
                }.getType())
                        : new ArrayList<Market>();
                Intent intent = new Intent(ARRIVE_MARKETS_EVENT);
                intent.putParcelableArrayListExtra("data", markets);
                LocalBroadcastManager.getInstance(SearchMarketActivity.this).sendBroadcast(intent);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchByAddressDiaglo();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(searchReceiver,
                new IntentFilter(ARRIVE_MARKETS_EVENT));
    }

    private void prepareLocationService() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            Log.e(TAG, "Not permissions for geolocation");
            Toast.makeText(this, R.string.not_geo_permission, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST);
            } else {
                Intent intent = new Intent(ARRIVE_MARKETS_EVENT);
                intent.putParcelableArrayListExtra("data", new ArrayList<Market>());
                LocalBroadcastManager.getInstance(SearchMarketActivity.this).sendBroadcast(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_LOCATION_REQUEST) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            prepareLocationService();
        }
    }

    private void locationFound(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        hasLocation = true;
    }

    private void searchMarketByLocation() {
        if (hasLocation) {
            marketService.searchByLocation(lat, lon);
        } else {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            try {
                Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
                marketService.searchByLocation(location.getLatitude(), location.getLongitude());
            } catch (SecurityException e) {
                Log.e(TAG, "Not permissions for geolocation");
                Toast.makeText(this, R.string.not_geo_permission, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ARRIVE_MARKETS_EVENT);
                intent.putParcelableArrayListExtra("data", new ArrayList<Market>());
                LocalBroadcastManager.getInstance(SearchMarketActivity.this).sendBroadcast(intent);
            } catch(Exception e) {
                Log.e(TAG, "Not found location");
                Toast.makeText(this, R.string.not_found_location, Toast.LENGTH_SHORT).show();
            }
        }

        try {
        } catch (SecurityException e) {
            Log.e(TAG, "Not permissions for geolocation");
            Toast.makeText(this, R.string.not_geo_permission, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST);
            } else {
                Intent intent = new Intent(ARRIVE_MARKETS_EVENT);
                intent.putParcelableArrayListExtra("data", new ArrayList<Market>());
                LocalBroadcastManager.getInstance(SearchMarketActivity.this).sendBroadcast(intent);
            }
        }
    }

    private void startMarketActivity(Market market) {
        Intent intent = new Intent(this, MarketActivity.class);
        intent.putExtra(MarketActivity.MARKET, market);
        startActivity(intent);
    }

    private void showEmptyDialog(final int messageId, final CreateMarketStrategy strategy) {
        new AlertDialog.Builder(SearchMarketActivity.this)
                .setMessage(messageId)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        strategy.execute(SearchMarketActivity.this);
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
        final View dialogView = this.getLayoutInflater().inflate(R.layout.address_search_dialog, null);
        new AlertDialog.Builder(SearchMarketActivity.this)
                .setTitle(R.string.search_by_search_title)
                .setMessage(R.string.search_by_address_explanation)
                .setView(dialogView)
                .setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TextView address = (TextView) dialogView.findViewById(R.id.address);
                        TextView locale = (TextView) dialogView.findViewById(R.id.locale);
                        marketService.searchByAddress(address.getText().toString(), locale.getText().toString());
                    }
                })
                .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void showMarketSelectionDiaglo(final List<Market> markets) {
        ListAdapter adapter = new MarketArrayAdapter(SearchMarketActivity.this, R.layout.market_item
                , markets);
        new AlertDialog.Builder(SearchMarketActivity.this)
                .setTitle(R.string.market_select)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Market market = markets.get(which);
                        saveAsFavorite(market);
                        startMarketActivity(market);
                    }
                }).create().show();
    }

    private void showCreateManualMarketDialog() {
        final View dialogView = this.getLayoutInflater().inflate(R.layout.manual_market_creation_dialog, null);
        new AlertDialog.Builder(SearchMarketActivity.this)
                .setTitle(R.string.new_title)
                .setView(dialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String address = ((TextView) dialogView.findViewById(R.id.address)).getText()
                                .toString();
                        String name = ((TextView) dialogView.findViewById(R.id.name)).getText()
                                .toString();
                        String locale = ((TextView) dialogView.findViewById(R.id.locale)).getText()
                                .toString();
                        String completeAddress = address + ", " + locale;
                        Geocoder geocoder = new Geocoder(SearchMarketActivity.this);
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(completeAddress, 1);
                            if (addresses.isEmpty()) {
                                Toast.makeText(SearchMarketActivity.this, "Address not found",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Address addr = Iterables.getFirst(addresses, null);
                                GeoLocation geo = new GeoLocation(addr.getLatitude(), addr.getLongitude());
                                Market market = new Market(null, name, address, locale, geo);
                                marketService.save(market);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private PreferencesService getPreferences() {
        return ((SocialPriceApplication) getApplication()).getPreferenceService();
    }

    private void saveAsFavorite(Market market) {
        PreferencesService preferences = getPreferences();
        if (!preferences.contain(FAVORITES_IDS + market.getId())) {
            List<Market> favorites = preferences.get(FAVORITES, new TypeToken<ArrayList<Market>>() {
            }.getType());
            favorites = favorites != null ? favorites : new ArrayList<Market>();
            favorites.add(market);
            preferences.put(FAVORITES, favorites);
            preferences.put(FAVORITES_IDS + market.getId(), market);
        }
    }

    private final BroadcastReceiver searchReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<Market> markets = intent.getExtras().getParcelableArrayList("data");
            if (markets.isEmpty()) {
                showEmptyDialog(R.string.no_market_location, new ManualCreateMarketStrategy());
            } else if (markets.size() == 1) {
                Market market = Iterables.getFirst(markets, null);
                saveAsFavorite(market);
                startMarketActivity(market);
            } else {
                showMarketSelectionDiaglo(markets);
            }
        }
    };

    private class MarketArrayAdapter extends ArrayAdapter<Market> {
        int layoutResourceId;
        List<Market> data;

        public MarketArrayAdapter(Context context, int resource, List<Market> markets) {
            super(context, resource, markets);
            this.layoutResourceId = resource;
            this.data = markets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater)getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }
            Market market = data.get(position);
            TextView name = (TextView)convertView.findViewById(R.id.name);
            TextView address = (TextView)convertView.findViewById(R.id.address);
            name.setText(market.getName());
            address.setText(market.getAddress() + " - " + market.getLocale());
            return convertView;
        }
    }
}
