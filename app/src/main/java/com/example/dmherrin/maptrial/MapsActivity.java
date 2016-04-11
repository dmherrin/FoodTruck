package com.example.dmherrin.maptrial;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorUltraLight), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.foodtrucks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.0764, -94.1608), 12.5f));

        // Add a marker in Sydney and move the camera
        LatLng YachtClub = new LatLng(36.071926, -94.157825);
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


    }
}
