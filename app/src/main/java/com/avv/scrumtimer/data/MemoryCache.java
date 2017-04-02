package com.avv.scrumtimer.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avv.scrumtimer.participants.Participant;
import com.avv.scrumtimer.participants.ParticipantGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angelvazquez on 18/5/16.
 */
public class MemoryCache {

    public static final String SELECTED_GROUP = "Selected_Group";
    public static final String GROUPS_PARTICIPANT = "Groups_Participant";
    public static final String RANDOM_SHIFT = "randomshift";

    public static List<ParticipantGroup> groupsParticipant;
    public static List<Participant> participants;
    public static HashMap<String, Long> results = new HashMap<String, Long>();
    public static String selectedGroupName = null;

    public static void initLoadData(Context context){
        List<ParticipantGroup> groups = getGroupsParticipant(context);
        for(ParticipantGroup pg:groups){
            if(pg.isSelected()){
                selectedGroupName = pg.getName();
                break;
            }
        }
    }

    public static boolean isGroupSelected(){
        return selectedGroupName!=null;
    }

    public static void saveSelectedGroup(String groupName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SELECTED_GROUP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTED_GROUP, groupName);
        editor.commit();

        int index = getGroup(groupName, MemoryCache.groupsParticipant);
        for(int i=0;i<MemoryCache.groupsParticipant.size();i++){
            MemoryCache.groupsParticipant.get(i).setSelected(false);
        }
        MemoryCache.groupsParticipant.get(index).setSelected(true);
        saveGroupsParticipant(MemoryCache.groupsParticipant, context);
    }

    public static int getGroup (String groupName, List<ParticipantGroup> participantGroupList) {
        for(int i=0;i<participantGroupList.size();i++){
            if(participantGroupList.get(i).getName().equals(groupName))
            return i;
        }
        return -1;
    }

    public static void saveParticipants(String groupName, List<Participant> participants, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUPS_PARTICIPANT, Context.MODE_PRIVATE);

        String json = sharedPreferences.getString(GROUPS_PARTICIPANT, "");
        Gson gson = new Gson();
        MemoryCache.groupsParticipant = gson.fromJson(json, new TypeToken<List<ParticipantGroup>>() {
        }.getType());

        int index = getGroup(groupName, MemoryCache.groupsParticipant);
        MemoryCache.groupsParticipant.get(index).setParticipantList(participants);

        json = gson.toJson(MemoryCache.groupsParticipant);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GROUPS_PARTICIPANT, json);
        editor.commit();

        MemoryCache.participants = participants;
    }

    public static List<Participant> getParticipants(String groupName, Context context) {
        if(groupName==null){
            participants = new ArrayList<Participant>();
            return participants;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUPS_PARTICIPANT, Context.MODE_PRIVATE);

        String json = sharedPreferences.getString(GROUPS_PARTICIPANT, "");

        if ("".equals(json)) {
            participants = new ArrayList<Participant>();
            return participants;
        } else {
            Gson gson = new Gson();
            MemoryCache.groupsParticipant = gson.fromJson(json, new TypeToken<List<ParticipantGroup>>() {
            }.getType());
            int index = getGroup(groupName, MemoryCache.groupsParticipant);
            MemoryCache.participants = groupsParticipant.get(index).getParticipantList()!=null?groupsParticipant.get(index).getParticipantList():new ArrayList<Participant>();

            return participants;
        }
    }

    public static void saveGroupsParticipant(List<ParticipantGroup> participantGroupLists, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUPS_PARTICIPANT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(participantGroupLists);

        editor.putString(GROUPS_PARTICIPANT, json);
        editor.commit();

        MemoryCache.groupsParticipant = participantGroupLists;
    }

    public static List<ParticipantGroup> getGroupsParticipant(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUPS_PARTICIPANT, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(GROUPS_PARTICIPANT, "");
        if ("".equals(json)) {
            groupsParticipant = new ArrayList<ParticipantGroup>();
            return groupsParticipant;
        } else {
            Gson gson = new Gson();
            MemoryCache.groupsParticipant = gson.fromJson(json, new TypeToken<List<ParticipantGroup>>() {
            }.getType());
            return groupsParticipant;
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
