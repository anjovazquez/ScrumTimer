package com.avv.scrumtimer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.participant_list) RecyclerView participants;
    @BindView(R.id.shift) CheckBox shift;
    @BindView(R.id.shiftTime) NumberPicker shiftTime;
    @BindView(R.id.shiftTimeMinute) NumberPicker shiftTimeMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        participants = (RecyclerView)findViewById(R.id.participant_list);
        participants.setAdapter(new ParticipantsAdapter(loadParticipants(), R.layout.layout_participant));
        participants.setLayoutManager(new LinearLayoutManager(this));
        participants.setItemAnimator(new DefaultItemAnimator());

        shiftTime = (NumberPicker) findViewById(R.id.shiftTime);
        shiftTimeMinute = (NumberPicker) findViewById(R.id.shiftTimeMinute);

        shiftTime.setMinValue(0);
        shiftTime.setMaxValue(10);

        shiftTimeMinute.setMinValue(0);
        shiftTimeMinute.setMaxValue(59);
    }

    private List<Participant> loadParticipants() {
        List<Participant> participants = new ArrayList<Participant>();
        participants.add(new Participant("Lucía"));
        participants.add(new Participant("Pablo"));
        participants.add(new Participant("Esteban"));
        participants.add(new Participant("Javi"));
        return participants;
    }

    public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {

        private List<Participant> items;
        private int itemLayout;

        public ParticipantsAdapter(List<Participant> items, int itemLayout) {
            this.items = items;
            this.itemLayout = itemLayout;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Participant item = items.get(position);
            holder.text.setText(item.getName());
        }

        @Override public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.participant_name);
            }
        }
    }
}
