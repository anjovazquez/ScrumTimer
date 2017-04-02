package com.avv.scrumtimer.participants.view;

import android.content.Context;

import com.avv.scrumtimer.participants.ParticipantGroup;

import java.util.List;

/**
 * Created by anjov on 26/03/2017.
 */

public interface ParticipantGroupView {

    void onParticipantGroupsListLoaded(List<ParticipantGroup> participantList);

    Context getContext();
}
