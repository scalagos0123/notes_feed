package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
//    private TextView save_button;

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

        groups.add(new Group("1", "Dummy group #1"));
        groups.add(new Group("2", "Dummy group #2"));

        NotesFeedSession sessionHandler = new NotesFeedSession(getActivity());
//        GroupsConnection group_async = new GroupsConnection();
//        group_async.execute(getContext().getSharedPreferences(sessionHandler.SHARED_PREFERENCES, getActivity().MODE_PRIVATE).getString(sessionHandler.SESSION_USER_ID, null));

        group_adapter = new ListAdapter(getActivity(), R.layout.group_list, groups);
        sampleListView.setAdapter(group_adapter);

        sampleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group itemOnList = (Group) parent.getItemAtPosition(position);
                System.out.println(itemOnList.getGroup_name());

                Intent groupActivity = new Intent(getContext(), GroupActivity.class);
                groupActivity.putExtra("selectedGroup", itemOnList);
                startActivity(groupActivity);
            }
        });

        return view;
    }

//    Asynchronous task. Adding groups to the ArrayList groups

    protected class GroupsConnection extends AsyncTask<String, Void, ArrayList<Map<String, String>>> {

        @Override
        protected ArrayList<Map<String, String>> doInBackground(String... params) {
            String user_id = "user_id=" + params[0];
            String link = "" + NotesFeedSession.SERVER_ADDRESS + "notesfeed/getgroups.php?" + user_id;
            byte[] user_id_bytes = user_id.getBytes();

            System.out.println("Sending data");

            ArrayList<Map<String, String>> receivedFromServer = new ArrayList<>();

            Map<String, String> group = new LinkedHashMap<>();
            Map<String, String> group_members = new LinkedHashMap<>();

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
                JSONArray group_total_members = jsonResponse.getJSONArray("group_total_members");

//                JSONArray group_members_received = jsonResponse.getJSONArray("group_members");

                for (int i = 0; i < group_name_array.length(); i++) {
                    group.put(group_id_array.getString(i), group_name_array.getString(i));
                    group_members.put(group_id_array.getString(i), group_total_members.getString(i));
                }

                for (Map.Entry<String, String> keyValues : group.entrySet()) {
                    System.out.println(keyValues.getKey() + ": " + keyValues.getValue());
                }

                receivedFromServer.add(group);
                receivedFromServer.add(group_members);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return receivedFromServer;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String, String>> stringStringMap) {
            super.onPostExecute(stringStringMap);

            int[] group_members = new int[100];
            int position = 0;

            for (Map.Entry<String, String> groups_total : stringStringMap.get(1).entrySet()) {
                group_members[position] = Integer.parseInt(groups_total.getValue());
                position++;
            }

            position = 0;

            for (Map.Entry<String, String> groups_received : stringStringMap.get(0).entrySet()) {
                Group g = new Group(groups_received.getKey(), groups_received.getValue());
                g.setGroupTotalMembers(group_members[position]);
                groups.add(g);
                position++;
            }

            sampleListView.setAdapter(group_adapter);

        }
    }

}