package com.avv.scrumtimer.timer.presenter;

import com.avv.scrumtimer.Presenter;
import com.avv.scrumtimer.data.MemoryCache;
import com.avv.scrumtimer.participants.Participant;
import com.avv.scrumtimer.timer.CountDownTimerFragment;
import com.avv.scrumtimer.timer.view.CountDownView;

import java.util.Collections;
import java.util.List;

/**
 * Created by anjov on 01/04/2017.
 */

public class CountDownTimerPresenter implements Presenter {


    private CountDownView view;

    public void setView(CountDownView view) {
        this.view = view;
    }

    public List<Participant> getParticipants(String groupName) {
        List<Participant> participants = MemoryCache.getParticipants(groupName, view.getContext());
        if (MemoryCache.isRandomShift(view.getContext())) {
            Collections.shuffle(participants);
        }
        return participants;
    }

    public void saveResult(String participantName, long totalTime){
        MemoryCache.setResult(participantName, totalTime);
    }

    public void resetResults() {
        MemoryCache.resetResults();
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
