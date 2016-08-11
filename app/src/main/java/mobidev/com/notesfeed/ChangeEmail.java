package mobidev.com.notesfeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Kat on 11/08/2016.
 */
public class ChangeEmail extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);
        ChangeEmail changeEmail= new ChangeEmail();

    }

    public void changeEmail(View view) {
        EditText currentEmail = (EditText) findViewById(R.id.textView7);
        EditText newEmail = (EditText) findViewById(R.id.textView8);
        EditText retypeEmail = (EditText) findViewById(R.id.textView9);
        NotesFeedSession n = new NotesFeedSession(this);

        String currentEmail1 = n.getUserEmail();
        String newEmail1 = newEmail.getText().toString();
        String retypeEmail1 = retypeEmail.getText().toString();


        if (currentEmail.getText().toString().equals(currentEmail1)) {
            if (newEmail.getText().toString().equals(retypeEmail1)) {
                //set
                n.editUserSessionPassword(newEmail1);
            } else {
                currentEmail.setTextColor(getResources().getColor(R.color.redLine));
                retypeEmail.setTextColor(getResources().getColor(R.color.redLine));
            }
        } else {
            //call edittext, change color red
            currentEmail.setTextColor(getResources().getColor(R.color.redLine));
        }
    }

}
