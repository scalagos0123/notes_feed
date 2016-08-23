package mobidev.com.notesfeed;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kat on 08/08/2016.
 */
public class ChangePassword extends AppCompatActivity {

    private NotesFeedSession n;
    String newPassword1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        n = new NotesFeedSession(this);

        Button btnChangePassword = (Button) findViewById(R.id.button);

    }

    public void changePassword(View view){
        EditText currentPassword = (EditText) findViewById(R.id.currentPass);
        EditText newPassword = (EditText) findViewById(R.id.newPass);
        EditText confirmPassword = (EditText) findViewById(R.id.retypePass);
        NotesFeedSession n = new NotesFeedSession(this);

        String currentPassword1 = n.getUserPassword();
        System.out.println(currentPassword1);
        newPassword1 = newPassword.getText().toString();
        String confirmPassword1 = confirmPassword.getText().toString();


        if(currentPassword.getText().toString().equals(currentPassword1)) {
          if(newPassword.getText().toString().equals(confirmPassword1) && newPassword.getText().length()>=6) {
             //set minimum length (6 characters) - 123456


              Change_password c = new Change_password();
              c.execute(newPassword1);

              finish();
          } else {
              currentPassword.setBackgroundColor(getResources().getColor(R.color.redLine));
              confirmPassword.setBackgroundColor(getResources().getColor(R.color.redLine));
          }
        } else {
            //call edittext, change color red
            currentPassword.setBackgroundColor(getResources().getColor(R.color.redLine));
        }
        //get currentpassword
        //enter new password
        //check newpassword and confirmpassword (if-else)
        //set newpassword to currentpassword

    }

    public class Change_password extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/changepassword.php";
            String body = "user_id="+n.getUserId()+"&user_password="+ params[0];
            byte[] bodyBytes =  body.getBytes();
            boolean status = false;


            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
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

            } catch(Exception e){
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {

                System.out.println(n.getUserPassword());
                n.editUserSessionPassword(newPassword1);
                System.out.println(n.getUserPassword());
            }
        }
    }



  //  private String
  //  public ChangePassword(Context context) {


   // }
}
