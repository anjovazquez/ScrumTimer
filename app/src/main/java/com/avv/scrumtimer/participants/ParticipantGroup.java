package com.avv.scrumtimer.participants;

import java.util.List;

/**
 * Created by angelvazquez on 4/5/16.
 */
public class ParticipantGroup {

    private String name;
    private boolean selected;

    private List<Participant> participantList;

    public ParticipantGroup() {
        this.selected = false;
    }

    public ParticipantGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}