package mobidev.com.notesfeed;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shaun on 7/19/2016.
 */
public class SignUpUser extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private String name[];
    private SignUp s;

    public SignUpUser(Context context, String name[], SignUp mySignup) {
        this.name = name;
        this.context = context;
        this.s = mySignup;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String email = (String) params[0];
        String password = (String) params[1];
        String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/registeruser.php";

        Map<String, String> person_info = new LinkedHashMap<>();
        person_info.put("email", email);
        person_info.put("password", password);
        person_info.put("fname", this.name[0]);
        person_info.put("lname", this.name[1]);

        StringBuilder encodedValues = new StringBuilder();
        for (Map.Entry<String, String> person_info_entry : person_info.entrySet()) {
            encodedValues.append(URLEncoder.encode(person_info_entry.getKey()));
            encodedValues.append("=");
            encodedValues.append(URLEncoder.encode(person_info_entry.getValue()));
            encodedValues.append("&");
        }

        boolean result = false;

//        Sending data to server via POST method

        try {
            byte[] infoBytes = encodedValues.toString().getBytes();
            URL urlBytes = new URL(link);
            HttpURLConnection urlConn = (HttpURLConnection) urlBytes.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            urlConn.getOutputStream().write(infoBytes);

//            End of sending
//            Checking output for boolean value

            Reader response = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

//            Compiling response
            StringBuilder responseObject = new StringBuilder();
            for (int i; (i = response.read()) >= 0;) {
                responseObject.append((char) i);
            }

            if (responseObject.toString().equals("true")) {
                result = true;
            } else {
                result = false;
            }

//            End of checking output
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean == true) {
            Toast.makeText(context, "You are now registered!", Toast.LENGTH_LONG).show();
            s.finish();
        } else {
            Toast.makeText(context, "Can't connect to server. \n Try again later.", Toast.LENGTH_SHORT);
        }
    }
}
