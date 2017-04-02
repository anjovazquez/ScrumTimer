package com.avv.scrumtimer.participants.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.fonts.FontManager;
import com.avv.scrumtimer.participants.ParticipantGroup;
import com.avv.scrumtimer.participants.listeners.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by anjov on 12/02/2017.
 */

public class ParticipantGroupsAdapter extends RecyclerView.Adapter<ParticipantGroupsAdapter.ViewHolder> implements View.OnClickListener {

    private List<ParticipantGroup> items;
    private int itemLayout;

    private OnRecyclerViewItemClickListener<ParticipantGroup> itemClickListener;

    public ParticipantGroupsAdapter(List<ParticipantGroup> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    public void setParticipantsList(List<ParticipantGroup> participantGroupsList) {
        this.items = participantGroupsList;
        notifyDataSetChanged();
    }

    @Override
    public ParticipantGroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        v.setOnClickListener(this);
        return new ParticipantGroupsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ParticipantGroupsAdapter.ViewHolder holder, int position) {
        ParticipantGroup item = items.get(position);
        holder.itemView.setTag(item);
        holder.text.setText(item.getName());
        holder.cSelected.setVisibility(View.GONE);
        if (item.isSelected()) {
            holder.cSelected.setText(holder.itemView.getContext().getResources().getString(R.string.fa_check));
            holder.cSelected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<ParticipantGroup> listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            ParticipantGroup model = (ParticipantGroup) view.getTag();
            itemClickListener.onItemClick(view, model);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView cSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.participant_group_name);
            cSelected = (TextView) itemView.findViewById(R.id.participant_group_selected);

            Typeface iconFont = FontManager.getTypeface(itemView.getContext(), FontManager.FONTAWESOME);
            FontManager.markAsIconContainer(itemView.findViewById(R.id.participant_group_container), iconFont);
        }
    }
}

