package mobidev.com.notesfeed;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shaun on 7/24/2016.
 */
public class Group {

    public Map<String, String> groups;
    private Context context;
    private String group_id;
    private String group_name;
    private Map<Integer, String> group_members;

    public Group() {
        this.group_members = new HashMap<>();
    }

//    private Map<String, String> getUserGroupsFromServer(String userId) {
//        GroupsConnection userGroups = new GroupsConnection();
//        userGroups.execute(userId);
//        return groups;
//    }
//
//    public void getGroupsByUser(String userId) {
//        getUserGroupsFromServer(userId);
//    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Map<Integer, String> getGroup_members() {
        return group_members;
    }

    public void setGroup_members(Map<Integer, String> group_members) {
        this.group_members = group_members;
    }

    public void addGroup_member(int userId, String group_member_name) {
        group_members.put(userId, group_member_name);
    }

    public int getGroupSize() {
        return group_members.size();
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
