package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Tab2 extends Fragment {

    private ArrayList<Group> groups;
    View view;
    ListView sampleListView;
    ListAdapter group_adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groups = new ArrayList<>();
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        sampleListView = (ListView) view.findViewById(R.id.listView);

        NotesFeedSession sessionHandler = new NotesFeedSession(getActivity());
        GroupsConnection group_async = new GroupsConnection();
        group_async.execute(getContext().getSharedPreferences(sessionHandler.SHARED_PREFERENCES, getActivity().MODE_PRIVATE).getString(sessionHandler.SESSION_USER_ID, null));

        group_adapter = new ListAdapter(getActivity(), R.layout.group_list, groups);

        return view;
    }

//    Asynchronous task. Adding groups to the ArrayList groups

    public class GroupsConnection extends AsyncTask<String, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(String... params) {
            String user_id = "user_id=" + params[0];
            String link = "" + NotesFeedSession.SERVER_ADDRESS + "notesfeed/getgroups.php?" + user_id;
            byte[] user_id_bytes = user_id.getBytes();

            System.out.println("Sending data");

            Map<String, String> group = new LinkedHashMap<>();

            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                System.out.println("Data sent");

                Reader server_output = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder response = new StringBuilder();

                for (int i; (i = server_output.read()) >= 0; ) {
                    response.append((char) i);
                }

                System.out.println(response + "");

                urlConnection.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray group_id_array = jsonResponse.getJSONArray("group_id");
                JSONArray group_name_array = jsonResponse.getJSONArray("group_name");

                for (int i = 0; i < group_name_array.length(); i++) {
                    group.put(group_id_array.getString(i), group_name_array.getString(i));
                }

                for (Map.Entry<String, String> keyValues : group.entrySet()) {
                    System.out.println(keyValues.getKey() + ": " + keyValues.getValue());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return group;
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            super.onPostExecute(stringStringMap);

            for (Map.Entry<String, String> groups_received : stringStringMap.entrySet()) {
                Group g = new Group(groups_received.getKey(), groups_received.getValue());
                User group_member = new User("1", "Shaun");
                User group_member2 = new User("2", "Bendrhick");
                User group_member3 = new User("3", "Kat");

                g.addGroup_member(group_member);
                g.addGroup_member(group_member2);
                g.addGroup_member(group_member3);

                groups.add(g);
            }

            sampleListView.setAdapter(group_adapter);
            group_adapter.notifyDataSetChanged();

        }
    }

}