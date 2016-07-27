package mobidev.com.notesfeed;

import android.os.AsyncTask;
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
        String user_id = params[0];
        String link = "http://SHAUN-G501/notesfeed/getgroups.php";
        byte[] user_id_bytes = new byte[1024];
        user_id_bytes = user_id.getBytes();

        System.out.println("Sending data");

        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(user_id_bytes);
            System.out.println("Data sent");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.group;
    }
}
