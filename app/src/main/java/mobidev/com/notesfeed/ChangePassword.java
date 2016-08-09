package mobidev.com.notesfeed;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Kat on 08/08/2016.
 */
public class ChangePassword extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        ChangePassword changePassword= new ChangePassword();

    }

    public void changePassword(View view){
        EditText currentPassword = (EditText) findViewById(R.id.textView3);
        EditText newPassword = (EditText) findViewById(R.id.textView4);
        EditText confirmPassword = (EditText) findViewById(R.id.textView5);
        NotesFeedSession n = new NotesFeedSession(this);

        String currentPassword1 = n.getUserPassword();
        String newPassword1 = newPassword.getText().toString();
        String confirmPassword1 = confirmPassword.getText().toString();


        if(currentPassword.getText().toString().equals(currentPassword1) ) {
          if(newPassword.getText().toString().equals(confirmPassword1)) {
             //set
              n.editUserSessionPassword(newPassword1);
          } else {
              currentPassword.setTextColor(getResources().getColor(R.color.redLine));
              confirmPassword.setTextColor(getResources().getColor(R.color.redLine));
          }
        } else {
            //call edittext, change color red
            currentPassword.setTextColor(getResources().getColor(R.color.redLine));
        }
        //get currentpassword
        //enter new password
        //check newpassword and confirmpassword (if-else)
        //set newpassword to currentpassword

    }



  //  private String
  //  public ChangePassword(Context context) {


   // }
}
