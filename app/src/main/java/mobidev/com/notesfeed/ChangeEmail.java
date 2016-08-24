package mobidev.com.notesfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kat on 11/08/2016.
 */
public class ChangeEmail extends AppCompatActivity {
    private NotesFeedSession n;
    private String newEmail1;
    private ChangeEmail c = this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);

        Button btnChangeEmail = (Button)findViewById(R.id.button);
        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });
    }

    public void done() {
        n.editUserSessionEmail(newEmail1);
        System.out.println(n.getUserEmail());
        Intent data = new Intent();
        data.putExtra("email", n.getUserEmail());
        setResult(MainActivity.CHANGE_EMAIL_SUCCESS, data);
        finish();
    }

    public void changeReject(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }


    public void changeEmail() {
        EditText currentEmail = (EditText) findViewById(R.id.currentEmail);
        EditText newEmail = (EditText) findViewById(R.id.newEmail);
        EditText retypeEmail = (EditText) findViewById(R.id.retypeEmail);
        n = new NotesFeedSession(this);

        String currentEmail1 = n.getUserEmail();
        newEmail1 = newEmail.getText().toString();
        String retypeEmail1 = retypeEmail.getText().toString();


        if (currentEmail.getText().toString().equals(currentEmail1) && currentEmail.getText().toString().contains("@")) {
            if (newEmail1.equals(retypeEmail1)) {
                //set


                //check the edittext input if it has @
                //

                Change_Email n = new Change_Email();
                n.execute(newEmail1);
                done();
            } else {
                currentEmail.setTextColor(getResources().getColor(R.color.redLine));
                retypeEmail.setTextColor(getResources().getColor(R.color.redLine));
            }
        } else {
            //call edittext, change color red
            currentEmail.setTextColor(getResources().getColor(R.color.redLine));
        }
    }

    public class Change_Email extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String m = params[0];
            System.out.println(m);
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/changeemail.php";
            String body = "user_id=" + n.getUserId() + "&user_email=" + m;
            byte[] bodyBytes = body.getBytes();
            boolean status = false;

            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(bodyBytes);

                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                for (int i; (i = r.read()) >= 0;) {
                    sb.append((char) i);
                }

                System.out.println(sb);
                if (sb.toString().equals("true")) {
                    status = true;
                } else {
                    status = false;
                    System.out.println(sb.toString());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                done();
            }
        }
    }

}
