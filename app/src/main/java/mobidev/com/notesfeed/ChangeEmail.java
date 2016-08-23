package mobidev.com.notesfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kat on 11/08/2016.
 */
public class ChangeEmail extends AppCompatActivity{
    private NotesFeedSession n;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);

        Button btnChangeEmail = (Button)findViewById(R.id.button1);
    }

    public void changeEmail(View view) {
        EditText currentEmail = (EditText) findViewById(R.id.currentEmail);
        EditText newEmail = (EditText) findViewById(R.id.newEmail);
        EditText retypeEmail = (EditText) findViewById(R.id.retypeEmail);
        n = new NotesFeedSession(this);

        String currentEmail1 = n.getUserEmail();
        String newEmail1 = newEmail.getText().toString();
        String retypeEmail1 = retypeEmail.getText().toString();


        if (currentEmail.getText().toString().equals(currentEmail1) && currentEmail.getText().toString().contains("@")) {
            if (newEmail.getText().toString().equals(retypeEmail1)) {
                //set


                //check the edittext input if it has @
                //

                n.editUserSessionEmail(newEmail1);
                System.out.println(n.getUserEmail());

                Change_Email n = new Change_Email();
                n.execute(newEmail1);

                finish();
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
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/changeemail.php";
            String body = "user_id=" + n.getUserId() + "&user_email=" + m;
            byte[] bodyBytes = body.getBytes();
            boolean status = false;

            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.getOutputStream().write(bodyBytes);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

}
