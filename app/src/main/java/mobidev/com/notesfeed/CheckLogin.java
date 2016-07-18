package mobidev.com.notesfeed;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shaun on 7/18/2016.
 */
public class CheckLogin extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private boolean loginStatus;

    public CheckLogin(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String username = (String) params[0];
        String password = (String) params[1];
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

            Reader in = new BufferedReader(new InputStreamReader(getUrl.getInputStream()));

            StringBuilder check = new StringBuilder();
            for (int c; (c = in.read()) >= 0;) {
                check.append((char)c);
            }

            System.out.println(check.toString());

            if (check.toString().equals("true")) {
                checkStatus = true;
            } else {
                checkStatus = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.loginStatus = checkStatus;

    }

    protected void onPostExecute (Boolean result) {
        if (this.loginStatus == true) {
            Intent i = new Intent (context, NotesFeed_main.class);
            context.startActivity(i);
        } else {
            Toast.makeText(context, "User doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }
}
