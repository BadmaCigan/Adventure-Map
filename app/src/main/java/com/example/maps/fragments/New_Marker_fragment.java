package com.example.maps.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maps.MainActivity;
import com.example.maps.R;
import com.example.maps.services.UserService;
import com.example.maps.entity.EventMarker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class New_Marker_fragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, TextWatcher {
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
    SeekBar seekBar;
    float hue;
    ChipGroup chipGroup;
    EditText tagsEdit;
    Set<String> checkedChips;
    ArrayList<Chip> chips = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // ???????????????? ????????????????????, ?????????????? ?????????????????? ???????????????? ???????????? ?? layout ?? ?????????? ????????????????
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
        chipGroup = getView().findViewById(R.id.tagsGroup);
        tagsEdit = getView().findViewById(R.id.editTag);
        checkedChips = new TreeSet<>();
        for (String tag : MainActivity.mainActivity.tags) {
            Chip chip = (Chip) getActivity().getLayoutInflater().inflate(R.layout.chip_sample, chipGroup, false);
            chips.add(chip);
            chipGroup.addView(chip);
            chip.setText(tag);
            chip.setChecked(checkedChips.contains(tag));
            chip.setOnCheckedChangeListener(this);

        }
        tagsEdit.addTextChangedListener(this);

        spinner = getView().findViewById(R.id.spinner);

        // ?????????????? ?????????????? ArrayAdapter ?? ?????????????? ?????????????? ?????????? ?? ?????????????????????? ???????????????? ?????????????? spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, EventMarker.CATEGORIES);
        // ???????????????????? ???????????????? ?????? ?????????????????????????? ?????? ???????????? ????????????????
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // ?????????????????? ?????????????? ?? ???????????????? spinner
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // ???????????????? ?????????????????? ????????????
                String item = (String) parent.getItemAtPosition(position);
                category = item;
                //eventCategorytv.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        seekBar = (SeekBar) getView().findViewById(R.id.colorOfMark);
        seekBar.setOnSeekBarChangeListener(this);


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
            //?????? ?????????????????????????? ???????????????? ????????

            case R.id.cancel_new_marker_button:


                MainActivity.mainActivity.onBackPressed();


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
                    marker.hue = hue;
                    ((MainActivity) getActivity()).addMarker(marker);
                    MainActivity.mainActivity.onBackPressed();
                    UserService service = ((MainActivity) getActivity()).serv;

                    Call<Void> call = service.sendMsg(marker);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(MainActivity.mainActivity, "?????????????? ?????????????? ??????????????????", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("dsd", t.getMessage(), new Exception());
                        }
                    });

                }


                break;

            case R.id.newMarkerAdresstv:
                fragmentTransaction.hide(fragment).addToBackStack("toSelectinPosition");
                ((MainActivity) getActivity()).selectingPosition();

                break;
        }
        fragmentTransaction.commit();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            date = new Date(year - 1900, monthOfYear, dayOfMonth);
            SimpleDateFormat pattern = new SimpleDateFormat("d MMMM y ??.", Locale.getDefault());
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
            Toast.makeText(context, "?????????????? ?????? ??????????????", Toast.LENGTH_SHORT).show();
        } else if (category == null) {
            Toast.makeText(context, "???????????????? ??????????????????", Toast.LENGTH_SHORT).show();
        } else if (date == null) {
            Toast.makeText(context, "???????????????? ????????", Toast.LENGTH_SHORT).show();
        } else if (adresstv.getText().toString().equals("?????????????? ??????????")) {
            Toast.makeText(context, "???????????????? ?????????? ????????????????????", Toast.LENGTH_SHORT).show();
        } else if (!Pattern.compile("\\S+( \\S+)*\\S").matcher(descriptionEdit.getText().toString()).find()) {
            Toast.makeText(context, "???????????????? ???????????????? ?? ??????????????", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.peopleSeekBar:
                New_Marker_fragment.this.numberOfPeopletv.setText("" + seekBar.getProgress() + "/" + seekBar.getMax());
                break;
            case R.id.colorOfMark:
                New_Marker_fragment.this.seekBar.setThumbTintList(ColorStateList.valueOf(Color.HSVToColor(new float[]{
                        seekBar.getProgress(), 100, 50})));
                hue = seekBar.getProgress();
                break;
        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            checkedChips.add(compoundButton.getText().toString());
        } else {
            checkedChips.remove(compoundButton.getText().toString());
        }

    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void clean(){

    }
}
