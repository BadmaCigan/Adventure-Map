package com.example.maps;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maps.entity.User;

public class Profil_fragment extends Fragment implements View.OnClickListener {
    TextView text_id;
    TextView text_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        return inflater.inflate(R.layout.profil_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton cancel_button = (ImageButton) getView().findViewById(R.id.cancel_profil_button);
        cancel_button.setOnClickListener(this);
        //text_id =(TextView) getView().findViewById(R.id.ID_text);
        text_name =(TextView) getView().findViewById(R.id.name_text);
        User user = ((MainActivity)getActivity()).user;
        //text_id.setText(text_id.getText().toString() + user.getId());
        text_name.setText(text_name.getText().toString() + user.nickName);

    }

    public void setAllVissible(){
        getActivity().findViewById(R.id.profil_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.new_marker_button).setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //при необходимости очистить поля

            case R.id.cancel_profil_button:
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.replace(R.id.place_holder_fragment,new Fragment());
                fragmentTransaction.commit();
                setAllVissible();


                break;



        }

    }

}
