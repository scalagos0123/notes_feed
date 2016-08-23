package mobidev.com.notesfeed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    private TextView moderatorName;
    private NotesFeedSession n;
    private User selectedUser;
    private MemberAdapter memberList_adapter;
    private StringBuilder toString;
    AlertDialog dialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupActivity activity = (GroupActivity) getActivity();
        g = activity.getGroupData();

        n = new NotesFeedSession(this.getContext());

        refreshOptions();

        memberList_adapter = new MemberAdapter(this.getContext(), R.layout.member_list, g.getGroup_members());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_settings_tab, container, false);

        initializeViews(view);

        return view;
    }

    public void addMember(User addedMember) {
        g.getGroup_members().add(addedMember);
        memberList_adapter.notifyDataSetChanged();
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
        moderatorName = (TextView) view.findViewById(R.id.moderator_name);
    }

    private void performModeration(String groupModeratorId) {
        groupName.setText(g.getGroup_name());
        final String publicMessage = "This group is public";
        final String privateMessage = "This group is private";

        if (g.getPrivacy() == 0) {
            privacySwitchText.setText(publicMessage);
            privacySwitch.setChecked(false);
        } else if (g.getPrivacy() == 1) {
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
            memberList.setOnItemClickListener(onItemClick);

            privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        privacySwitchText.setText(privateMessage);
//                        call asynctask with true as parameter
                        UpdateGroup u = new UpdateGroup();
                        u.execute("true");
                    } else {
                        privacySwitchText.setText(publicMessage);
//                        call asynctask with false as parameter
                        UpdateGroup u = new UpdateGroup();
                        u.execute("false");
                    }
                }
            });

            groupName.setOnClickListener(clickListeners);
            addMember.setOnClickListener(clickListeners);
            saveButton.setOnClickListener(clickListeners);
            deleteGroup.setOnClickListener(clickListeners);
            memberList.setOnItemClickListener(onItemClick);

        } else {
            groupName.setFocusable(false);
            groupName.setFocusableInTouchMode(false);
            addMember.setVisibility(View.GONE);
            deleteGroup.setVisibility(View.GONE);
            privacySwitch.setVisibility(View.GONE);
            memberList.setOnItemClickListener(null);
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
                GroupActions g = new GroupActions();
                g.execute(1);
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
                GroupActions g = new GroupActions();
                g.execute(0);
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

    private void confirmUserRemoval(User selectedUser) {
        AlertDialog.Builder removal = new AlertDialog.Builder(this.getContext());
        removal.setTitle("Confirm removal");
        removal.setMessage("Are you sure you want to remove " + selectedUser.getName() + "?");
        final AlertDialog moderatorOptionsDialog = this.dialog;
        removal.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                call removal of member asynctask
                moderatorOptionsDialog.dismiss();
                ModeratorActions m = new ModeratorActions();
                m.execute(1);
            }
        });

        removal.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        removal.show();
    }

    private void showMemberOptions(User selectedUser) {
        this.selectedUser = selectedUser;
        AlertDialog.Builder memberOptions = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View v = inflater.inflate(R.layout.memberaction_dialog, null);

        RelativeLayout removeMember = (RelativeLayout) v.findViewById(R.id.remove_member);
        RelativeLayout setModerator = (RelativeLayout) v.findViewById(R.id.set_moderator);

        removeMember.setOnClickListener(clickListeners);
        setModerator.setOnClickListener(clickListeners);

        memberOptions.setView(v);
        this.dialog = memberOptions.create();
        this.dialog.show();
    }

    private void confirmModeratorChange(final User selectedUser) {
        AlertDialog.Builder moderatorChange = new AlertDialog.Builder(this.getContext());
        moderatorChange.setTitle("Confirm moderator change");
        moderatorChange.setMessage("Are you sure you want to make " + selectedUser.getName() + " moderator?");
        final AlertDialog moderatorOptionsDialog = this.dialog;
        moderatorChange.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Asynctask moderator actions with flag 0
                moderatorOptionsDialog.dismiss();
                ModeratorActions m = new ModeratorActions();
                m.execute(0);
            }
        });
        moderatorChange.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        moderatorChange.show();
    }

    public void refreshOptions () {
        GetOptions settingsAsynctask = new GetOptions();
        settingsAsynctask.execute(g.getGroup_id());
    }

    public void addSettings() {
        g.getGroup_members().clear();
        memberList.setAdapter(memberList_adapter);

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
                    g.getGroup_members().remove(i);
                }
            }

            g.setPrivacy(groupPrivacy.getInt(0));
            performModeration(g.getGroup_moderator().getUserId());
            moderatorName.setText(g.getGroup_moderator().getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        memberList_adapter.notifyDataSetChanged();
    }

    View.OnClickListener clickListeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set_moderator:
                    confirmModeratorChange(selectedUser);
                    break;
                case R.id.remove_member:
                    confirmUserRemoval(selectedUser);
                    break;
                case R.id.group_name:
                    saveButton.setVisibility(View.VISIBLE);
                    break;
                case R.id.add_member:
