package com.avv.scrumtimer.participants.presenter;

import android.app.Application;

import com.avv.scrumtimer.Presenter;
import com.avv.scrumtimer.data.MemoryCache;
import com.avv.scrumtimer.participants.Participant;
import com.avv.scrumtimer.participants.view.ParticipantsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anjov on 12/02/2017.
 */

public class ParticipantsPresenter implements Presenter {

    private ParticipantsView participantsView;

    public ParticipantsPresenter(ParticipantsView participantsView){
        this.participantsView = participantsView;
    }

    public void loadParticipants(String groupName){
        List<Participant> participantsList = MemoryCache.getParticipants(groupName, participantsView.getContext());
        participantsView.onParticipantsListLoaded(participantsList);
    }

    public void addParticipant(String groupName, Participant participant){
        MemoryCache.getParticipants(groupName, participantsView.getContext());
        MemoryCache.participants.add(participant);
        MemoryCache.saveParticipants(groupName, MemoryCache.participants, participantsView.getContext());
        participantsView.onParticipantsListLoaded(MemoryCache.participants);
    }

    public void removeParticipant(String groupName, Participant participant){
        MemoryCache.getParticipants(groupName, participantsView.getContext());
        for(int i=0;i<MemoryCache.participants.size();i++){
            if(participant.getName().equalsIgnoreCase(MemoryCache.participants.get(i).getName())){
                MemoryCache.participants.remove(i);
                MemoryCache.saveParticipants(groupName,MemoryCache.participants,  participantsView.getContext());
            }
        }
        participantsView.onParticipantsListLoaded(MemoryCache.participants);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

}
