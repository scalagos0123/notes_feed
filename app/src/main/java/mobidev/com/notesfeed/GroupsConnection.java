package mobidev.com.notesfeed;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Shaun on 7/24/2016.
 */
public class GroupsConnection extends AsyncTask<String, Void, ArrayList<String>> {

    private Context groupsContext;
    private Group g;

    public GroupsConnection(Context context, Group g) {
        this.groupsContext = context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        return null;
    }
}