//                    call add member activity here
                    Bundle b = new Bundle();
                    b.putSerializable("members", g.getGroup_members());
                    b.putSerializable("moderator", g.getGroup_moderator());
                    b.putString("groupId", g.getGroup_id());

                    Intent i = new Intent (getContext(), AddMember.class);
                    i.putExtra("usersBundle", b);
                    startActivityForResult(i, 100);
                    break;
                case R.id.delete_group:
                    showDeleteGroupDialogBox();
                    break;
                case R.id.leave_group:
                    showLeaveGroupDialogBox();
                    break;
                case R.id.save_button:
                    UpdateGroup u = new UpdateGroup();
                    u.execute(groupName.getText().toString());
                    saveButton.setVisibility(View.GONE);
                    break;
            }
        }
    };

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            User selectedUser = (User) parent.getItemAtPosition(position);
            showMemberOptions(selectedUser);
        }
    };

    private class ModeratorActions extends AsyncTask<Integer, Void, Boolean> {

        String result = "";

        @Override
        protected Boolean doInBackground(Integer... params) {
            boolean status = false;
            int flag = params[0];
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/moderatoractions.php";
            String body = "group_id=" + g.getGroup_id() + "&user_id=" + selectedUser.getUserId() + "&flag=" + flag;
            byte[] bodyBytes = body.getBytes();

            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(bodyBytes);

                BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder responseString = new StringBuilder();

                for (int i; (i = response.read()) >= 0;) {
                    responseString.append((char) i);
                }

                System.out.println(responseString.toString());

                if (responseString.toString().equals("moderator changed")) {
                    result = responseString.toString();
                    status = true;
                } else if (responseString.toString().equals("removed member")) {
                    result = responseString.toString();
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

            if (aBoolean && result.equals("moderator changed")) {
                g.setGroup_moderator(selectedUser);
                moderatorName.setText(g.getGroup_moderator().getName());
                performModeration(g.getGroup_moderator().getUserId());
            } else if (aBoolean && result.equals("removed member")) {
                if (removeUser()) {
                    performModeration(g.getGroup_moderator().getUserId());
                }
            }
        }

        private boolean removeUser() {
            boolean removed = false;
            int i = 0;

            while (!removed) {
                if (selectedUser.getUserId().equals(g.getGroup_members().get(i).getUserId())) {
                    g.getGroup_members().remove(i);
                    memberList_adapter.notifyDataSetChanged();
                    removed = true;
                } else {
                    i++;
                }
            }
            return removed;
        }
    }

    private class GetOptions extends AsyncTask<String, Void, Boolean> {

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
                addSettings();

            } else  {
                System.out.println("Error in connection");
            }
        }
    }

    private class UpdateGroup extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            String selectedUpdate = params[0];
            int flag = 0;
            int privacyCondition = 0;
            String body = "";
            byte[] bodyBytes = null;
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/updategroup.php";
            boolean status = false;

            if (selectedUpdate.equals("true") || selectedUpdate.equals("false")) {
                flag = 1;

                if (selectedUpdate.equals("true")) {
                    privacyCondition = 1;
                } else if (selectedUpdate.equals("false")) {
                    privacyCondition = 0;
                }

                body = "group_id=" + g.getGroup_id() + "&privacy=" + privacyCondition + "&flag=" + flag;
                bodyBytes = body.getBytes();

            } else {
                flag = 0;
                body = "group_id=" + g.getGroup_id() + "&group_name=" + selectedUpdate + "&flag=" + flag;
                bodyBytes = body.getBytes();

            }

            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(bodyBytes);

                BufferedReader getResponse = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();

                for (int i; (i = getResponse.read()) >= 0;) {
                    response.append((char) i);
                }

                System.out.println(response.toString());

                if (response.toString().equals("privacy updated") || response.toString().equals("group name updated")) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }
    }

    private class GroupActions extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            boolean status = false;
            int flag = params[0];
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/groupactions.php";
            String body = "";
            byte[] bodyBytes = null;

            int newModerator = 0;

            if (flag == 0) {

                if (n.getUserId().equals(g.getGroup_moderator().getUserId())) {
                    for (int i = 0; i < g.getGroup_members().size(); i++) {
                        if (n.getUserId().equals(g.getGroup_members().get(i).getUserId())) {
                            g.getGroup_members().remove(i);
                        }
                    }

                    if (g.getGroup_members().size() == 0) {
                        flag = 1;
                    } else {
                        newModerator = Integer.parseInt(g.getGroup_members().get(0).getUserId());
                    }

                } else {
                    newModerator = Integer.parseInt(g.getGroup_moderator().getUserId());
                }

                body = "group_id=" + g.getGroup_id() + "&user_id=" + n.getUserId() + "&new_moderator=" + newModerator + "&flag=" + flag;
                bodyBytes = body.getBytes();
            }

            if (flag == 1) {
                body = "group_id=" + g.getGroup_id() + "&flag=" + flag;
                bodyBytes = body.getBytes();
            }

            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(bodyBytes);

                BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder responseToString = new StringBuilder();

                for (int i; (i = response.read()) >= 0;) {
                    responseToString.append((char) i);
                }

                if (responseToString.toString().equals("removed group") || responseToString.toString().equals("removed member")) {
                    System.out.println(responseToString.toString());
                    getActivity().setResult(95);
                    getActivity().finish();
                } else {
                    System.out.println(responseToString.toString());
                }

            } catch (Exception e) {

            }


            return status;
        }
    }
}
