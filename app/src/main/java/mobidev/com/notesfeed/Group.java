package mobidev.com.notesfeed;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun on 7/24/2016.
 */
public class Group {

    public ArrayList<String> groups;
    public ArrayList<Integer> groupIds;
    public final int GET_GROUPS = 1;
    public final int ADD_GROUP = 2;
    public final int DELETE_GROUP = 3;
    private Context context;

    public Group(Context context) {
        this.groups = new ArrayList<String>();
        this.groupIds = new ArrayList<Integer>();
        this.context = context;
    }

    private void getUserGroupsFromServer(String userId) {
        GroupsConnection userGroups = new GroupsConnection(context, this);
        userGroups.execute(userId);
    }

    public void getGroupsByUser(String userId) {

    }

}
