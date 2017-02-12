package com.avv.scrumtimer.participants.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.data.MemoryCache;
import com.avv.scrumtimer.participants.adapter.ParticipantsAdapter;
import com.avv.scrumtimer.participants.listeners.OnRecyclerViewItemClickListener;
import com.avv.scrumtimer.participants.Participant;
import com.avv.scrumtimer.participants.dialog.ParticipantDialog;
import com.avv.scrumtimer.participants.presenter.ParticipantsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ParticipantsFragment extends Fragment implements ParticipantDialog.ParticipantListener,
        ParticipantsView {

    @BindView(R.id.participant_list)
    RecyclerView participantsRV;

    @BindView(R.id.fabAddParticipant)
    FloatingActionButton fabAddParticipant;

    private OnFragmentInteractionListener mListener;
    private ParticipantsAdapter adapter;
    private ParticipantsPresenter presenter;

    public ParticipantsFragment() {}

    public static ParticipantsFragment newInstance() {
        ParticipantsFragment fragment = new ParticipantsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ParticipantsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuration_fragment, container, false);
        ButterKnife.bind(this, view);

        adapter = new ParticipantsAdapter(new ArrayList<Participant>(), R.layout.layout_participant);
        participantsRV.setAdapter(adapter);
        participantsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        participantsRV.setItemAnimator(new DefaultItemAnimator());

        fabAddParticipant.setOnClickListener(new AddParticipantListener());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<Participant>() {
            @Override
            public void onItemClick(View view, Participant participant) {
                presenter.removeParticipant(participant);
            }
        });

        presenter.loadParticipants();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onParticipantsListLoaded(List<Participant> participantList) {
        adapter.setParticipantsList(participantList);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class AddParticipantListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ParticipantDialog participantDialog = new ParticipantDialog();
            participantDialog.setTargetFragment(ParticipantsFragment.this, 1);
            participantDialog.show(fm, "fragment_edit_name");
        }
    }

    @Override
    public void onAddedParticipantDialog(String inputText) {
        Participant participant = new Participant(inputText);
        presenter.addParticipant(participant);
    }

}