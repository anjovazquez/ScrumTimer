package com.avv.scrumtimer.participants.view;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.fonts.FontManager;
import com.avv.scrumtimer.participants.Participant;
import com.avv.scrumtimer.participants.ParticipantGroup;
import com.avv.scrumtimer.participants.adapter.ParticipantGroupsAdapter;
import com.avv.scrumtimer.participants.adapter.ParticipantsAdapter;
import com.avv.scrumtimer.participants.dialog.ParticipantDialog;
import com.avv.scrumtimer.participants.dialog.ParticipantGroupDialog;
import com.avv.scrumtimer.participants.listeners.OnRecyclerViewItemClickListener;
import com.avv.scrumtimer.participants.presenter.ParticipantGroupPresenter;
import com.avv.scrumtimer.participants.presenter.ParticipantsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ParticipantGroupsFragment extends Fragment implements ParticipantGroupDialog.ParticipantGroupListener,
        ParticipantGroupView {

    @BindView(R.id.participant_group_list)
    RecyclerView participantGroupsRV;

    @BindView(R.id.fabAddParticipantGroup)
    FloatingActionButton fabAddParticipantGroup;

    @BindView(R.id.mEmpty)
    ViewGroup mEmpty;

    private OnGroupSelectedListener mListener;
    private ParticipantGroupsAdapter adapter;
    private ParticipantGroupPresenter presenter;

    public ParticipantGroupsFragment() {}

    public static ParticipantGroupsFragment newInstance() {
        ParticipantGroupsFragment fragment = new ParticipantGroupsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ParticipantGroupPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_configuration_fragment, container, false);
        ButterKnife.bind(this, view);

        Typeface iconFont = FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.participant_group_container), iconFont);

        adapter = new ParticipantGroupsAdapter(new ArrayList<ParticipantGroup>(), R.layout.layout_participant_group);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkRecyclerViewIsEmpty();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                checkRecyclerViewIsEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkRecyclerViewIsEmpty();
            }
        });
        participantGroupsRV.setAdapter(adapter);
        participantGroupsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        participantGroupsRV.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeThreshold(viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (viewHolder instanceof ParticipantGroupsAdapter.ViewHolder){
                    ParticipantGroup pg = new ParticipantGroup();
                    pg.setName(((ParticipantGroupsAdapter.ViewHolder)viewHolder).text.getText().toString());
                    presenter.removeParticipantGroup(pg);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(participantGroupsRV);

        fabAddParticipantGroup.setOnClickListener(new AddParticipantListener());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<ParticipantGroup>() {
            @Override
            public void onItemClick(View view, ParticipantGroup participantGroup) {
                presenter.setSelectedGroup(participantGroup.getName());
                mListener.onGroupSelected(participantGroup.getName());
            }
        });

        presenter.loadParticipantGroups();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGroupSelectedListener) {
            mListener = (OnGroupSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void checkRecyclerViewIsEmpty() {
        if (adapter.getItemCount() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onParticipantGroupsListLoaded(List<ParticipantGroup> participantList) {
        adapter.setParticipantsList(participantList);
        checkRecyclerViewIsEmpty();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public interface OnGroupSelectedListener {
        void onGroupSelected(String groupName);
    }

    public class AddParticipantListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ParticipantGroupDialog participantGroupDialog = new ParticipantGroupDialog();
            participantGroupDialog.setTargetFragment(ParticipantGroupsFragment.this, 1);
            participantGroupDialog.show(fm, "fragment_edit_name");
        }
    }

    @Override
    public void onAddedParticipantGroupDialog(String inputText) {
        ParticipantGroup participant = new ParticipantGroup(inputText);
        presenter.addParticipantGroup(participant);
    }

}