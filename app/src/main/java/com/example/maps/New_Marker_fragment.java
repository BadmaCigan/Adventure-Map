package com.example.maps;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnimatorRes;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class New_Marker_fragment extends Fragment implements View.OnClickListener {
    Spinner spinner;
    TextView eventCategorytv;
    TextView enterDatetv;
    String category;
    Date date;
    TextInputEditText descriptionEdit;
    Button confirmbt;
    TextView adresstv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        View view = inflater.inflate(R.layout.new_marker_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button cancel_button = (Button) getView().findViewById(R.id.cancel_new_marker_button);
        eventCategorytv = getView().findViewById(R.id.newEventCategorytextView);
        enterDatetv = getView().findViewById(R.id.newEventEnterDate);
        confirmbt = getView().findViewById(R.id.newMarkerConfirmButton);
        descriptionEdit = getView().findViewById(R.id.newMarkerDescriptionEditView);
        adresstv = getView().findViewById(R.id.newMarkerAdresstv);
        adresstv.setOnClickListener(this);
        enterDatetv.setOnClickListener(this);
        confirmbt.setOnClickListener(this);
        cancel_button.setOnClickListener(this);

        spinner = getView().findViewById(R.id.spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, EventMarker.CATEGORIES);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);
                category = item;
                //eventCategorytv.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);


    }

    public void setAllVissible() {
        getActivity().findViewById(R.id.profil_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.new_marker_button).setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);
        switch (view.getId()) {
            //при необходимости очистить поля

            case R.id.cancel_new_marker_button:


                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.replace(R.id.place_holder_fragment, new Fragment());
                setAllVissible();


                break;

            case R.id.newEventEnterDate:
                Calendar now = Calendar.getInstance();
                DatePickerDialog a = new DatePickerDialog(getActivity(), d, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                a.show();
                break;
            case R.id.newMarkerConfirmButton:
                Toast.makeText(getActivity(), descriptionEdit.getText().toString(), Toast.LENGTH_SHORT).show();

                break;

            case R.id.newMarkerAdresstv:
                fragmentTransaction.hide(fragment);
                ((MainActivity) getActivity()).selectingPosition();

                break;
        }
        fragmentTransaction.commit();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            date = new Date(year - 1900, monthOfYear, dayOfMonth);
            SimpleDateFormat pattern = new SimpleDateFormat("d MMMM y г.", Locale.getDefault());
            enterDatetv.setText(pattern.format(date));

        }
    };

    public void setAdress(LatLng position) {
        Geocoder geocoder = new Geocoder(getActivity(),Locale.getDefault());
        Address address = null;
        try {
            address = geocoder.getFromLocation(position.latitude,position.longitude,1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adresstv.setText(address.getAddressLine(0));
    }

}
