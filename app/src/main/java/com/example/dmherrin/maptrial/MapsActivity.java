package com.example.dmherrin.maptrial;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

import static com.example.dmherrin.maptrial.FavoriteDatabaseContract.QuakeColumns.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,OnMarkerClickListener,OnInfoWindowClickListener {

    public static final String TAG = "foodtruck";
    public static final String PROFILE = "dmherrin";
    public static final String MAG_KEY = "truck";


    private Handler handler = new Handler();

    ArrayList<FoodTruck> foodtruckArrayList;
    ArrayAdapter<FoodTruck> foodtruckArrayAdapter;

    Boolean isFavorite = false;
    private GoogleMap mMap;
    private FavoriteSQLiteOpenHelper favoriteHelper;
    private SQLiteDatabase favoriteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Spinner creates drop-down listview-type menu that can be accessed by clicking next to the app name
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorUltraLight), PorterDuff.Mode.SRC_ATOP);
        //Spinner is populated from list of foodtrucks that can be found in res->values->foodtrucks.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.foodtrucks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        foodtruckArrayList = new ArrayList<>();
        foodtruckArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, foodtruckArrayList);

        //ListView earthquakeListView = (ListView)findViewById(R.id.quakeListView);
        //earthquakeListView.setAdapter(earthquakeArrayAdapter);

        favoriteHelper = new FavoriteSQLiteOpenHelper(this);
        favoriteDB = favoriteHelper.getWritableDatabase();

        Log.i(TAG, "in onDownloadFoodtrucks");


