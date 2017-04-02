package com.avv.scrumtimer.results.presenter;

import com.avv.scrumtimer.Presenter;
import com.avv.scrumtimer.data.MemoryCache;
import com.avv.scrumtimer.results.view.ResultsView;

import java.util.HashMap;

/**
 * Created by anjov on 02/04/2017.
 */

public class ResultsPresenter implements Presenter{

    private ResultsView view;

    public void setView(ResultsView view){
        this.view = view;
    }

    public HashMap<String, Long> getResults(){
        return MemoryCache.results;
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
