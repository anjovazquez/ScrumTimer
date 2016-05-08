package com.avv.scrumtimer.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.avv.scrumtimer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angelvazquez on 7/5/16.
 */
public class ParticipantDialog extends DialogFragment implements TextView.OnEditorActionListener {

    @BindView(R.id.txt_your_name)
    EditText mEditText;

    public ParticipantDialog() {
        // Empty constructor required for DialogFragment
    }

    public static ParticipantDialog newInstance(ParticipantListener listener) {

        Bundle args = new Bundle();

        ParticipantDialog fragment = new ParticipantDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Nuevo participante");
        View view = inflater.inflate(R.layout.new_participant_dialog, container);
        TextView titleView = ((TextView) getDialog().findViewById(android.R.id.title));
        titleView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        titleView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        titleView.setPadding(10, 10, 10, 10);

        ButterKnife.bind(this, view);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface ParticipantListener {

        void onAddedParticipantDialog(String inputText);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            ((ParticipantListener) getTargetFragment()).onAddedParticipantDialog(mEditText.getText().toString());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            this.dismiss();

            return true;
        }
        return false;
    }


}
