package com.example.maps.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maps.MainActivity;
import com.example.maps.R;
import com.example.maps.entity.EventMarker;
import com.example.maps.entity.Message;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Marker_Info_fragment extends Fragment implements View.OnClickListener {
    TextView title_of_event_tv;
    TextView event_description_tv;
    TextView event_date_tv;
    TextView event_address_tv;
    TextView event_category_tv;
    TextView event_count_of_people;
    com.google.android.material.textfield.TextInputEditText asd;
    EventMarker eventMarker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        View view = inflater.inflate(R.layout.marker_info_fragment, container, false);

        title_of_event_tv = view.findViewById(R.id.title_of_event_text_view);
        event_description_tv = view.findViewById(R.id.event_description_tv);
        event_date_tv = view.findViewById(R.id.event_date_text_view);
        event_address_tv = view.findViewById(R.id.event_address_text_view);
        event_category_tv = view.findViewById(R.id.event_category_text_view);
        event_count_of_people = view.findViewById(R.id.event_count_of_people_text_view);
        Bundle bundle = getArguments();
        float id = Float.parseFloat((String) bundle.get("id"));
        eventMarker = MainActivity.mapOfMarkers.get(id);
        title_of_event_tv.setText(eventMarker.title);
        event_description_tv.setText(eventMarker.description);
        event_date_tv.setText(eventMarker.getStringDate());
        eventMarker.setAdress(getActivity());
        event_address_tv.setText(eventMarker.address);
        event_category_tv.setText(eventMarker.category);
        event_count_of_people.setText(Integer.toString(eventMarker.peopleNow) + "/" + eventMarker.maxPeople + " людей");


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        getView().findViewById(R.id.plus_human_button).setOnClickListener(this);
        ImageButton cancel_button = (ImageButton) getView().findViewById(R.id.cancel_layers_button);
        cancel_button.setOnClickListener(this);
        getView().findViewById(R.id.openChatButton).setOnClickListener(this);


    }

    public void setAllVissible() {
        getActivity().findViewById(R.id.profil_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.new_marker_button).setVisibility(View.VISIBLE);

    }

    public void setMarkerInfo(EventMarker eventMarker) {


        //Toast.makeText(getActivity().getApplicationContext(), getView()==null?"YES":"NO", Toast.LENGTH_SHORT).show();
        //title_of_event_tv.setText("asda ");
        //event_description_tv.setText("asda ");
        //event_date_tv.setText("asda ");
        //event_address_tv.setText("asda ");
        //event_category_tv.setText("asda ");

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //при необходимости очистить поля

            case R.id.cancel_layers_button:
                MainActivity.mainActivity.onBackPressed();


                break;

            case R.id.plus_human_button:
                MainActivity.mainActivity.serv.isUserSubsribedOnEvent((int) eventMarker.id, MainActivity.mainActivity.user.getId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body()) {
                            Toast.makeText(MainActivity.mainActivity, "Вы уже зарегистрированы на это событие", Toast.LENGTH_SHORT).show();
                        } else {
                            registrateOnEvent();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
                break;

            case R.id.openChatButton:
                Toast.makeText(getActivity(), "Knopka", Toast.LENGTH_SHORT).show();
                MainActivity.mainActivity.serv.getEventMessages((int) eventMarker.id).enqueue(new Callback<ArrayList<Message>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                        Log.e("gg", "gg " + new Gson().toJson(response.body()));
                        Bundle args = new Bundle();
                        args.putString("messages",new Gson().toJson(response.body()));
                        args.putInt("eventId",(int) eventMarker.id);
                        ChatFragment chatFragment = new ChatFragment();
                        chatFragment.setArguments(args);
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.place_holder_fragment,chatFragment).addToBackStack("toChat").commit();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                        Log.e("gg",t.getMessage());
                    }
                });

                break;
                


        }

    }

    public void registrateOnEvent() {
        MainActivity.mainActivity.serv.registrationOnEvent((int) eventMarker.id, MainActivity.mainActivity.user.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(), "Вы успешно зарегистрированы на событие", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
