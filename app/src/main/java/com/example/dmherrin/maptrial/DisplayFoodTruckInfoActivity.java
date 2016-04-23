package com.example.dmherrin.maptrial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayFoodTruckInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food_truck_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra("truckName");
        String address = intent.getStringExtra("location");
        String yelpPage = intent.getStringExtra("yelpPage");
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String menu = intent.getStringExtra("menu");

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

    }
    public void onBack(View v){
        finish();
    }
}
