package com.drcita.user.videomeeting.Common.fragment;

import android.os.Bundle;

import android.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drcita.user.R;
import com.drcita.user.videomeeting.Common.Actvity.CreateOrJoinActivity;


public class CreateOrJoinFragment extends Fragment {

    public CreateOrJoinFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_or_join, container, false);

        view.findViewById(R.id.btnCreateMeeting).setOnClickListener(v -> {
            ((CreateOrJoinActivity) getActivity()).CreateMeetingFragment();
        });

        view.findViewById(R.id.btnJoinMeeting).setOnClickListener(v -> {
            ((CreateOrJoinActivity) getActivity()).joinMeetingFragment();
        });
        // Inflate the layout for this fragment
        return view;

    }
}