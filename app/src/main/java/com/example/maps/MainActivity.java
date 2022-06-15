package com.example.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.example.maps.entity.EventMarker;
import com.example.maps.entity.Filters;
import com.example.maps.entity.User;
import com.example.maps.fragments.LayersFragment;
import com.example.maps.fragments.Marker_Info_fragment;
import com.example.maps.fragments.New_Marker_fragment;
import com.example.maps.fragments.Profil_fragment;
import com.example.maps.fragments.Registration_fragment;
import com.example.maps.fragments.Search_fragment;
import com.example.maps.fragments.Upper_fragment;
import com.example.maps.services.UserService;
import com.example.maps.services.VK_Service;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleMap.OnMarkerClickListener, Toolbar.OnMenuItemClickListener {
    private static final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 1234;
    public GoogleMap googleMap;
    public Fragment googleMapFragment;
    public Button profil_button;
    public Button search_button;
    public Button new_marker_button;
    public Button confitm_button;
    public Upper_fragment titleFragment;
    public Profil_fragment profil_fragment;
    public Search_fragment search_fragment;
    public New_Marker_fragment new_marker_fragment;
    public Marker_Info_fragment marker_info_fragment;
    public LayersFragment layersFragment;
    public static HashMap<Float, EventMarker> mapOfMarkers;
    public Animation animation;
    public Fragment fragment;
    public Retrofit retrofit;
    public Retrofit vk_retrofit;
    public UserService serv;
    public VK_Service vk_service;
    public SharedPreferences userPreferences;
    public String vkAccessToken;
    public String vk_version = "5.131";
    public User user;
    public boolean flag;
    public static MainActivity mainActivity;
    public CoordinatorLayout coordinatorLayout;
    public BottomAppBar bottomAppBar;
    public FloatingActionButton floatingActionButton;
    public boolean firsLaunchFlag = true;
    public ArrayList<String> tags;
    public TreeSet<String> filteredTags;
    public MapThread mapThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;

        super.onCreate(savedInstanceState);
        titleFragment = new Upper_fragment();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        profil_button = findViewById(R.id.profil_button);
        profil_button.setOnClickListener(this);
        search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(this);
        new_marker_button = findViewById(R.id.new_marker_button);
        new_marker_button.setOnClickListener(this);
        confitm_button = findViewById(R.id.mainComfirmbtn);
        coordinatorLayout = findViewById(R.id.coordinator);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomAppBar.setOnMenuItemClickListener(this);
        confitm_button.setOnClickListener(this);
        googleMapFragment = getFragmentManager().findFragmentById(R.id.mapView);
        profil_fragment = new Profil_fragment();
        search_fragment = new Search_fragment();
        new_marker_fragment = new New_Marker_fragment();
        marker_info_fragment = new Marker_Info_fragment();
        mapOfMarkers = new HashMap<>();
        fragment = new Fragment();
        layersFragment = new LayersFragment();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://fathomless-coast-14243.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serv = retrofit.create(UserService.class);
        initial();
        createMapView();
        vk_retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        vk_service = vk_retrofit.create(VK_Service.class);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.place_holder_fragment, titleFragment);
        fragmentTransaction.commit();
