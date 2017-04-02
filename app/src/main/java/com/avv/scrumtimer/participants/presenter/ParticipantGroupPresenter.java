package com.avv.scrumtimer.participants.presenter;

import com.avv.scrumtimer.Presenter;
import com.avv.scrumtimer.data.MemoryCache;
import com.avv.scrumtimer.participants.Participant;
import com.avv.scrumtimer.participants.ParticipantGroup;
import com.avv.scrumtimer.participants.view.ParticipantGroupView;
import com.avv.scrumtimer.participants.view.ParticipantsView;

import java.util.List;

/**
 * Created by anjov on 26/03/2017.
 */

public class ParticipantGroupPresenter implements Presenter {

    private ParticipantGroupView participantGroupView;

    public ParticipantGroupPresenter(ParticipantGroupView participantGroupsView){
        this.participantGroupView = participantGroupsView;
    }

    public void loadParticipantGroups(){
        List<ParticipantGroup> participantGroupsList = MemoryCache.getGroupsParticipant(participantGroupView.getContext());
        participantGroupView.onParticipantGroupsListLoaded(participantGroupsList);
    }

    public void addParticipantGroup(ParticipantGroup participantGroup){
        MemoryCache.getGroupsParticipant(participantGroupView.getContext());
        MemoryCache.groupsParticipant.add(participantGroup);
        MemoryCache.saveGroupsParticipant(MemoryCache.groupsParticipant, participantGroupView.getContext());
        participantGroupView.onParticipantGroupsListLoaded(MemoryCache.groupsParticipant);
    }

    public void removeParticipantGroup(ParticipantGroup participantGroup){
        MemoryCache.getGroupsParticipant(participantGroupView.getContext());
        for(int i=0;i<MemoryCache.groupsParticipant.size();i++){
            if(participantGroup.getName().equalsIgnoreCase(MemoryCache.groupsParticipant.get(i).getName())){
                MemoryCache.groupsParticipant.remove(i);
                MemoryCache.saveGroupsParticipant(MemoryCache.groupsParticipant, participantGroupView.getContext());
            }
        }
        participantGroupView.onParticipantGroupsListLoaded(MemoryCache.groupsParticipant);
    }

    public void setSelectedGroup(String groupName){
        MemoryCache.selectedGroupName = groupName;
        MemoryCache.saveSelectedGroup(groupName, participantGroupView.getContext());
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
