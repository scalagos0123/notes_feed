package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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
    LayoutInflater inflater;
    ViewGroup container;
    Context context;

    int lastGroupId;
    NotesFeedSession sessionHandler;
    Group addedGroup;

//    private TextView save_button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groups = new ArrayList<>();
        context = this.getContext();
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        view.setTag("tab2");
        sampleListView = (ListView) view.findViewById(R.id.listView);
        FloatingActionButton createGroup = (FloatingActionButton) view.findViewById(R.id.group_create);
        FloatingActionButton findGroup = (FloatingActionButton) view.findViewById(R.id.group_find);

        findGroup.setOnClickListener(buttonAction);
        createGroup.setOnClickListener(buttonAction);

//        groups.add(new Group("1", "Dummy group #1"));
//        groups.add(new Group("2", "Dummy group #2"));
        sessionHandler = new NotesFeedSession(getActivity());
        GroupsConnection group_async = new GroupsConnection();
        group_async.execute(getContext().getSharedPreferences(sessionHandler.SHARED_PREFERENCES, getActivity().MODE_PRIVATE).getString(sessionHandler.SESSION_USER_ID, null));

        group_adapter = new ListAdapter(getActivity(), R.layout.group_list, groups);
//        sampleListView.setAdapter(group_adapter);

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

    private void showCreateGroupDialog() {
        AlertDialog.Builder createGroup = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getLayoutInflater(null);
        View dialogLayout = inflater.inflate(R.layout.content_create_group, null);

        createGroup.setView(dialogLayout);

        final EditText input = (EditText) dialogLayout.findViewById(R.id.group_name);

        createGroup.setNegativeButton("Cancel", null);
        createGroup.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Group g = new Group (lastGroupId + "", input.getText().toString());
                User automaticMember = new User(sessionHandler.getUserId(), sessionHandler.getUserFullName());
                g.setGroupTotalMembers(1);
                g.addGroup_member(automaticMember);
                g.setGroup_moderator(automaticMember);

                CreateGroupAsyncTask c = new CreateGroupAsyncTask();
                c.execute(g);
            }
        });

        createGroup.show();

    }

    private View.OnClickListener buttonAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.group_create:
                    showCreateGroupDialog();
                    break;
                case R.id.group_find:
                    Intent group_find = new Intent(context, FindGroup.class);
                    startActivity(group_find);
                    break;
            }
        }
    };

    public void makeResponse() {
        Toast.makeText(this.getContext(), "Group created", Toast.LENGTH_SHORT).show();
    }

    private class CreateGroupAsyncTask extends AsyncTask<Group, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Group... params) {

            String groupName = params[0].getGroup_name();
            String moderator = params[0].getGroup_moderator().getUserId();

            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/creategroup.php";

            StringBuffer sb = new StringBuffer();

            Map<String, String> m = new LinkedHashMap<>();
            m.put("groupName", groupName);
            m.put("moderator", moderator);

            for (Map.Entry<String, String> values : m.entrySet()) {
                sb.append(values.getKey());
                sb.append("=");
                sb.append(values.getValue());
                sb.append("&");
            }

            byte[] sendBytes = sb.toString().getBytes();
            boolean response = false;

            try {

                URL url = new URL(link);
                HttpURLConnection sendData = (HttpURLConnection) url.openConnection();
                sendData.setRequestMethod("POST");
                sendData.getOutputStream().write(sendBytes);

                BufferedReader r = new BufferedReader(new InputStreamReader(sendData.getInputStream()));
                StringBuffer responseOutput = new StringBuffer();

                for (int c; (c = r.read()) >= 0; ) {
                    responseOutput.append((char) c);
                }

                if (responseOutput.toString().equals("group added")) {
                    response = true;
                    addedGroup = params[0];
                    System.out.println(addedGroup.getGroup_members().get(0).getName());
                } else {
                    System.out.println(responseOutput.toString());
                    response = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean == true) {
                groups.add(addedGroup);
                group_adapter.notifyDataSetChanged();
                makeResponse();
            }
        }
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

            if (groups.size() == 0) {
                lastGroupId = 0;
            } else {
                lastGroupId = Integer.parseInt(groups.get(groups.size() - 1).getGroup_id()) + 1;
            }

            System.out.println(lastGroupId);

        }
    }
}