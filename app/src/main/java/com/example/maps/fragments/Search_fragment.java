package com.example.maps.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maps.MainActivity;
import com.example.maps.R;
import com.example.maps.adapters.RecyclerEventMarkerAdapter;
import com.example.maps.entity.EventMarker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Search_fragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerEventMarkerAdapter adapter;
    EditText search;
    ArrayList<EventMarker> markers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button cancel_button = (Button) getView().findViewById(R.id.cancel_search_button);
        cancel_button.setOnClickListener(this);
        recyclerView = getView().findViewById(R.id.markers);
        search = getView().findViewById(R.id.search);
        Iterator<Map.Entry<Float, EventMarker>> iterator = MainActivity.mainActivity.mapOfMarkers.entrySet().iterator();
        markers = new ArrayList<EventMarker>();
        while (iterator.hasNext()) {
            markers.add(iterator.next().getValue());
        }

        adapter = new RecyclerEventMarkerAdapter();
        adapter.setItems(markers);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i3, int i1, int i2) {
                String searchRequest = charSequence.toString();
                String regExpr = ".*";
                for (int i = 0; i < searchRequest.length(); i++) {
                    String symb = String.valueOf(searchRequest.charAt(i));
                    regExpr += "[" + symb.toLowerCase()+symb.toUpperCase() + "]" + ".*";
                }
                Pattern regPatt = Pattern.compile(regExpr);
                List<EventMarker> list = new ArrayList<>();
                for (EventMarker marker :
                        markers) {
                    if (regPatt.matcher(marker.title).find() || regPatt.matcher(marker.description).find()) {
                        list.add(marker);
                    }
                }
                adapter.setItems(list);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void setAllVissible() {
        getActivity().findViewById(R.id.profil_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.new_marker_button).setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //при необходимости очистить поля

            case R.id.cancel_search_button:
                getFragmentManager().popBackStack();


                break;


        }

    }

    public void update() {

    }
}
