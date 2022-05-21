package com.example.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleMap.OnMarkerClickListener  {
    public GoogleMap googleMap;
    public Button profil_button;
    public Button search_button;
    public Button new_marker_button;
    public Button confitm_button;
    public Profil_fragment profil_fragment;
    public Search_fragment search_fragment;
    public New_Marker_fragment new_marker_fragment;
    public Marker_Info_fragment marker_info_fragment;
    public static HashMap<Float,EventMarker> mapOfMarkers;
    public Animation animation;
    public Fragment fragment;


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
        confitm_button = findViewById(R.id.mainComfirmbtn);
        confitm_button.setOnClickListener(this);
        profil_fragment = new Profil_fragment();
        search_fragment = new Search_fragment();
        new_marker_fragment = new New_Marker_fragment();
        marker_info_fragment = new Marker_Info_fragment();
        mapOfMarkers = new HashMap<>();
        fragment = new Fragment();
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
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
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


            case R.id.mainComfirmbtn:
                LatLng position = googleMap.getCameraPosition().target;
                confitm_button.setVisibility(View.INVISIBLE);
                fragmentTransaction.show(new_marker_fragment);
                new_marker_fragment.setAdress(position);

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

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        this.openMarkerInfo(this.mapOfMarkers.get(marker.getZIndex()));
        return false;
    }

    public void addMarker(EventMarker marker) {
        marker.addMarkertoMap(googleMap,mapOfMarkers);
    }

    class MyMapListener implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MainActivity.this.googleMap=googleMap;
            float a= 8f;
            for(int i = 0;i<200;i++){
                EventMarker marker = new EventMarker(MainActivity.this, i, 55.705199 + Math.random()/a*(Math.random()>0.5?1:-1),37.820906 + Math.random()/a*(Math.random()>0.5?1:-1.5),"Метка №" + i,i%6);
                marker.addMarkertoMap(googleMap,mapOfMarkers);

            }

            MarkerOptions mark = new MarkerOptions().position(new LatLng(55.705199, 37.820906)).rotation(15f).draggable(true).title("Туса")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).zIndex(45f);

             //googleMap.addMarker(mark);


            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(55.705199, 37.820906))
                    .zoom(18)
                    .bearing(0)
                    .tilt(45)
                    .build();

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

            googleMap.setOnMarkerClickListener(MainActivity.this);
        }


    }

    public void openMarkerInfo(EventMarker eventMarker){
        marker_info_fragment = new Marker_Info_fragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",Float.toString(eventMarker.id));
        marker_info_fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment place_holder_fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);
        fragmentTransaction.replace(R.id.place_holder_fragment,this.marker_info_fragment);
        fragmentTransaction.commit();
        setAllInvissible();
        marker_info_fragment.setMarkerInfo(eventMarker);
        Toast.makeText(this, eventMarker.category, Toast.LENGTH_SHORT).show();

    }

    public static HashMap<Float,EventMarker> getMapOfMarkers(){
        return new MainActivity().mapOfMarkers;
    }

    public void selectingPosition(){
        setAllInvissible();
        confitm_button.setVisibility(View.VISIBLE);
    }
}