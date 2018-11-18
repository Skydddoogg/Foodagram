package com.example.foodagramapp.foodagram.Feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getIntent().getExtras();
        Post post = bundle.getParcelable("post");
        String placeName = post.getPlaceName();
        Double lat = Double.parseDouble(post.getLatitude());
        Double lng = Double.parseDouble(post.getLongitude());
        LatLng location = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(placeName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}
