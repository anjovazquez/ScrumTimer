package com.avv.scrumtimer.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avv.scrumtimer.Participant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angelvazquez on 18/5/16.
 */
public class MemoryCache {

    public static final String PARTICIPANTS = "Participants";
    public static final String RANDOM_SHIFT = "randomshift";

    public static List<Participant> participants;
    public static HashMap<String, Long> results = new HashMap<String, Long>();

    public static void saveParticipants(List<Participant> participants, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PARTICIPANTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(participants);

        editor.putString(PARTICIPANTS, json);
        editor.commit();

        MemoryCache.participants = participants;
    }

    public static List<Participant> getParticipants(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PARTICIPANTS, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(PARTICIPANTS, "");
        if ("".equals(json)) {
            participants = new ArrayList<Participant>();
            participants.add(new Participant("Lucía"));
            participants.add(new Participant("Pablo"));
            participants.add(new Participant("Esteban"));
            participants.add(new Participant("Javi"));
            participants.add(new Participant("Manuel"));
            return participants;
        } else {
            Gson gson = new Gson();
            MemoryCache.participants = gson.fromJson(json, new TypeToken<List<Participant>>() {
            }.getType());
            return participants;
        }
    }

    public static void setResult(String participant, long time) {
        results.put(participant, time);
    }

    public static void resetResults() {
        results.clear();
    }

    public static boolean isRandomShift(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(RANDOM_SHIFT, false);
    }


}
