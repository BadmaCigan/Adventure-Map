package com.example.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
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
import com.google.gson.JsonElement;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleMap.OnMarkerClickListener {
    public GoogleMap googleMap;
    public Fragment googleMapFragment;
    public Button profil_button;
    public Button search_button;
    public Button new_marker_button;
    public Button confitm_button;
    public Profil_fragment profil_fragment;
    public Search_fragment search_fragment;
    public New_Marker_fragment new_marker_fragment;
    public Marker_Info_fragment marker_info_fragment;
    public static HashMap<Float, EventMarker> mapOfMarkers;
    public Animation animation;
    public Fragment fragment;
    public Retrofit retrofit;
    public Retrofit vk_retrofit;
    public UserService serv;
    public VK_Service vk_service;
    public SharedPreferences sharedPreferences;
    public String vkAccessToken;
    public String vk_version = "5.131";
    public User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPreference = sharedPreferences.edit();
        setContentView(R.layout.activity_main);
        profil_button = findViewById(R.id.profil_button);
        profil_button.setOnClickListener(this);
        search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(this);
        new_marker_button = findViewById(R.id.new_marker_button);
        new_marker_button.setOnClickListener(this);
        confitm_button = findViewById(R.id.mainComfirmbtn);
        confitm_button.setOnClickListener(this);
        googleMapFragment = getFragmentManager().findFragmentById(R.id.mapView);
        profil_fragment = new Profil_fragment();
        search_fragment = new Search_fragment();
        new_marker_fragment = new New_Marker_fragment();
        marker_info_fragment = new Marker_Info_fragment();
        mapOfMarkers = new HashMap<>();
        fragment = new Fragment();
        createMapView();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serv = retrofit.create(UserService.class);

        vk_retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        vk_service = vk_retrofit.create(VK_Service.class);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.place_holder_fragment, new Upper_fragment());
        fragmentTransaction.commit();
        setAllInvissible();
        ArrayList<VKScope> list = new ArrayList<>();
        list.add(VKScope.WALL);
        list.add(VKScope.PHOTOS);
        VK.login(this, list);


    }


    public void setAllInvissible() {
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

        switch (view.getId()) {

            case R.id.search_button:
                fragmentTransaction.replace(R.id.place_holder_fragment, search_fragment);

                fragmentTransaction.show(search_fragment);

                setAllInvissible();


                break;

            case R.id.profil_button:
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

    public void setAllVisible() {
        profil_button.setVisibility(View.VISIBLE);
        search_button.setVisibility(View.VISIBLE);
        new_marker_button.setVisibility(View.VISIBLE);
    }


    private void createMapView() {

        try {
            if (null == googleMap) {
                MyMapListener listener = new MyMapListener();
                ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMapAsync(listener);


            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        this.openMarkerInfo(this.mapOfMarkers.get(marker.getZIndex()));
        return false;
    }

    public void addMarker(EventMarker marker) {
        marker.addMarkertoMap(googleMap, mapOfMarkers);
    }

    class MyMapListener implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MainActivity.this.googleMap = googleMap;
            float a = 8f;
            for (int i = 0; i < 200; i++) {
                EventMarker marker = new EventMarker(i, 55.705199 + Math.random() / a * (Math.random() > 0.5 ? 1 : -1), 37.820906 + Math.random() / a * (Math.random() > 0.5 ? 1 : -1.5), "Метка №" + i, i % 6, 20);
                marker.addMarkertoMap(googleMap, mapOfMarkers);

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

    public void openMarkerInfo(EventMarker eventMarker) {
        marker_info_fragment = new Marker_Info_fragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", Float.toString(eventMarker.id));
        marker_info_fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment place_holder_fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);
        fragmentTransaction.replace(R.id.place_holder_fragment, this.marker_info_fragment);
        fragmentTransaction.commit();
        setAllInvissible();
        marker_info_fragment.setMarkerInfo(eventMarker);
        Toast.makeText(this, eventMarker.category, Toast.LENGTH_SHORT).show();

    }

    public static HashMap<Float, EventMarker> getMapOfMarkers() {
        return new MainActivity().mapOfMarkers;
    }

    public void selectingPosition() {
        setAllInvissible();
        confitm_button.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NonNull VKAccessToken vkAccessToken) {
                Toast.makeText(MainActivity.this, "Вход выполнен успешно", Toast.LENGTH_LONG).show();
                Log.e(null, vkAccessToken.getAccessToken());
                ((MainActivity) MainActivity.this).vkAccessToken = vkAccessToken.getAccessToken();
                Call<JsonElement> call = vk_service.getUserInfo(MainActivity.this.vkAccessToken, vk_version);
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject resp = jsonObject.getJSONObject("response");
                            int id = Integer.parseInt(resp.get("id").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {


                    }
                });

            }

            @Override
            public void onLoginFailed(int i) {
                Toast.makeText(MainActivity.this, "Не получилось", Toast.LENGTH_SHORT).show();
            }
        };
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(MainActivity.this, "gg", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isUserRegistrated(int id) {
        Call<Boolean> call = serv.isUserRegistrated(id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    getUserById(id);
                } else {
                    registrateUserById(id);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

        return true;
    }

    private void registrateUserById(int id) {

    }

    public void getUserById(int id) {
        Call<User> call = serv.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("maps", response.body().toString());
                MainActivity.this.user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}