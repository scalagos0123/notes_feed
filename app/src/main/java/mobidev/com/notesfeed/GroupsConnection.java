package mobidev.com.notesfeed;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Shaun on 7/24/2016.
 */
public class GroupsConnection extends AsyncTask<String, Void, Map<String, String>> {

    private Map<String, String> group;

    @Override
    protected Map<String, String> doInBackground(String... params) {
        String user_id = "user_id=" + params[0];
        String link = "http://SHAUN-G501/notesfeed/getgroups.php?" + user_id;
        byte[] user_id_bytes = user_id.getBytes();

        System.out.println("Sending data");

        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            System.out.println("Data sent");

            Reader server_output = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder response = new StringBuilder();

            for (int i; (i = server_output.read()) >= 0;) {
                response.append((char) i);
            }

            urlConnection.disconnect();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray group_id_array = jsonResponse.getJSONArray("group_id");
            JSONArray group_name_array = jsonResponse.getJSONArray("group_name");

            for (int i = 0; i < group_name_array.length(); i++) {
                System.out.println(group_name_array.getString(i));
            }

            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.group;
    }
}
