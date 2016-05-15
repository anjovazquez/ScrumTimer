package com.avv.scrumtimer.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.IOException;

/**
 * Created by angelvazquez on 8/5/16.
 */
public class MyCustomListPreference extends ListPreference {

    private int mClickedDialogEntryIndex;
    private String mValue;
    private MediaPlayer mMediaPlayer;

    public MyCustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomListPreference(Context context) {
        super(context);
    }

    private int getValueIndex() {
        int index = findIndexOfValue(mValue=getValue());
        return index;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        mMediaPlayer = new MediaPlayer();
        if (getEntries() == null || getEntryValues() == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        mClickedDialogEntryIndex = getValueIndex();
        builder.setSingleChoiceItems(getEntries(), mClickedDialogEntryIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mClickedDialogEntryIndex = which;

                        /*
                         * Clicking on an item simulates the positive button
                         * click, and dismisses the dialog.
                         */
                        /*
                        onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                        */

                        mValue = getEntryValues()[which].toString();
                        Uri uri = uriFromRaw(mValue);

                        try {
                            playSong(uri);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        /*
         * The typical interaction for list-based dialogs is to have
         * click-on-an-item dismiss the dialog instead of the user having to
         * press 'Ok'.
         */
        builder.setPositiveButton("Ok", this);
        builder.setNegativeButton("Cancel", this);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        mMediaPlayer.stop();
        mMediaPlayer.release();

        if (positiveResult && mClickedDialogEntryIndex >= 0 && getEntryValues() != null) {
            String value = getEntryValues()[mClickedDialogEntryIndex].toString();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }

    }

    private void playSong(Uri path) throws IllegalArgumentException,
            IllegalStateException, IOException {

        Log.d("ringtone", "playSong :: " + path);

        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(getContext(), path);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    private Uri uriFromRaw(String name) {
        int resId = getContext().getResources().getIdentifier(name, "raw", getContext().getPackageName());
        return Uri.parse("android.resource://" + getContext().getPackageName() + "/" + resId);
    }


}
