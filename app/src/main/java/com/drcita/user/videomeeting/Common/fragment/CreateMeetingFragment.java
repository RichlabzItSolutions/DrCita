package com.drcita.user.videomeeting.Common.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.drcita.user.R;
import com.drcita.user.videomeeting.Common.Actvity.CreateOrJoinActivity;
import com.drcita.user.videomeeting.Common.Listener.ResponseListener;
import com.drcita.user.videomeeting.Common.Utils.HelperClass;
import com.drcita.user.videomeeting.Common.Utils.NetworkUtils;
import com.drcita.user.videomeeting.GroupCall.Activity.GroupCallActivity;
import com.drcita.user.videomeeting.OneToOneCall.OneToOneCallActivity;
import com.google.android.material.snackbar.Snackbar;

public class CreateMeetingFragment extends Fragment {

    String auth_token  = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiI1ZTgwMTcwNC0yOTYzLTRiZjMtOGY2Ni1mNDk3NmY5MTUyN2EiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTczOTQ0NDYyNCwiZXhwIjoxNzQyMDM2NjI0fQ.FBsluy5YzYVcpMA2U3mx_T4QE9pzXDPpUBdA0aUgcPE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_meeting, container, false);

        EditText etName = view.findViewById(R.id.etName);

        Button btnJoin = view.findViewById(R.id.btnJoin);

        String[] meetingType = getContext().getResources().getStringArray(R.array.meeting_options);

        final String[] selectedMeetingType = new String[1];

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, meetingType);

        AutoCompleteTextView autocompleteTV = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        autocompleteTV.setAdapter(arrayAdapter);
        autocompleteTV.setDropDownBackgroundDrawable(
                ResourcesCompat.getDrawable(
                        getContext().getResources(),
                        R.drawable.et_style,
                        null
                )
        );

        autocompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMeetingType[0] = meetingType[i];
            }
        });


        btnJoin.setOnClickListener(v -> {
            if ("".equals(etName.getText().toString())) {
                Toast.makeText(getContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
            } else {
                NetworkUtils networkUtils = new NetworkUtils(getContext());
                if (networkUtils.isNetworkAvailable()) {
                    networkUtils.getToken(new ResponseListener<String>() {
                        @Override
                        public void onResponse(String token) {
                            networkUtils.createMeeting(auth_token, new ResponseListener<String>() {
                                @Override
                                public void onResponse(String meetingId) {
                                    Intent intent = null;
                                    if (!TextUtils.isEmpty(selectedMeetingType[0])) {
                                        if (selectedMeetingType[0].equals("One to One Meeting")) {
                                            intent = new Intent((CreateOrJoinActivity) getActivity(), OneToOneCallActivity.class);
                                        } else {
                                            intent = new Intent((CreateOrJoinActivity) getActivity(), GroupCallActivity.class);
                                        }
                                        intent.putExtra("token", auth_token);
                                        intent.putExtra("meetingId", meetingId);
                                        intent.putExtra("webcamEnabled", ((CreateOrJoinActivity) getActivity()).isWebcamEnabled());
                                        intent.putExtra("micEnabled", ((CreateOrJoinActivity) getActivity()).isMicEnabled());
                                        intent.putExtra("participantName", etName.getText().toString().trim());
                                        startActivity(intent);
                                        ((CreateOrJoinActivity) getActivity()).finish();
                                    } else {
                                        Toast.makeText(getContext(),
                                                "Please Choose Meeting Type", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }

                    });

                } else {
                    Snackbar snackbar = Snackbar.make(view.findViewById(R.id.createMeetingLayout), "No Internet Connection", Snackbar.LENGTH_LONG);
                    HelperClass.setSnackBarStyle(snackbar.getView(), 0);
                    snackbar.show();
                }
            }

        });
        return view;
    }


}