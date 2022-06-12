package com.example.maps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maps.entity.EventMarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerEventMarkerAdapter extends RecyclerView.Adapter<RecyclerEventMarkerAdapter.EventMarkerViewHolder> {
    List<EventMarker> markerList = new ArrayList<EventMarker>();


    class EventMarkerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        float id;
        View view;
        EventMarker eventMarker;

        public EventMarkerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_event_pice_layout);
            description = itemView.findViewById(R.id.description_event_pice_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.mainActivity.goToMark(eventMarker);
                }
            });
        }

        public void bind(EventMarker marker) {
            eventMarker = marker;
            title.setText(marker.title);
            description.setText(marker.description);
            eventMarker = marker;
        }

        public EventMarkerViewHolder setOcnClick(View view){
            this.view = view;
            return this;
        }
    }

    public void setItems(Collection<EventMarker> markers) {
        markerList = (List<EventMarker>) markers;
        notifyDataSetChanged();
    }

    public void clearItems() {
        markerList.clear();
        notifyDataSetChanged();
    }


    @Override
    public EventMarkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_marker_piece_layout, parent, false);
        return new EventMarkerViewHolder(view).setOcnClick(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventMarkerViewHolder holder, int position) {
        holder.bind(markerList.get(position));
    }

    @Override
    public int getItemCount() {
        return markerList.size();
    }



}
