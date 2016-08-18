package mobidev.com.notesfeed;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
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
    ListView groupsList;
    ListAdapter groupsList_adapter;
    ListAdapter results_adapter;
    NotesFeedSession n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);

        n = new NotesFeedSession(this);
        groupList = new ArrayList<>();
        searchResults = new ArrayList<>();

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

//        adapter for the original list
        groupsList_adapter = new ListAdapter(this, R.layout.group_list, groupList);

//        adapter for the results
        results_adapter  = new ListAdapter(this, R.layout.group_list, searchResults);

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
                groupList.add(g);
                position++;
            }

            groupsList.setAdapter(groupsList_adapter);

        }
    }

}