/*
        Runnable r = new Runnable() {
            @Override
            public void run() {
                download();
            }
        };
        Thread t = new Thread(r);
        t.start();*/

        //SharedPreferences sp = getSharedPreferences(PROFILE, Activity.MODE_PRIVATE);
        //minMagnitude = sp.getFloat(MAG_KEY, 0f);

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
        //makes the map start where it is zoomed in on Fayetteville city limits
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.0764, -94.1608), 12.5f));

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
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"Before for loop");
        for (int i = 0; i < foodtruckArrayList.size(); i++) {
            Log.i(TAG,"In for loop");
            String address = foodtruckArrayList.get(i).getLocation();
            String title1 = foodtruckArrayList.get(i).getTruck();
            LatLng location = getLocationFromAddress(this, address);
            if(searchByTitle(title1)){
                mMap.addMarker(new MarkerOptions().position(location)
                    .title(title1)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
            else {
                mMap.addMarker(new MarkerOptions().position(location)
                        .title(title1)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
            }
            Log.i(TAG, title1);
            Log.i(TAG, address);


        }
        // Hard codes the position for the following locations
        /*LatLng YachtClub = new LatLng(36.071926, -94.157825);
        LatLng Baller = new LatLng(36.120140, -94.150971);
        LatLng Nomad = new LatLng(36.066036, -94.161964);
        LatLng NaturalState = new LatLng(36.077075, -94.168486);
        LatLng Burton = new LatLng(36.066425, -94.163760);
        LatLng Greenhouse = new LatLng(36.057590, -94.165408);
        LatLng KindKitchen = new LatLng(36.371750, -94.210529);
        LatLng OffTheRails = new LatLng(36.061613, -94.160861);
        mMap.addMarker(new MarkerOptions().position(YachtClub).title("The Yacht Club").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
        mMap.addMarker(new MarkerOptions().position(Baller).title("Baller").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
        mMap.addMarker(new MarkerOptions().position(Nomad).title("Nomad's Natural Plate").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
        mMap.addMarker(new MarkerOptions().position(NaturalState).title("Natural State Sandwiches").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
        mMap.addMarker(new MarkerOptions().position(Burton).title("Burton's Comfort Creamery").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
        mMap.addMarker(new MarkerOptions().position(Greenhouse).title("Greenhouse Grill Food Cart").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
        mMap.addMarker(new MarkerOptions().position(KindKitchen).title("Kind Kitchen").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
        mMap.addMarker(new MarkerOptions().position(OffTheRails).title("Off the Rails BBQ").icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));
*/


    }

    public void download() {

        Log.i(TAG, "in download");

        try {
            URL url = new URL(getString(R.string.foodtrucks_url));
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            int responseCode = httpURLConnection.getResponseCode();
            final InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream));

            try {
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    Log.i(TAG, "HTTP OK");

                    String temp;
                    reader.readLine();
                    while((temp = reader.readLine()) != null) {
                        String[] data = temp.split(",");
                        final String truck = data[0];
                        final String location  = data[1];


                        foodtruckArrayList.add(new FoodTruck
                                (truck, location));
                        //mMap.addMarker(new MarkerOptions().position(truckLocation).title(truck).icon(BitmapDescriptorFactory.fromResource(R.drawable.mk2pin48)));

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            foodtruckArrayAdapter.notifyDataSetChanged();
                            Log.i(TAG, foodtruckArrayList.toString());
                        }
                    });
                }
            }
            finally {
                ((HttpURLConnection) connection).disconnect();
                inputStream.close();
                reader.close();
            }
        }
        catch (MalformedURLException e) {
            Log.i(TAG, "MalformedURLException.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public LatLng getLocationFromAddress(Context context,String strAddress) {

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

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

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
        Intent intent = new Intent(MapsActivity.this,DisplayFoodTruckInfoActivity.class);
        for(int i = 0; i < foodtruckArrayList.size(); i++){
            tempTruck = foodtruckArrayList.get(i);
            if(tempTruck.getTruck().equals(marker.getTitle())){
                intent.putExtra("truckName",tempTruck.getTruck());
                intent.putExtra("location",tempTruck.getLocation());
                isFavorite = searchByTitle(tempTruck.getTruck());
                intent.putExtra("isFavorite",isFavorite);
                if(tempTruck.getTruck().equals("Baller")){
                    intent.putExtra("yelpPage", "http://www.yelp.com/biz/baller-food-truck-fayetteville");
                    intent.putExtra("phoneNumber", "(479) 619-6830");
                    intent.putExtra("menu", "");
                }
                else if(tempTruck.getTruck().equals("Nomad's Natural Plate")){
                    intent.putExtra("yelpPage", "http://www.yelp.com/biz/nomads-natural-plate-fayetteville");
                    intent.putExtra("phoneNumber", "(479) 435-5312");
                    intent.putExtra("menu", "");
                }
                else if(tempTruck.getTruck().equals("Off The Rails BBQ")){
                    intent.putExtra("yelpPage", "https://www.zomato.com/northwest-arkansas/off-the-rail-bbq-fayetteville");
                    intent.putExtra("phoneNumber", "(479) 856-4341");
                    intent.putExtra("menu", "");
                }
                else if(tempTruck.getTruck().equals("Burton's Comfort Creamery")){
                    intent.putExtra("yelpPage", "http://www.yelp.com/biz/burtons-comfort-creamery-fayetteville");
                    intent.putExtra("phoneNumber", "");
                    intent.putExtra("menu", "http://www.burtonscreamery.com/");
                }
                else if(tempTruck.getTruck().equals("Natural State Sandwiches")){
                    intent.putExtra("yelpPage", "http://www.yelp.com/biz/natural-state-sandwiches-fayetteville");
                    intent.putExtra("phoneNumber", "(479) 225-1103");
                    intent.putExtra("menu", "http://www.naturalstatesandwiches.com/#!menu/c1aeq");
                }
                else if(tempTruck.getTruck().equals("Greenhouse Grill Food Cart")){
                    intent.putExtra("yelpPage", "https://www.zomato.com/northwest-arkansas/greenhouse-grille-food-cart-fayetteville");
                    intent.putExtra("phoneNumber", "(479) 444-8909");
                    intent.putExtra("menu", "");
                }
            }
        }
        startActivityForResult(intent,requestCode);
    }
    private Boolean searchByTitle(String title){
        String[] projection = {COLUMN_FAVORITE};
        String selection = COLUMN_FAVORITE + " =?";
        String[] selectionArgs = {title};

        Cursor cursor = favoriteDB.query(TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String favorite;
                favorite = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE));
                if(favorite == title){
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
        Intent intent = getIntent();
        String truckName = intent.getStringExtra("truckName");
        if(requestCode == 1){
            if(resultCode == 1){
                favoriteHelper.insert(favoriteDB, truckName);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    this.recreate();
                }
                else{
                    finish();
                    startActivity(intent);
                }
            }
            else if(resultCode == 2){
                favoriteHelper.remove(favoriteDB, truckName);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    this.recreate();
                }
                else{
                    finish();
                    startActivity(intent);
                }
            }
            else if(resultCode == 3){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    this.recreate();
                }
                else{
                    finish();
                    startActivity(intent);
                }
            }
        }
    }
}
