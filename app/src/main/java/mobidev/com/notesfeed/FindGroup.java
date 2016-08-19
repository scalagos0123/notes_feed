package mobidev.com.notesfeed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Map;

public class FindGroup extends AppCompatActivity {

    ArrayList<Group> groupList;
    ArrayList<Group> searchResults;
    ArrayList<Group> existingGroups;
    ListView groupsList;
    ListAdapter groupsList_adapter;
    ListAdapter results_adapter;
    NotesFeedSession n;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);

        n = new NotesFeedSession(this);
        groupList = new ArrayList<>();
        searchResults = new ArrayList<>();
        this.c = this;

        Bundle bundle = getIntent().getExtras().getBundle("groupBundle");
        existingGroups = (ArrayList<Group>) bundle.getSerializable("existingGroup");

        GroupsConnection groupsConnection = new GroupsConnection();
        groupsConnection.execute(n.getUserId());

        EditText inputBox = (EditText) findViewById(R.id.search_input);
        ImageButton back = (ImageButton) findViewById(R.id.backbtn) ;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
                System.out.println("Searching now: " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        groupsList = (ListView) findViewById(R.id.group_list);
        groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Group selectedGroup = (Group) parent.getItemAtPosition(position);

                boolean status = memberCheck(selectedGroup);

                if (!status) {
                    addAsMemberDialogBox(selectedGroup);
                } else {
                    Intent groupActivity = new Intent(c, GroupActivity.class);
                    groupActivity.putExtra("selectedGroup", selectedGroup);
                    startActivity(groupActivity);
                }

            }
        });

//        adapter for the original list
        groupsList_adapter = new ListAdapter(this, R.layout.group_list, groupList);

//        adapter for the results
        results_adapter  = new ListAdapter(this, R.layout.group_list, searchResults);

    }

    private boolean memberCheck(Group selectedGroup) {
        boolean status = false;
        int i = 0;

        while (status == false && i < existingGroups.size()) {
            if (existingGroups.get(i).getGroup_id().equals(selectedGroup.getGroup_id())) {
                status = true;
            } else {
                i++;
            }
        }

        return status;
    }

    private void performSearch(String input) {
        if (input.length() == 0) {
            groupsList.setAdapter(groupsList_adapter);
        } else if (input.length() > 0) {
            searchResults.clear();
            for (int i = 0; i < groupList.size(); i++) {
                if (groupList.get(i).getGroup_name().contains(input)) {
                    searchResults.add(groupList.get(i));
                }
            }
            groupsList.setAdapter(results_adapter);
        }
    }

    private void addAsMemberDialogBox(final Group selectedGroup) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to join " + selectedGroup.getGroup_name() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Call add to on of the group members asynctask
                AddToGroup add = new AddToGroup();
                add.execute(selectedGroup.getGroup_id());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private class AddToGroup extends AsyncTask<String, Void, Boolean> {

        String groupId;
        String userId;

        @Override
        protected Boolean doInBackground(String... params) {
            boolean status = false;
            groupId = params[0];
            userId = n.getUserId();
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/addmember.php";
            String data = "group_id=" + groupId + "&user_id=" + userId;
            byte[] dataBytes = data.getBytes();

            System.out.println("adding member");
            try {
                URL url = new URL(link);
                HttpURLConnection addMemberConnection = (HttpURLConnection) url.openConnection();
                addMemberConnection.setRequestMethod("POST");
                addMemberConnection.setDoOutput(true);
                addMemberConnection.getOutputStream().write(dataBytes);

                System.out.println("Data sent");

                BufferedReader r = new BufferedReader(new InputStreamReader(addMemberConnection.getInputStream()));
                StringBuffer response = new StringBuffer();

                for (int i; (i = r.read()) >= 0;) {
                    response.append((char) i);
                }

                if (response.toString().equals("member added")) {
                    status = true;
                } else {
                    status = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean == true) {
                setResult(95);
                finish();
            }
        }
    }

    protected class GroupsConnection extends AsyncTask<String, Void, ArrayList<Map<String, String>>> {

        @Override
        protected ArrayList<Map<String, String>> doInBackground(String... params) {
            String user_id = "user_id=" + params[0];
            String link = "" + NotesFeedSession.SERVER_ADDRESS + "notesfeed/getgroups_find.php?" + user_id;
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
                groupList.add(g);
                position++;
            }

            groupsList.setAdapter(groupsList_adapter);

        }
    }

}
