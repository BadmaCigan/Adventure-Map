package com.example.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EventMarker {
    public static final int CATEGORY_OTHER = 0;
    public static final int CATEGORY_SPORT = 1;
    public static final int CATEGORY_MUSIC = 2;
    public static final int CATEGORY_FUN = 3;
    public static final int CATEGORY_TALKING = 4;


    public String category;
    public LatLng position;
    public String description;
    public String title;
    public String address;
    public float hue;
    public float id;
    public long date;


    public EventMarker(Context context, int id,double latitude, double longitude, String title, int category){
        this(context, id,latitude,longitude,title,"Событие тест - "+ title+" по координатам : "
                + latitude + "  " + longitude,new Date().getTime(),category);



    }

    public EventMarker(Context context, int id, double latitude, double longitude, String title, String description, long date, int category ){
        this.position = new LatLng(latitude, longitude);
        this.description = description;
        this.title = title;
        this.id = (float) id;
        this.date = date;
        this.address = " "+ latitude + " " + longitude;




        switch (category){
            case CATEGORY_OTHER:
                this.category = "Другое";
                this.hue = BitmapDescriptorFactory.HUE_MAGENTA;
                break;

            case CATEGORY_SPORT:
                this.category = "Спорт";
                this.hue = BitmapDescriptorFactory.HUE_AZURE;
                break;

            case CATEGORY_MUSIC:
                this.category = "Музка";
                this.hue = BitmapDescriptorFactory.HUE_ROSE;
                break;

            case CATEGORY_FUN:
                this.category = "Веселье";
                this.hue = BitmapDescriptorFactory.HUE_YELLOW;
                break;

            case CATEGORY_TALKING:
                this.category = "Разговоры";
                this.hue = BitmapDescriptorFactory.HUE_VIOLET;
                break;

            default:
                this.category = "Не определенно";
                this.hue = BitmapDescriptorFactory.HUE_RED;
        }


    }

    public MarkerOptions toMarkerOptions(){
        return new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(this.hue))
                .zIndex(this.id)
                .title(this.title)
                .zIndex(this.id);
    }

    public void addMarkertoMap(GoogleMap googleMap, HashMap<Float,EventMarker> hashMap){
        googleMap.addMarker(this.toMarkerOptions());





        hashMap.put(this.id,this);

    }




}
