package com.example.maps;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maps.entity.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class LayersFragment extends Fragment implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    ChipGroup chipGroup;
    EditText tagsEdit;
    ArrayList<Chip> chips = new ArrayList<>();
    ArrayList<String> confirmTags = new ArrayList<>();
    Set<String> checkedChips;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        return inflater.inflate(R.layout.layers_fragments, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chipGroup = getView().findViewById(R.id.tags);
        tagsEdit = getView().findViewById(R.id.autoCompleteTags);
        getView().findViewById(R.id.cancel_layers_button).setOnClickListener(this);
        checkedChips = MainActivity.mainActivity.filteredTags;
        chipGroup.removeAllViews();

        for (String tag:MainActivity.mainActivity.tags){
            Chip chip = (Chip) getActivity().getLayoutInflater().inflate(R.layout.chip_sample, chipGroup,false);
            chips.add(chip);
            chipGroup.addView(chip);
            chip.setText(tag);
            chip.setChecked(checkedChips.contains(tag));
            chip.setOnCheckedChangeListener(this);

        }

        tagsEdit.addTextChangedListener(this);
        //chipGroup.removeViewAt(0);

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //при необходимости очистить поля

            case R.id.cancel_layers_button:
                getFragmentManager().popBackStack();


                break;



        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
        sorting();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            checkedChips.add(compoundButton.getText().toString());
        }else {
            checkedChips.remove(compoundButton.getText().toString());
        }
        sorting();
    }

    public void sorting(){
        String regex = ".*";
        for (int i =0;i<tagsEdit.getText().toString().length();i++){
            regex+=tagsEdit.getText().subSequence(i,i+1)+".*";
        }
        regex+=".*";
        Pattern pattern = Pattern.compile(regex);
        chipGroup.removeAllViews();
        for (Chip chip: chips){
            if(chip.isChecked()){
                chipGroup.removeView(chip);
                chipGroup.addView(chip);
            }
        }
        for (Chip chip: chips){
            if(pattern.matcher(chip.getText().toString()).find() && !chip.isChecked()){
                chipGroup.addView(chip);
            }
        }
    }
}