package com.example.maps.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.maps.MainActivity;
import com.example.maps.R;
import com.example.maps.entity.User;

public class Registration_fragment extends Fragment implements View.OnClickListener {
    private static String CLIENT_ID = "5400655";
    private static String CLIENT_SECRET = "wWtRxNOBD6yfzkclsaSE";
    private static String TOKEN_URL = "https://oauth.vk.com/access_token";
    private static String OAUTH_URL = "http://oauth.vk.com/authorize";
    private static String RESPONSE_TYPE = "code";
    private static String VK_API_URL = "https://api.vk.com/method/users.get";
    private static String REDIRECT_URI = "http://localhost";
    private static String OUR_SERVER = "http://192.168.1.35:8080/VK/";


EditText nicknemeET;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nicknemeET = getView().findViewById(R.id.editTextTextPersonName);
        getView().findViewById(R.id.sign_up_button).setOnClickListener(this);
        ImageButton cancel_button = (ImageButton) getView().findViewById(R.id.cancel_profil_button);
        cancel_button.setOnClickListener(this);
        //ArrayList<VKScope> list = new ArrayList<>();
        //list.add(VKScope.WALL);
        //list.add(VKScope.PHOTOS);
        //VK.login(getActivity(),list);



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
            case R.id.sign_up_button:
                ((MainActivity)getActivity()).registrateUser(new User(getArguments().getInt("id"),
                        nicknemeET.getText().toString()));

                break;


        }

    }

}
