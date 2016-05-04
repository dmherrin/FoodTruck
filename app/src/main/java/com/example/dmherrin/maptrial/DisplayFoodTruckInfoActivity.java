package com.example.dmherrin.maptrial;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        truckName.setText(name);

        TextView truckAddress = (TextView)findViewById(R.id.address);
        truckAddress.setText(address);

        TextView truckPage = (TextView)findViewById(R.id.yelp_page);
        truckPage.setText(yelpPage);

        TextView truckPhone = (TextView)findViewById(R.id.phone_number);
        truckPhone.setText(phoneNumber);

        TextView truckMenu = (TextView)findViewById(R.id.menu);
        truckMenu.setText(menu);

        ImageView image1 = (ImageView)findViewById(R.id.image1);
//        ImageView image2 = (ImageView)findViewById(R.id.image2);
//        ImageView image3 = (ImageView)findViewById(R.id.image3);

        if(name.equals("Baller")){

            image1.setImageResource(R.drawable.baller1);
//            image2.setImageResource(R.drawable.baller2);
        }
        else if(name.equals("Nomad's Natural Plate")){

            image1.setImageResource(R.drawable.nomad1);
//            image2.setImageResource(R.drawable.nomad2);
        }
        else if(name.equals("Off The Rail BBQ")){

            image1.setImageResource(R.drawable.offtherail1);
        }
        else if(name.equals("Burton's Comfort Creamery")){

//            image1.setImageResource(R.drawable.burton1);
//            image2.setImageResource(R.drawable.burton2);
//            image3.setImageResource(R.drawable.burton3);
        }
        else if(name.equals("Natural State Sandwiches")){

            image1.setImageResource(R.drawable.nstate1);
//            image2.setImageResource(R.drawable.nstate2);
        }
        else if(name.equals("Greenhouse Grill Food Cart")){

        }


    }
    public void onBack(View v){

        Intent intent = new Intent(DisplayFoodTruckInfoActivity.this,MapsActivity.class);
        setResult(3,intent);
        finish();
    }

    public void onFavorite(View v) {

        Intent intent = new Intent(DisplayFoodTruckInfoActivity.this,MapsActivity.class);
        intent.putExtra("truckName",name);
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
