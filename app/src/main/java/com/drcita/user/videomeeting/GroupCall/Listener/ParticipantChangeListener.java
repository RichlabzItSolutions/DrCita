package com.drcita.user.videomeeting.GroupCall.Listener;

import java.util.List;

import live.videosdk.rtc.android.Participant;

public interface ParticipantChangeListener {

    void onChangeParticipant(List<List<Participant>> participantList);

    void onPresenterChanged(boolean screenShare);

    void onSpeakerChanged(List<List<Participant>> participantList, Participant activeSpeaker);
}
