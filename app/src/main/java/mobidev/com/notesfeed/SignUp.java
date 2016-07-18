package mobidev.com.notesfeed;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void registerUser(View view) {
//        Getting all textfields first for parsing
//        Personal info
        EditText fname = (EditText) findViewById(R.id.fname_personal_info);
        EditText lname = (EditText) findViewById(R.id.lname_personal_info);

        //        Login info
        EditText email = (EditText) findViewById(R.id.email_text);
        EditText password = (EditText) findViewById(R.id.password_text);
        EditText confirm_password = (EditText) findViewById(R.id.confirm_password_text);

        if (password.toString().equals(confirm_password.toString()) && password.length() > 0) {
            if (email.toString().length() > 0 && fname.toString().length() > 0 && lname.toString().length() > 0) {

                SignUpUser signup = new SignUpUser(this, new String[]{fname.toString(), lname.toString()});
                signup.execute(email.toString(), password.toString());

//                , String username, String password, String name[]

            } else {
                if (email.toString().length() == 0) {
                    Snackbar.make(view, "Email's required", Snackbar.LENGTH_LONG).show();
                } else if (fname.toString().length() == 0) {
                    Snackbar.make(view, "First name's required", Snackbar.LENGTH_LONG).show();
                } else if (lname.toString().length() == 0) {
                    Snackbar.make(view, "Last name's required", Snackbar.LENGTH_LONG).show();
                }
            }
        } else {
            Snackbar.make(view, "Passwords don't match.", Snackbar.LENGTH_LONG).show();
        }
    }
}
