package com.avv.scrumtimer.view.fragment;

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
import android.widget.TextView;

import com.avv.scrumtimer.Participant;
import com.avv.scrumtimer.R;
import com.avv.scrumtimer.view.MemoryCache;
import com.avv.scrumtimer.view.OnRecyclerViewItemClickListener;
import com.avv.scrumtimer.view.dialog.ParticipantDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ParticipantsFragment extends Fragment implements ParticipantDialog.ParticipantListener {

    @BindView(R.id.participant_list)
    RecyclerView participants;

    @BindView(R.id.fabAddParticipant)
    FloatingActionButton fabAddParticipant;

    private OnFragmentInteractionListener mListener;

    private ParticipantsAdapter adapter;

    public ParticipantsFragment() {
        // Required empty public constructor
    }


    public static ParticipantsFragment newInstance() {
        ParticipantsFragment fragment = new ParticipantsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuration_fragment, container, false);
        ButterKnife.bind(this, view);

        loadParticipants();

        adapter = new ParticipantsAdapter(loadParticipants(), R.layout.layout_participant);
        participants.setAdapter(adapter);
        participants.setLayoutManager(new LinearLayoutManager(getActivity()));
        participants.setItemAnimator(new DefaultItemAnimator());

        fabAddParticipant.setOnClickListener(new AddParticipantListener());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<Participant>() {
            @Override
            public void onItemClick(View view, Participant participant) {
                adapter.remove(participant);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> implements View.OnClickListener {

        private List<Participant> items;
        private int itemLayout;

        private OnRecyclerViewItemClickListener<Participant> itemClickListener;

        public ParticipantsAdapter(List<Participant> items, int itemLayout) {
            this.items = items;
            this.itemLayout = itemLayout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Participant item = items.get(position);
            holder.itemView.setTag(item);
            holder.text.setText(item.getName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void add(Participant participant){
            MemoryCache.getParticipants(getActivity());
            MemoryCache.participants.add(participant);
            items.add(participant);
            notifyItemInserted(items.indexOf(participant));
            MemoryCache.saveParticipants(MemoryCache.participants, getActivity());
        }

        public void remove(Participant participant){
            int position = items.indexOf(participant);
            items.remove(position);
            notifyItemRemoved(position);
            MemoryCache.saveParticipants(items, getActivity());
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

    public class AddParticipantListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ParticipantDialog participantDialog = new ParticipantDialog();
            participantDialog.setTargetFragment(ParticipantsFragment.this, 1);
            participantDialog.show(fm, "fragment_edit_name");
        }
    }

    private List<Participant> loadParticipants() {
        return MemoryCache.getParticipants(getActivity());
    }

    @Override
    public void onAddedParticipantDialog(String inputText) {
        Participant participant = new Participant(inputText);
        adapter.add(participant);
    }
}
