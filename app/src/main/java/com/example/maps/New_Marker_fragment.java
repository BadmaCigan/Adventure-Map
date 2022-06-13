package com.example.maps;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maps.entity.EventMarker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class New_Marker_fragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    Spinner spinner;
    TextView eventCategorytv;
    TextView enterDatetv;
    String category;
    Date date;
    TextInputEditText descriptionEdit;
    Button confirmbt;
    TextView adresstv;
    TextView titleev;
    TextView numberOfPeopletv;
    LatLng position;
    SeekBar numberOfPeopleseekbar;

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
        ImageButton cancel_button = (ImageButton) getView().findViewById(R.id.cancel_new_marker_button);
        eventCategorytv = getView().findViewById(R.id.newEventCategorytextView);
        enterDatetv = getView().findViewById(R.id.newEventEnterDate);
        confirmbt = getView().findViewById(R.id.newMarkerConfirmButton);
        descriptionEdit = getView().findViewById(R.id.newMarkerDescriptionEditView);
        adresstv = getView().findViewById(R.id.newMarkerAdresstv);
        titleev = getView().findViewById(R.id.newMarkerEventTitle);
        numberOfPeopletv = getView().findViewById(R.id.new_marker_number_of_people);
        numberOfPeopleseekbar = getView().findViewById(R.id.peopleSeekBar);
        adresstv.setOnClickListener(this);
        enterDatetv.setOnClickListener(this);
        confirmbt.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
        numberOfPeopleseekbar.setOnSeekBarChangeListener(this);

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


                fragmentManager.popBackStack();


                break;

            case R.id.newEventEnterDate:
                Calendar now = Calendar.getInstance();
                DatePickerDialog a = new DatePickerDialog(getActivity(), d, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                a.show();
                break;
            case R.id.newMarkerConfirmButton:
                if (verify()) {
                    EventMarker marker = new EventMarker(Math.abs(UUID.randomUUID().hashCode()),
                            position.latitude, position.longitude,
                            titleev.getText().toString(),
                            descriptionEdit.getText().toString(), date.getTime(), EventMarker.getIntCategory(category), numberOfPeopleseekbar.getProgress(),
                            (MainActivity.mainActivity).user.getId());
                    ((MainActivity) getActivity()).addMarker(marker);
                    fragmentManager.popBackStack();
                    UserService service = ((MainActivity) getActivity()).serv;

                    Call<Void> call = service.sendMsg(marker);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(MainActivity.mainActivity, "Событие успешно добавлено", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("dsd", t.getMessage(), new Exception());
                        }
                    });

                }


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
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        this.position = position;
        Address address = null;
        try {
            address = geocoder.getFromLocation(position.latitude, position.longitude, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adresstv.setText(address.getAddressLine(0));
    }

    public boolean verify() {
        Context context = getActivity();
        //Toast.makeText(context,, Toast.LENGTH_SHORT).show();
        if (!Pattern.compile("\\S+( \\S+)*\\S").matcher(titleev.getText().toString()).find()) {
            Toast.makeText(context, "Введите имя события", Toast.LENGTH_SHORT).show();
        } else if (category == null) {
            Toast.makeText(context, "Выберите категорию", Toast.LENGTH_SHORT).show();
        } else if (date == null) {
            Toast.makeText(context, "Выберите дату", Toast.LENGTH_SHORT).show();
        } else if (adresstv.getText().toString().equals("Введите адрес")) {
            Toast.makeText(context, "Выберите место проведения", Toast.LENGTH_SHORT).show();
        } else if (!Pattern.compile("\\S+( \\S+)*\\S").matcher(descriptionEdit.getText().toString()).find()) {
            Toast.makeText(context, "Добавьте описание к событию", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        New_Marker_fragment.this.numberOfPeopletv.setText("" + seekBar.getProgress() + "/" + seekBar.getMax());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
