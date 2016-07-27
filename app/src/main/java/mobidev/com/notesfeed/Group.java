package mobidev.com.notesfeed;

import android.content.Context;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shaun on 7/24/2016.
 */
public class Group {

    public Map<String, String> groups;
    public final int GET_GROUPS = 1;
    public final int ADD_GROUP = 2;
    public final int DELETE_GROUP = 3;
    private Context context;

    public Group(Context context, String userId) {
        this.groups = new LinkedHashMap<>();
        this.context = context;
    }

    private Map<String, String> getUserGroupsFromServer(String userId) {
        GroupsConnection userGroups = new GroupsConnection();
        userGroups.execute(userId);
        return groups;
    }

    public void getGroupsByUser(String userId) {
        getUserGroupsFromServer(userId);
    }

}
