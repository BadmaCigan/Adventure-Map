package com.example.maps;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Search_fragment extends Fragment implements View.OnClickListener {

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

            case R.id.cancel_search_button:
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragment);
                setAllVissible();
                fragmentTransaction.commit();


                break;



        }

    }

}
