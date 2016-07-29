package mobidev.com.notesfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shaun on 7/18/2016.
 */
public class CheckLogin extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private boolean loginStatus;
    private LoginActivity activityMethods;
    private String userId;
    private String user_fullname;

    public CheckLogin(Context context, LoginActivity thisLoginActivity) {
        this.context = context;
        this.activityMethods = thisLoginActivity;

    }

    public boolean getResult() {
        return this.loginStatus;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        String link = "http://SHAUN-G501/notesfeed/getusers.php";

        Map<String, String> loginValues = new LinkedHashMap<>();
        loginValues.put("email", username);
        loginValues.put("password", password);

        StringBuilder urlParameters = new StringBuilder();
        for (Map.Entry<String, String> keyValues : loginValues.entrySet()) {
            urlParameters.append(URLEncoder.encode(keyValues.getKey()));
            urlParameters.append("=");
            urlParameters.append(URLEncoder.encode(keyValues.getValue()));
            urlParameters.append("&");
        }

        boolean checkStatus = false;

        try {

            byte[] loginData = urlParameters.toString().getBytes();
            URL url = new URL(link);
            HttpURLConnection getUrl = (HttpURLConnection) url.openConnection();
            getUrl.setRequestMethod("POST");
            getUrl.setDoOutput(true);
            System.out.println("Sending data");
            getUrl.getOutputStream().write(loginData);
            System.out.println("Data sent");

//            Receiving data from server
            InputStreamReader is = new InputStreamReader(getUrl.getInputStream());
            Reader in = new BufferedReader(is);

//            Parsing data received from server
            StringBuilder check = new StringBuilder();
            for (int c; (c = in.read()) >= 0;) {
                check.append((char)c);
            }

            /*

            Since I've encoded the name to JSON, this is the current and effective way
            to convert String to a JSON format (since the server is sending JSON)

             */

            JSONObject receivedJson = new JSONObject(check + "");
            JSONArray user_name = receivedJson.getJSONArray("user_name");
            JSONArray condition = receivedJson.getJSONArray("condition");
            JSONArray user_id = receivedJson.getJSONArray("user_id");

//            Checking condition from JSON

            if (condition.getString(0).equals("true")) {
                this.user_fullname = user_name.getString(0);
                this.userId = user_id.getString(0);
                checkStatus = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.loginStatus = checkStatus;

    }

    protected void onPostExecute (Boolean result) {
        if (this.loginStatus == true) {
            activityMethods.showProgress(false);

//            session = context.getSharedPreferences(activityMethods.SHARED_PREFERENCES, context.MODE_PRIVATE);
//            SharedPreferences.Editor edit = session.edit();
//
//            edit.putString("userId", userId);
//            edit.putString("user_fullname", user_fullname);
//            edit.commit();
//
//            System.out.println(session.getString("userId", null));
//            System.out.println(session.getString("user_fullname", null));

            User currentUser = new User(context, this.userId, this.user_fullname);
            currentUser.startUserSession();

            Intent i = new Intent (context, MainActivity.class);
            i.putExtra("currentUser", currentUser);
            context.startActivity(i);

        } else {
            activityMethods.showProgress(false);
            Toast.makeText(context, "User doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }
}
