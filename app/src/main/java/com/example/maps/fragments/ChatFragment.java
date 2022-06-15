package com.example.maps.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maps.MainActivity;
import com.example.maps.R;
import com.example.maps.adapters.RecyclerMessagesAdapter;
import com.example.maps.entity.Message;
import com.example.maps.entity.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerMessagesAdapter adapter;
    EditText messageEdit;
    Message[] messages;
    Button sendMsg;
    User user;
    int eventId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = MainActivity.mainActivity.user;
        sendMsg = getView().findViewById(R.id.sendMessage);
        sendMsg.setOnClickListener(this);
        recyclerView = getView().findViewById(R.id.messages);
        messageEdit = getView().findViewById(R.id.messageEdit);
        Bundle args = getArguments();
        messages = new Gson().fromJson(args.getString("messages"), Message[].class);


        adapter = new RecyclerMessagesAdapter();
        adapter.setUserId(user.id);
        adapter.setItems(messages);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


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
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.place_holder_fragment);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.replace(R.id.place_holder_fragment, new Fragment());
                setAllVissible();
                fragmentTransaction.commit();


                break;

            case R.id.sendMessage:
                String text = messageEdit.getText().toString();
                if (!text.equals("")) {
                    eventId = getArguments().getInt("eventId");
                    messageEdit.setText("");
                    Message message = new Message(Math.abs(UUID.randomUUID().hashCode()), text, user.nickName, user.id, eventId, Calendar.getInstance().getTimeInMillis());
                    MainActivity.mainActivity.serv.sendMessage(new Gson().toJson(message)).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            ChatFragment.this.update();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }
                break;

        }

    }

    public void update() {
        MainActivity.mainActivity.serv.getEventMessages(eventId).enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                adapter.setItems(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {

            }
        });
    }
}
