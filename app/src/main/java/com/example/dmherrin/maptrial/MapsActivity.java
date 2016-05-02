package com.example.dmherrin.maptrial;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.dmherrin.maptrial.FavoriteDatabaseContract.QuakeColumns.COLUMN_FAVORITE;
import static com.example.dmherrin.maptrial.FavoriteDatabaseContract.QuakeColumns.TABLE_NAME;
import static com.example.dmherrin.maptrial.FavoriteDatabaseContract.QuakeColumns._ID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,OnMarkerClickListener,OnInfoWindowClickListener,AdapterView.OnItemSelectedListener {

    public static final String TAG = "foodtruck";
    public static final String PROFILE = "dmherrin";
    public static final String MAG_KEY = "truck";


    private Handler handler = new Handler();

    ArrayList<FoodTruck> foodtruckArrayList;
    ArrayAdapter<FoodTruck> foodtruckArrayAdapter;

    List<Marker> foodTruckMarkerList;

    Boolean isFavorite = false;
    private GoogleMap mMap;
    private FavoriteSQLiteOpenHelper favoriteHelper;
    private SQLiteDatabase favoriteDB;

    private FoodTruckSQLiteOpenHelper foodtruckHelper;
    private SQLiteDatabase foodtruckDB;
    String[] cols = {FoodTruckDatabaseContact.TruckColumns.COLUMN_TRUCK, FoodTruckDatabaseContact.TruckColumns.COLUMN_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Spinner creates drop-down listview-type menu that can be accessed by clicking next to the app name
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorUltraLight), PorterDuff.Mode.SRC_ATOP);
        //Spinner is populated from list of foodtrucks that can be found in res->values->foodtrucks.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.foodtrucks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        foodtruckArrayList = new ArrayList<>();
        foodtruckArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, foodtruckArrayList);

        foodTruckMarkerList = new ArrayList<>();

        favoriteHelper = new FavoriteSQLiteOpenHelper(this);
        favoriteDB = favoriteHelper.getWritableDatabase();

        foodtruckHelper = new FoodTruckSQLiteOpenHelper(this);
        foodtruckDB = foodtruckHelper.getWritableDatabase();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                download();
            }
        };
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteDB.close();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we recenter the camera to Fayetteville and place markers for a few of the food trucks.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        //makes the map start where it is zoomed in on Fayetteville city limits
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.0764, -94.1608), 12.5f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);


        //Step 2: Call function to put database contents in arraylist
        foodtruckArrayList = getMyFoodTrucks();

        //Step 4: Iterate through arraylist and addmarkers for each database object
        for (int i = 0; i < foodtruckArrayList.size(); i++) {

            String address = foodtruckArrayList.get(i).getLocation();
            String title1 = foodtruckArrayList.get(i).getTruck();
            LatLng location = getLocationFromAddress(this, address);
            Marker marker;

            if(title1.compareTo("baller") == 0)
            {
                title1 = "Baller";
            }
            else if(title1.compareTo("nomads") == 0)
            {
                title1 = "Nomad's Natural Plate";
            }
            else if(title1.compareTo("nstate") == 0)
            {
                title1 = "Natural State Sandwiches";
            }
            else if(title1.compareTo("burton") == 0)
            {
                title1 = "Burton's Comfort Creamery";
            }
            else if(title1.compareTo("greenh") == 0)
            {
                title1 = "Greenhouse Grill Food Cart";
            }
            else if(title1.compareTo("otrbbq") == 0)
            {
                title1 = "Off The Rails BBQ";
            }

            if (location != null) {
                if (searchByTitle(title1)) {
                    marker = mMap.addMarker(new MarkerOptions().position(location)
                            .title(title1)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48favorite)));
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(location)
                            .title(title1)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
                }
                foodTruckMarkerList.add(marker);
            }

        }

    }




    public void download() {


        try {
            URL url = new URL(getString(R.string.foodtrucks_url));
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            int responseCode = httpURLConnection.getResponseCode();
            final InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream));

            try {
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    String temp;
                    reader.readLine();
                    while ((temp = reader.readLine()) != null) {
                        String[] data = temp.split(",");

                        if (data.length == 2) {
                            final String truck = data[0];
                            final String location = data[1];

                            //Step 1: Add Truck, Location to FoodTruck Database
                            foodtruckHelper.remove(foodtruckDB, truck);
                            foodtruckHelper.insert(foodtruckDB, truck, location);
                        }

                    }
                }
            } finally {
                ((HttpURLConnection) connection).disconnect();
                inputStream.close();
                reader.close();
            }
        } catch (MalformedURLException e) {
            Log.i(TAG, "MalformedURLException.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int requestCode = 1;
        FoodTruck tempTruck;
        Intent intent = new Intent(MapsActivity.this, DisplayFoodTruckInfoActivity.class);
        for (int i = 0; i < foodtruckArrayList.size(); i++) {
            tempTruck = foodtruckArrayList.get(i);

            intent.putExtra("location", tempTruck.getLocation());
            isFavorite = searchByTitle(tempTruck.getTruck());
            intent.putExtra("isFavorite", isFavorite);
            if (tempTruck.getTruck().equals("baller")) {
                intent.putExtra("truckName","Baller");
                intent.putExtra("yelpPage", "http://www.yelp.com/biz/baller-food-truck-fayetteville");
                intent.putExtra("phoneNumber", "(479) 619-6830");
                intent.putExtra("menu", "http://imgur.com/H8GqL3V.jpg");
            } else if (tempTruck.getTruck().equals("nomads")) {
                intent.putExtra("truckName","Nomad's Natural Plate");
                intent.putExtra("yelpPage", "http://www.yelp.com/biz/nomads-natural-plate-fayetteville");
                intent.putExtra("phoneNumber", "(479) 435-5312");
                intent.putExtra("menu", "http://imgur.com/zL20Fsp.jpg");
            } else if (tempTruck.getTruck().equals("otrbbq")) {
                intent.putExtra("truckName","Off The Rail BBQ");
                intent.putExtra("yelpPage", "https://www.zomato.com/northwest-arkansas/off-the-rail-bbq-fayetteville");
                intent.putExtra("phoneNumber", "(479) 856-4341");
                intent.putExtra("menu", "N/A");
            } else if (tempTruck.getTruck().equals("burton")) {
                intent.putExtra("truckName","Burton's Comfort Creamery");
                intent.putExtra("yelpPage", "http://www.yelp.com/biz/burtons-comfort-creamery-fayetteville");
                intent.putExtra("phoneNumber", "");
                intent.putExtra("menu", "http://www.burtonscreamery.com/");
            } else if (tempTruck.getTruck().equals("nstate")) {
                intent.putExtra("truckName","Natural State Sandwiches");
                intent.putExtra("yelpPage", "http://www.yelp.com/biz/natural-state-sandwiches-fayetteville");
                intent.putExtra("phoneNumber", "(479) 225-1103");
                intent.putExtra("menu", "http://www.naturalstatesandwiches.com/#!menu/c1aeq");
            } else if (tempTruck.getTruck().equals("greenh")) {
                intent.putExtra("truckName","Greenhouse Grill Food Cart");
                intent.putExtra("yelpPage", "https://www.zomato.com/northwest-arkansas/greenhouse-grille-food-cart-fayetteville");
                intent.putExtra("phoneNumber", "(479) 444-8909");
                intent.putExtra("menu", "N/A");
            }

        }

        startActivityForResult(intent, requestCode);
    }

    private Boolean searchByTitle(String title) {
        String[] projection = {_ID, COLUMN_FAVORITE};
        String selection = COLUMN_FAVORITE + " =?";
        String[] selectionArgs = {title};

        Cursor cursor = favoriteDB.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String favorite;
                favorite = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE));
                if (favorite.equals(title)) {
                    return true;
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int resultFavorite = 1;
        int resultRemoveFavorite = 2;
        int resultBack = 3;

        String truckName = data.getStringExtra("truckName");
        if (requestCode == 1) {
            if (resultCode == resultFavorite) {
                Log.v(TAG, truckName);
                favoriteHelper.insert(favoriteDB, truckName);
            } else if (resultCode == resultRemoveFavorite) {
                favoriteHelper.remove(favoriteDB, truckName);
            } else if (resultCode == resultBack) {

            }
        }
    }
    //Step 3: Place database contents in arraylist
    public ArrayList<FoodTruck> getMyFoodTrucks(){
        ArrayList<FoodTruck> foodtrucks = new ArrayList<FoodTruck>();

        Cursor cursor = foodtruckDB.query(FoodTruckDatabaseContact.TruckColumns.TABLE_NAME, cols, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            FoodTruck f = cursorToFoodTruck(cursor);
            foodtrucks.add(f);
            cursor.moveToNext();
        }
        cursor.close();

        return foodtrucks;
    }

    private FoodTruck cursorToFoodTruck(Cursor cursor) {
        FoodTruck f = new FoodTruck();
        f.setTruck(cursor.getString(0));
        f.setLocation(cursor.getString(1));
        return f;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedFoodTruckName = parent.getItemAtPosition(position).toString();

        for(int i = 0; i < foodTruckMarkerList.size(); i++){

            Marker marker = foodTruckMarkerList.get(i);
            String title = marker.getTitle();

            if (selectedFoodTruckName.equals(title)) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12.5f));
                marker.showInfoWindow();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}