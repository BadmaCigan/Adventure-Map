package com.example.maps.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;

import com.example.maps.MainActivity;
import com.example.maps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static final String[] CATEGORIES = new String[]{"Другое", "Спорт", "Музыка", "Развлечение", "Разговоры"};
    public static Locale locale = Locale.getDefault();


    public String category;
    public double latitude;
    public double longitude;
    public String description;
    public String title;
    public String address;
    public float hue;
    public float id;
    public long date;
    public int maxPeople;
    public int peopleNow;
    public int userId;


    public EventMarker(int id, double latitude, double longitude, String title, int category, int maxPeople, int userId) {
        this(id, latitude, longitude, title, "Событие тест - " + title + " по координатам : "
                + latitude + "  " + longitude, Calendar.getInstance().getTime().getTime(), category, maxPeople, userId);


    }

    public EventMarker(int id, double latitude, double longitude, String title, String description, long date, int category, int maxPeople, int userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.title = title;
        this.id = (float) id;
        this.date = date;
        this.address = " " + latitude + " " + longitude;
        this.maxPeople = maxPeople;
        this.peopleNow = 0;
        this.userId = userId;


        switch (category) {
            case CATEGORY_OTHER:
                this.category = CATEGORIES[category];
                this.hue = BitmapDescriptorFactory.HUE_MAGENTA;
                break;

            case CATEGORY_SPORT:
                this.category = CATEGORIES[category];
                this.hue = BitmapDescriptorFactory.HUE_AZURE;
                break;

            case CATEGORY_MUSIC:
                this.category = CATEGORIES[category];
                this.hue = BitmapDescriptorFactory.HUE_ROSE;
                break;

            case CATEGORY_FUN:
                this.category = CATEGORIES[category];
                this.hue = BitmapDescriptorFactory.HUE_YELLOW;
                break;

            case CATEGORY_TALKING:
                this.category = CATEGORIES[category];
                this.hue = BitmapDescriptorFactory.HUE_VIOLET;
                break;

            default:
                this.category = CATEGORIES[0];
                this.hue = BitmapDescriptorFactory.HUE_RED;
        }


    }

    public String getStringDate() {
        SimpleDateFormat pattern = new SimpleDateFormat("d MMMM y г.", locale);
        return pattern.format(new Date(this.date));

    }

    public static int getIntCategory(String category) {
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(category)) {
                return i;
            }
        }


        return 0;
    }

    public MarkerOptions toMarkerOptions() {
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.mainActivity.getResources(), R.drawable.earn);
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .zIndex(this.id)
                .title(this.title)
                .zIndex(this.id);
    }

    public void addMarkertoMap(GoogleMap googleMap, HashMap<Float, EventMarker> hashMap) {
        googleMap.addMarker(this.toMarkerOptions());


        hashMap.put(this.id, this);

    }


    public void setAdress(Context context) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(this.latitude, this.longitude, 1);
            if (addresses.size() > 0) {
                this.address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "EventMarker{" +
                "category='" + category + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", hue=" + hue +
                ", id=" + id +
                ", date=" + date +
                ", maxPeople=" + maxPeople +
                ", peopleNow=" + peopleNow +
                ", userId=" + userId +
                '}';
    }
}
