package com.example.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.HashMap;

public class EventMarker {
    public static final int CATEGORY_OTHER = 0;
    public static final int CATEGORY_SPORT = 1;
    public static final int CATEGORY_MUSIC = 2;
    public static final int CATEGORY_FUN = 3;
    public static final int CATEGORY_TALKING = 4;


    String category;
    LatLng position;
    String description;
    String title;
    float hue;
    float id;

    public EventMarker(double latitude, double longitude, String title, int category){
        this(13,latitude,longitude,title,null,12l,category);

    }

    public EventMarker(int id, double latitude, double longitude, String title, String description, long time,int category ){
        this.position = new LatLng(latitude, longitude);
        this.description = description;
        this.title = title;
        this.id = (float) id;



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
