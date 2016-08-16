package mobidev.com.notesfeed;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.LinkedHashMap;
import java.util.Map;

public class CreateGroup extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.content_create_group, null);
        view.setTag("createGroup");
        dialogBuilder.setView(view);

        final EditText groupName = (EditText) view.findViewById(R.id.group_name);

        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Asynctask create a group
                CreateGroupAsyncTask c = new CreateGroupAsyncTask();
                c.execute(groupName.getText().toString());
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return dialogBuilder.create();
    }

    public class CreateGroupAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            NotesFeedSession n = new NotesFeedSession(getActivity());

            String groupName = params[0];
            String moderator = n.getUserId();
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

                for (int c; (c = r.read()) >= 0;) {
                    responseOutput.append((char)c);
                }

                if (responseOutput.toString().equals("group added")) {
                    response = true;
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

//            View view = getActivity().getLayoutInflater().inflate(R.layout.tab_fragment_2, null);
            Tab2 methods = new Tab2();

            if (aBoolean == true) {
                methods.makeResponse();
            }
        }
    }

}
