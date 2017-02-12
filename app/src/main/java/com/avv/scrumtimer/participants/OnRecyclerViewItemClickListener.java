package com.avv.scrumtimer.participants;

import android.view.View;

/**
 * Created by angelvazquez on 15/5/16.
 */
public interface OnRecyclerViewItemClickListener<Model> {
    public void onItemClick(View view, Model model);
}
