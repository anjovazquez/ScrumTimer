package com.avv.scrumtimer.participants.view;

import android.content.Context;

import com.avv.scrumtimer.participants.Participant;

import java.util.List;

/**
 * Created by anjov on 12/02/2017.
 */

public interface ParticipantsView {

    void onParticipantsListLoaded(List<Participant> participantList);

    Context getContext();

}
