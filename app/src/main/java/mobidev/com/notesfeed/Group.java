package mobidev.com.notesfeed;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shaun on 7/24/2016.
 */
public class Group implements Serializable {

    private String group_id;
    private String group_name;
    ArrayList<User> group_members;
    private int groupTotalMembers;

    public Group(String group_id, String group_name) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.group_members = new ArrayList<User>();
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public ArrayList<User> getGroup_members() {
        return group_members;
    }

    public void addGroup_member(User groupMember) {
        this.group_members.add(groupMember);
    }

    public int getGroupTotalMembers() {
        return this.groupTotalMembers;
    }

    public void setGroupTotalMembers(int totalMembers) {
        this.groupTotalMembers = totalMembers;
    }

    public String getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
