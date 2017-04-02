package com.avv.scrumtimer;

import com.avv.scrumtimer.data.MemoryCache;

/**
 * Created by anjov on 01/04/2017.
 */

public class MainPresenter implements Presenter {

    private MainView view;

    public void setMainView(MainView view){
        this.view = view;
    }

    public void initLoadData(){
        MemoryCache.initLoadData(view.getContext());
    }

    public boolean isParticipantGroupSelected(){
        return MemoryCache.isGroupSelected();
    }

    public String getSelectedGroup(){
        return MemoryCache.selectedGroupName;
    }

    public boolean hasParticipantGroupMembers(){
        return !(MemoryCache.getParticipants(MemoryCache.selectedGroupName, view.getContext()).isEmpty());
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