//setAllInvissible();
        bottomAppBar.setVisibility(View.INVISIBLE);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        registrtion();
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        floatingActionButton.setColorFilter(Color.argb(255, 255, 255, 255));
        //getFragmentManager().beginTransaction().addToBackStack("root").commit();
        //getFragmentManager().beginTransaction().addToBackStack("first").commit();




    }


    public void initial() {
        serv.getTags().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                tags = response.body();
                filteredTags = new TreeSet<>((ArrayList<String>) tags.clone());
                updateMarkers();
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment place_holder_fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);
        fragmentTransaction.addToBackStack("from main");

        switch (view.getId()) {

            case R.id.search_button:
                fragmentTransaction.replace(R.id.place_holder_fragment, search_fragment);
                fragmentTransaction.show(search_fragment);
                search_fragment.update();


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

            case R.id.fab:

                fragmentTransaction.replace(R.id.place_holder_fragment, new_marker_fragment);

                fragmentTransaction.show(new_marker_fragment);

                setAllInvissible();

                break;
        }
        fragmentTransaction.commit();


    }

    //Работа с Меню

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment place_holder_fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);


        switch (item.getItemId()) {
            case R.id.app_bar_profile:

                fragmentTransaction.replace(R.id.place_holder_fragment, profil_fragment);
                fragmentTransaction.addToBackStack("toProfile");
                fragmentTransaction.show(profil_fragment);
                setAllInvissible();

                break;

            case R.id.app_bar_search:
                fragmentTransaction.replace(R.id.place_holder_fragment, search_fragment);
                fragmentTransaction.addToBackStack("toSearch");
                fragmentTransaction.show(search_fragment);
                search_fragment.update();
                setAllInvissible();

                break;


            case R.id.app_bar_layers:
                fragmentTransaction.replace(R.id.place_holder_fragment, new LayersFragment());
                fragmentTransaction.addToBackStack("toLayers");
                setAllInvissible();

                break;
            default:
                Toast.makeText(this, "hz", Toast.LENGTH_SHORT).show();
                break;
        }
        fragmentTransaction.commit();
        return true;


    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            openQuitDialog();
        } else {
            String lastCommit = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            if (lastCommit.equals("toLayers")) {
                fragmentManager.popBackStack();
                updateMarkers();
                setAllVisible();
            } else if (lastCommit.equals("toChat")) {
                fragmentManager.popBackStack();
            } else {
                fragmentManager.popBackStack();
                setAllVisible();
            }
        }
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainActivity.this);
        quitDialog.setTitle("Выход: Вы уверены?");

        quitDialog.setPositiveButton("Таки да!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        quitDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottomappbar_menu, menu);


        return true;
    }

    public void setAllInvissible() {
        bottomAppBar.setVisibility(View.INVISIBLE);
        floatingActionButton.hide();
    }

    public void setAllVisible() {
        bottomAppBar.setVisibility(View.VISIBLE);
        floatingActionButton.show();
    }


    private void createMapView() {

        try {
            MyMapListener listener = new MyMapListener();
            ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapView)).getMapAsync(listener);


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

    public void goToMark(EventMarker eventMarker) {
        CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(eventMarker.latitude, eventMarker.longitude));
        getFragmentManager().beginTransaction().replace(R.id.place_holder_fragment, new Fragment()).commit();
        googleMap.animateCamera(update);
        setAllVisible();
    }


    class MyMapListener implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MainActivity.this.googleMap = googleMap;
            float a = 8f;
            //for (int i = 0; i < 200; i++) {
            //    EventMarker marker = new EventMarker(i, 55.705199 + Math.random() / a * (Math.random() > 0.5 ? 1 : -1), 37.820906 + Math.random() / a * (Math.random() > 0.5 ? 1 : -1.5), "Метка №" + i, i % 6, 20, 24 + i);
            //    marker.addMarkertoMap(googleMap, mapOfMarkers);
            //}
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.earn);
            //MarkerOptions mark = new MarkerOptions().position(new LatLng(55.705199, 37.820906)).rotation(0f).draggable(true).title("Туса")
            //        .icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            //googleMap.addMarker(mark);
            updateMarkers();
            if (mapThread != null) {
                mapThread.finish();
            }
            mapThread = new MapThread();
            mapThread.start();
            //updateMarkers();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);


            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION_FINE_LOCATION);
            }

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            //googleMap.getUiSettings().setZoomControlsEnabled(true);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);

                } else {
                    // permission denied
                }
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        fragmentTransaction.addToBackStack("toMarkerInfo");
        fragmentTransaction.commit();
        //setAllInvissible();
        setAllInvissible();
        marker_info_fragment.setMarkerInfo(eventMarker);
        Toast.makeText(this, eventMarker.category, Toast.LENGTH_SHORT).show();

    }

    public static HashMap<Float, EventMarker> getMapOfMarkers() {
        return new MainActivity().mapOfMarkers;
    }

    public void selectingPosition() {
        //setAllInvissible();
        confitm_button.setVisibility(View.VISIBLE);
    }

    public void registrtion() {
        userPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (userPreferences.contains("isRegistrated")) {

        } else {
            ArrayList<VKScope> list = new ArrayList<>();
            list.add(VKScope.WALL);
            list.add(VKScope.PHOTOS);

            VK.login(this, list);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.place_holder_fragment,new Registration_fragment());
        //fragmentTransaction.commit();
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NonNull VKAccessToken vkAccessToken) {
                setAllVisible();
                Log.e("gg", vkAccessToken.getAccessToken());
                ((MainActivity) MainActivity.this).vkAccessToken = vkAccessToken.getAccessToken();
                Log.i("am", "" + vkAccessToken.getUserId());

                Call<JsonElement> call = vk_service.getUserInfo(MainActivity.this.vkAccessToken, vk_version);
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject resp = jsonObject.getJSONObject("response");
                            int id = Integer.parseInt(resp.get("id").toString());
                            checkAcc(id);


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


    public void checkAcc(int id) {
        Call<Boolean> call = serv.isUserRegistrated(id);


        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                flag = response.body();
                if (flag) {
                    getUserById(id);
                    getFragmentManager().beginTransaction().replace(R.id.place_holder_fragment, fragment).commit();
                    //setAllVisible();
                } else {
                    Registration_fragment frag = new Registration_fragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    frag.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.place_holder_fragment, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }


    public void getUserById(int id) {
        Call<User> userCall = serv.getUserById(id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                MainActivity.this.user = response.body();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    public void registrateUser(User user) {
        String strungUser = new Gson().toJson(user);
        Call<Void> call = serv.registrateUser(strungUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.place_holder_fragment, new Fragment()).commit();
                setAllVisible();
                MainActivity.this.user = user;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Регистрация прервана", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String gsonUser = gson.toJson(user, User.class);
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", gsonUser);
        editor.putBoolean("isRegistrated", true);
    }

    public void updateMarkers() {
        if (filteredTags != null) {
            ArrayList<String> filters = new ArrayList<>();
            for (String fltr : filteredTags) {
                filters.add(fltr);
            }

            Log.e("gg", new Gson().toJson(new Filters(filters)));

            serv.getEventsByFilter(new Gson().toJson(new Filters(filters))).enqueue(new Callback<ArrayList<EventMarker>>() {
                @Override
                public void onResponse(Call<ArrayList<EventMarker>> call, Response<ArrayList<EventMarker>> response) {
                    mapOfMarkers = new HashMap<>();
                    if (response.body() != null) {
                        for (EventMarker marker :
                                response.body()) {
                            mapOfMarkers.put(marker.id, marker);
                        }
                    }
                    googleMap.clear();
                    if (mapOfMarkers != null) {
                        for (float id : mapOfMarkers.keySet()) {
                            addMarker(mapOfMarkers.get(id));
                        }
                    }

                }


                @Override
                public void onFailure(Call<ArrayList<EventMarker>> call, Throwable t) {

                }
            });
        }



        /*Call<ArrayList<EventMarker>> call = serv.getMarkers();
        call.enqueue(new Callback<ArrayList<EventMarker>>() {
            @Override
            public void onResponse(Call<ArrayList<EventMarker>> call, Response<ArrayList<EventMarker>> response) {

                Log.e("thread", "ok");
                ArrayList<EventMarker> markers = response.body();
                if (markers != null) {
                    for (EventMarker marker :
                            markers) {
                        if (!mapOfMarkers.containsKey(marker.id)) {
                            addMarker(marker);
                        }
                        mapOfMarkers.put(marker.id, marker);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EventMarker>> call, Throwable t) {
                Log.e("thread", "not ok");
            }
        });*/
    }

    public class MapThread extends Thread {
        @Override
        public synchronized void start() {
            super.start();
        }

        public volatile boolean isFinish = false;

        @Override
        public void run() {
            super.run();
            Log.e("Thread", "start");
            while (true && !isFinish) {
                MainActivity.mainActivity.updateMarkers();
                Log.e("Thread", "Поток update");
                try {
                    sleep(1000 * 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e("Thread", "stop");

        }

        public void finish()        //Инициирует завершение потока
        {
            isFinish = true;
        }
    }
}