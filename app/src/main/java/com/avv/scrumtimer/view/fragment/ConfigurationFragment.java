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
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.avv.scrumtimer.Participant;
import com.avv.scrumtimer.R;
import com.avv.scrumtimer.view.dialog.ParticipantDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigurationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigurationFragment extends Fragment implements ParticipantDialog.ParticipantListener {

    @BindView(R.id.participant_list)
    RecyclerView participants;
    @BindView(R.id.shift)
    CheckBox shift;
    @BindView(R.id.shiftTime)
    NumberPicker shiftTime;
    @BindView(R.id.shiftTimeMinute) NumberPicker shiftTimeMinute;
    @BindView(R.id.fabAddParticipant)
    FloatingActionButton fabAddParticipant;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConfigurationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigurationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigurationFragment newInstance(String param1, String param2) {
        ConfigurationFragment fragment = new ConfigurationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuration_fragment, container, false);
        ButterKnife.bind(this, view);


        loadParticipants();

        participants.setAdapter(new ParticipantsAdapter(participantList, R.layout.layout_participant));
        participants.setLayoutManager(new LinearLayoutManager(getActivity()));
        participants.setItemAnimator(new DefaultItemAnimator());


        shiftTime.setMinValue(0);
        String[] hours = loadHourValues();
        shiftTime.setMaxValue(hours.length-1);
        shiftTime.setDisplayedValues(hours);


        shiftTimeMinute.setMinValue(0);
        String[] minutes = loadMinuteValues();
        shiftTimeMinute.setMaxValue(minutes.length-1);
        shiftTimeMinute.setDisplayedValues(minutes);

        fabAddParticipant.setOnClickListener(new AddParticipantListener());

        return view;
    }

    private String[] loadHourValues(){
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<11;i++) {
            list.add(String.format("%02d",i));
        }
        String[] hours = new String[list.size()];
        return list.toArray(hours);

    }

    private String[] loadMinuteValues(){
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<59;i=i+5) {
            list.add(String.format("%02d",i));
        }
        String[] minutes = new String[list.size()];
        return list.toArray(minutes);

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

    public class AddParticipantListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ParticipantDialog participantDialog = new ParticipantDialog();
            participantDialog.setTargetFragment(ConfigurationFragment.this, 1);
            participantDialog.show(fm, "fragment_edit_name");
        }
    }


    List<Participant> participantList = new ArrayList<Participant>();

    private List<Participant> loadParticipants() {

        participantList.add(new Participant("Lucía"));
        participantList.add(new Participant("Pablo"));
        participantList.add(new Participant("Esteban"));
        participantList.add(new Participant("Javi"));
        participantList.add(new Participant("Manuel"));
        return participantList;
    }

    @Override
    public void onAddedParticipantDialog(String inputText) {
        participantList.add(new Participant(inputText));
        //participants.setAdapter(participantList);
        participants.getAdapter().notifyDataSetChanged();
    }
}
