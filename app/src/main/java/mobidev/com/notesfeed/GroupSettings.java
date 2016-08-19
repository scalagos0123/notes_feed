package mobidev.com.notesfeed;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shaun on 8/2/2016.
 */
public class GroupSettings extends Fragment {

    private Group g = null;

    private EditText groupName;
    private Button saveButton;
    private Switch privacySwitch;
    private CardView addMember;
    private CardView leaveGroup;
    private CardView deleteGroup;
    private ListView memberList;
    private TextView privacySwitchText;
    private NotesFeedSession n;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupActivity activity = (GroupActivity) getActivity();
        g = activity.getGroupData();

        n = new NotesFeedSession(this.getContext());

        GetOptions settingsAsynctask = new GetOptions();
        settingsAsynctask.execute(g.getGroup_id());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_settings_tab, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        groupName = (EditText) view.findViewById(R.id.group_name);
        saveButton = (Button) view.findViewById(R.id.save_button);
        privacySwitch = (Switch) view.findViewById(R.id.privacy_switch);
        addMember = (CardView) view.findViewById(R.id.add_member);
        leaveGroup = (CardView) view.findViewById(R.id.leave_group);
        deleteGroup = (CardView) view.findViewById(R.id.delete_group);
        memberList = (ListView) view.findViewById(R.id.member_list);
        privacySwitchText = (TextView) view.findViewById(R.id.privacy_switch_text);
    }

    private void performModeration(String groupModeratorId) {
        groupName.setText(g.getGroup_name());
        final String publicMessage = "This group is public";
        final String privateMessage = "This group is private";

        if (g.getPrivacy() == 0) {
            privacySwitchText.setText(publicMessage);
            privacySwitch.setChecked(false);
        } else {
            privacySwitchText.setText(privateMessage);
            privacySwitch.setChecked(true);
        }

        leaveGroup.setOnClickListener(clickListeners);

        if (n.getUserId().equals(groupModeratorId)) {
            groupName.setFocusable(true);
            groupName.setFocusableInTouchMode(true);
            addMember.setVisibility(View.VISIBLE);
            deleteGroup.setVisibility(View.VISIBLE);
            privacySwitch.setVisibility(View.VISIBLE);

            privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        privacySwitchText.setText(privateMessage);
//                        call asynctask with true as parameter
                    } else {
                        privacySwitchText.setText(publicMessage);
//                        call asynctask with false as parameter
                    }
                }
            });

            groupName.setOnClickListener(clickListeners);
            addMember.setOnClickListener(clickListeners);
            deleteGroup.setOnClickListener(clickListeners);
            saveButton.setOnClickListener(clickListeners);
            memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    call dialog box here containing moderator actions
                }
            });
        }
    }

    private void showDeleteGroupDialogBox () {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this.getContext());
        deleteDialog.setMessage("Are you sure? You can't undo this action.");
        deleteDialog.setTitle("Deleting group");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                call delete group asynctask
            }
        });

        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        deleteDialog.show();
    }

    private void showLeaveGroupDialogBox() {
        AlertDialog.Builder leaveDialog = new AlertDialog.Builder(this.getContext());
        leaveDialog.setMessage("Do you really want to leave?");
        leaveDialog.setTitle("Leaving group");
        leaveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                call asynctask deleting user_id in the current group
            }
        });
        leaveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        leaveDialog.show();
    }

    View.OnClickListener clickListeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.group_name:
                    saveButton.setVisibility(View.VISIBLE);
                    break;
                case R.id.add_member:
//                    call add member activity here
                    break;
                case R.id.delete_group:
//                    call delete group dialog box here
                    showDeleteGroupDialogBox();
                    break;
                case R.id.leave_group:
//                    call leave group dialog box here
                    showLeaveGroupDialogBox();
                    break;
                case R.id.save_button:
//                    call update group name asynctask here
                    break;
            }
        }
    };

    private class GetOptions extends AsyncTask<String, Void, Boolean> {

        StringBuilder toString;

        @Override
        protected Boolean doInBackground(String... params) {
            boolean status = false;

            String groupId = params[0];
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/getgroupmembers.php?group_id=" + groupId;

            System.out.println("Sending group");
            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                System.out.println("Group sent");

                BufferedReader receive = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                toString = new StringBuilder();

                for (int i; (i = receive.read()) >= 0;) {
                    toString.append((char) i);
                }

                System.out.println(toString.toString());

                status = true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                try {

                    JSONObject groupSettings = new JSONObject(toString.toString());
                    JSONArray userId = groupSettings.getJSONArray("user_id");
                    JSONArray userName = groupSettings.getJSONArray("user_name");
                    JSONArray groupPrivacy = groupSettings.getJSONArray("group_privacy");
                    JSONArray groupModerator = groupSettings.getJSONArray("group_moderator");

                    for (int i = 0; i < userName.length(); i++) {
                        User member = new User(userId.getString(i), userName.getString(i));
                        g.addGroup_member(member);

                        if (member.getUserId().equals(groupModerator.getString(0))) {
                            g.setGroup_moderator(member);
                        }
                    }

                    g.setPrivacy(groupPrivacy.getInt(0));

                    performModeration(g.getGroup_moderator().getUserId());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else  {
                System.out.println("Error in connection");
            }
        }
    }
}
