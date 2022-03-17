package com.year3project.qrcodegenerator;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    private static final String TAG = "ScheduleFragment";

    private Context context;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_schedule_fragment, container, false);

        rv = view.findViewById(R.id.announcementRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new GetAnnouncement(context, rv).execute();
        return view;
    }
}