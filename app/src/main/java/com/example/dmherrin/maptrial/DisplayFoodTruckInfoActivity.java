package com.example.dmherrin.maptrial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayFoodTruckInfoActivity extends AppCompatActivity {

    public static final String TAG = "displayinfo";
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food_truck_info);

        Intent intent = getIntent();
        Boolean isFavorite = intent.getBooleanExtra("isFavorite",false);
        name = intent.getStringExtra("truckName");
        String address = intent.getStringExtra("location");
        String yelpPage = intent.getStringExtra("yelpPage");
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String menu = intent.getStringExtra("menu");

        if(isFavorite){
            Button removeFavoriteButton = (Button)findViewById(R.id.remove_favorite);
            removeFavoriteButton.setVisibility(View.VISIBLE);
            removeFavoriteButton.setClickable(true);
            removeFavoriteButton.setEnabled(true);
        }
        else{
            Button favoriteButton = (Button)findViewById(R.id.mark_favorite);
            favoriteButton.setVisibility(View.VISIBLE);
            favoriteButton.setClickable(true);
            favoriteButton.setEnabled(true);
        }
        TextView truckName = (TextView)findViewById(R.id.food_truck_name);
        //truckName.setText(name);
        if(name.compareTo("baller") == 0)
        {
            truckName.setText("Baller");
        }
        else if(name.compareTo("nomads") == 0)
        {
            truckName.setText("Nomad's Natural Plate");
        }
        else if(name.compareTo("nstate") == 0)
        {
            truckName.setText("Natural State Sandwiches");
        }
        else if(name.compareTo("burton") == 0)
        {
            truckName.setText("Burton's Comfort Creamery");
        }
        else if(name.compareTo("greenh") == 0)
        {
            truckName.setText("Greenhouse Grille Food Cart");
        }
        else if(name.compareTo("otrbbq") == 0)
        {
            truckName.setText("Off The Rail BBQ");
        }
        Log.v(TAG,name);

        TextView truckAddress = (TextView)findViewById(R.id.address);
        truckAddress.setText(address);

        TextView truckPage = (TextView)findViewById(R.id.yelp_page);
        truckPage.setText(yelpPage);

        TextView truckPhone = (TextView)findViewById(R.id.phone_number);
        truckPhone.setText(phoneNumber);

        TextView truckMenu = (TextView)findViewById(R.id.menu);
        truckMenu.setText(menu);

    }
    public void onBack(View v){

        Intent intent = new Intent(DisplayFoodTruckInfoActivity.this,MapsActivity.class);
        setResult(3,intent);
        finish();
    }

    public void onFavorite(View v) {

        Intent intent = new Intent(DisplayFoodTruckInfoActivity.this,MapsActivity.class);
        intent.putExtra("truckName",name);
        Log.v(TAG,name);
        setResult(1,intent);
        finish();
    }

    public void onRemoveFavorite(View v){

        Intent intent = new Intent(DisplayFoodTruckInfoActivity.this,MapsActivity.class);
        intent.putExtra("truckName",name);
        setResult(2,intent);
        finish();
    }
}
