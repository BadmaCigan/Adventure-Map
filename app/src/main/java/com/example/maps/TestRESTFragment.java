package com.example.maps;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maps.entity.EventMarker;
import com.example.maps.entity.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestRESTFragment extends Fragment implements View.OnClickListener {
    User user = new User(1234567,
            "Изобэлла");

    EventMarker eventMarker = new EventMarker(23478129,
            88.88, 35.56, "ТУса", "будет круто", 238112343243424223l, 3, 20, user.getId());

    UserService service = MainActivity.mainActivity.serv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        View view = inflater.inflate(R.layout.rest_test_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void setAllVissible() {
        getActivity().findViewById(R.id.profil_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.new_marker_button).setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button:
                Toast.makeText(getActivity(), "deleteDB", Toast.LENGTH_SHORT).show();
                service.deleteDB(1234).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(getActivity(), Boolean.toString(response.body()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button2:
                Toast.makeText(getActivity(), "createDB", Toast.LENGTH_SHORT).show();
                service.createDB().enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(getActivity(), Boolean.toString(response.body()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button3:
                Toast.makeText(getActivity(), "sendMsg", Toast.LENGTH_SHORT).show();
                service.sendMsg(eventMarker).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "not ok", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button4:
                Toast.makeText(getActivity(), "registrateUser", Toast.LENGTH_SHORT).show();
                service.registrateUser(new Gson().toJson(user)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "not ok", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button5:
                Toast.makeText(getActivity(), "isUserRegistrated", Toast.LENGTH_SHORT).show();
                service.isUserRegistrated(user.id).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(getActivity(), Boolean.toString(response.body()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button6:
                Toast.makeText(getActivity(), "getUserById", Toast.LENGTH_SHORT).show();
                service.getUserById(user.id).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button7:
                Toast.makeText(getActivity(), "deleteUser", Toast.LENGTH_SHORT).show();
                service.deleteUser(user.id).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(getActivity(), Boolean.toString(response.body()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button8:
                Toast.makeText(getActivity(), "deleteMarker", Toast.LENGTH_SHORT).show();
                service.deleteMarker( (int)eventMarker.id).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(getActivity(), Boolean.toString(response.body()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button9:
                Toast.makeText(getActivity(), "registrationOnEvent", Toast.LENGTH_SHORT).show();
                service.registrationOnEvent((int)eventMarker.id,user.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "not ok", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button10:
                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                service.test().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "not ok", Toast.LENGTH_SHORT).show();
                    }
                });
                break;


        }

    }
}
