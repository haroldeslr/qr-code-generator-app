package com.year3project.qrcodegenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AnnouncementModel> announcementModelArrayList;

    public AnnouncementAdapter(Context context, ArrayList<AnnouncementModel> announcementModelArrayList) {
        this.context = context;
        this.announcementModelArrayList = announcementModelArrayList;
    }

    @NonNull
    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.ViewHolder holder, int position) {
        AnnouncementModel model = announcementModelArrayList.get(position);
        holder.cardTitleTV.setText(model.getTitle());
        holder.cardMessageTV.setText(model.getMessage());
        holder.cardDateTV.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return announcementModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cardTitleTV, cardMessageTV, cardDateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitleTV = itemView.findViewById(R.id.card_title);
            cardMessageTV = itemView.findViewById(R.id.card_message);
            cardDateTV = itemView.findViewById(R.id.card_date);
        }
    }
}
