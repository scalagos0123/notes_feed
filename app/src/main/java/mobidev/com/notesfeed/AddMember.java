package mobidev.com.notesfeed;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shaun on 8/20/2016.
 */
public class AddMember extends AppCompatActivity {

    private MemberAdapter userDatabase_adapter;
    private MemberAdapter results_adapter;
    private ListView memberList;
    private EditText inputBox;
    private ImageButton back;
    private ArrayList<User> members;
    private ArrayList<User> userDatabase;
    private ArrayList<User> results;
    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);

        userDatabase = new ArrayList<>();
        results = new ArrayList<>();

        Bundle getBundle = getIntent().getExtras().getBundle("usersBundle");
        User moderator = (User) getBundle.getSerializable("moderator");
        groupId = getBundle.getString("groupId");
        members = (ArrayList<User>) getBundle.getSerializable("members");
        members.add(moderator);

        initializeViews();

        GetUsers g = new GetUsers();
        g.execute();

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
    }

    private void performSearch(String input) {
        if (input.length() == 0) {
            memberList.setAdapter(userDatabase_adapter);
        } else if (input.length() > 0) {
            results.clear();

            for (int i = 0; i < userDatabase.size(); i++) {
                if (input.equals(userDatabase.get(i).getName())) {
                    results.add(userDatabase.get(i));
                }
            }

            memberList.setAdapter(results_adapter);

        }
    }

    private void initializeViews() {
        inputBox = (EditText) findViewById(R.id.search_input);
        back = (ImageButton) findViewById(R.id.backbtn);
        inputBox.setHint("Type a person's name");
        memberList = (ListView) findViewById(R.id.group_list);
        userDatabase_adapter = new MemberAdapter(this, R.layout.member_list, userDatabase);
        results_adapter = new MemberAdapter(this, R.layout.member_list, results);

        memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = (User) parent.getItemAtPosition(position);
                showAddMemberDialogBox(selectedUser);
            }
        });
    }

    private boolean filterMembers() {
        boolean status = false;

        for (int i = 0; i < members.size(); i++) {
           for (int j = 0; j < userDatabase.size(); j++) {
               if (members.get(i).getUserId().equals(userDatabase.get(j).getUserId())) {
                   userDatabase.remove(j);
               }
           }
        }

        status = true;
        return status;
    }

    private void showAddMemberDialogBox(final User selectedUser) {
        AlertDialog.Builder addMember = new AlertDialog.Builder(this);
        addMember.setMessage("Do you want to add " + selectedUser.getName() + "?");
        addMember.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddMemberToGroup a = new AddMemberToGroup();
                a.execute(selectedUser.getUserId());
            }
        });

        addMember.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        addMember.show();
    }

    private class AddMemberToGroup extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String user = "";
            String userId = params[0];
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/addmember.php";
            String body = "group_id=" + groupId + "&user_id=" + userId;
            byte[] bodyBytes = body.getBytes();

            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(bodyBytes);

                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();

                for (int i; (i = r.read()) >= 0;) {
                    response.append((char) i);
                }

                if (response.toString().equals("member added")) {
                    user = "member added";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return user;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("member added")) {
                System.out.println(s);
                finish();
            } else {
                System.out.println(s);
            }
        }
    }

    private class GetUsers extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/getallusers.php";
            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);

                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder responseString = new StringBuilder();

                for (int i; (i = r.read()) >= 0;) {
                    responseString.append((char) i);
                }

                response = responseString.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject users = new JSONObject(s);
                JSONArray user_id = users.getJSONArray("user_id");
                JSONArray user_name = users.getJSONArray("user_name");

                for (int i = 0; i < user_id.length(); i++) {
                    userDatabase.add(new User(user_id.getString(i), user_name.getString(i)));
                }

                if (filterMembers()) {
                    memberList.setAdapter(userDatabase_adapter);
                }

            } catch (Exception e) {
                System.out.println(s);
            }
        }
    }
}
