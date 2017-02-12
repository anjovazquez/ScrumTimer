package com.avv.scrumtimer.participants.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.data.MemoryCache;
import com.avv.scrumtimer.participants.Participant;
import com.avv.scrumtimer.participants.listeners.OnRecyclerViewItemClickListener;
import com.avv.scrumtimer.participants.view.ParticipantsFragment;

import java.util.List;

/**
 * Created by anjov on 12/02/2017.
 */

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> implements View.OnClickListener {

    private List<Participant> items;
    private int itemLayout;

    private OnRecyclerViewItemClickListener<Participant> itemClickListener;

    public ParticipantsAdapter(List<Participant> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    public void setParticipantsList(List<Participant> participantsList){
        this.items = participantsList;
        notifyDataSetChanged();
    }

    @Override
    public ParticipantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        v.setOnClickListener(this);
        return new ParticipantsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ParticipantsAdapter.ViewHolder holder, int position) {
        Participant item = items.get(position);
        holder.itemView.setTag(item);
        holder.text.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<Participant> listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            Participant model = (Participant) view.getTag();
            itemClickListener.onItemClick(view, model);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.participant_name);
        }
    }
}

