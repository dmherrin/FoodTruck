package com.example.dmherrin.maptrial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayFoodTruckInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food_truck_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra("truckName");
        String address = intent.getStringExtra("location");
        TextView truckName = (TextView)findViewById(R.id.food_truck_name);
        truckName.setText(name);
        TextView truckAddress = (TextView)findViewById(R.id.address);
        truckAddress.setText(address);
    }
    public void onBack(View v){
        finish();
    }
}
