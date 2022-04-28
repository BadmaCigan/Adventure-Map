package com.example.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GoogleMap googleMap;
    Button profil_button;
    Button search_button;
    Button new_marker_button;
    Profil_fragment profil_fragment;
    Search_fragment search_fragment;
    New_Marker_fragment new_marker_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profil_button = findViewById(R.id.profil_button);
        profil_button.setOnClickListener(this);
        search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(this);
        new_marker_button = findViewById(R.id.new_marker_button);
        new_marker_button.setOnClickListener(this);
        profil_fragment = new Profil_fragment();
        search_fragment = new Search_fragment();
        new_marker_fragment = new New_Marker_fragment();

        createMapView();



    }



    public void setAllInvissible(){
        profil_button.setVisibility(View.INVISIBLE);
        search_button.setVisibility(View.INVISIBLE);
        new_marker_button.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment place_holder_fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);

        switch (view.getId()){

            case R.id.search_button:


                fragmentTransaction.replace(R.id.place_holder_fragment, search_fragment);

                fragmentTransaction.show(search_fragment);

                setAllInvissible();





                break;

            case R.id.profil_button:
                //fragmentTransaction.add(profil_fragment,null);

                fragmentTransaction.replace(R.id.place_holder_fragment, profil_fragment);

                fragmentTransaction.show(profil_fragment);

                setAllInvissible();


                break;

            case R.id.new_marker_button:

                fragmentTransaction.replace(R.id.place_holder_fragment, new_marker_fragment);

                fragmentTransaction.show(new_marker_fragment);

                setAllInvissible();

                break;



        }
        fragmentTransaction.commit();


    }

    public void setAllVisible(){
        profil_button.setVisibility(View.VISIBLE);
        search_button.setVisibility(View.VISIBLE);
        new_marker_button.setVisibility(View.VISIBLE);
    }



    private void createMapView(){

        try {
            if(null == googleMap){
                MyMapListener listener = new MyMapListener();
                ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMapAsync(listener);



            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    class MyMapListener implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MainActivity.this.googleMap=googleMap;

            MarkerOptions mark = new MarkerOptions().position(new LatLng(47.222531,39.718705)).rotation(90f).draggable(true).title("Туса");

            googleMap.addMarker(mark);

            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(47.222531,39.718705))
                    .zoom(18)
                    .bearing(0)
                    .tilt(45)
                    .build();

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
        }


    }
}