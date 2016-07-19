package mobidev.com.notesfeed;

import android.os.AsyncTask;
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
        EditText fname_input = (EditText) findViewById(R.id.fname_personal_info);
        EditText lname_input = (EditText) findViewById(R.id.lname_personal_info);

        //        Login info
        EditText email_input = (EditText) findViewById(R.id.email_text);
        EditText password_input = (EditText) findViewById(R.id.password_text);
        EditText confirm_password_input = (EditText) findViewById(R.id.confirm_password_text);

        String fname = fname_input.getText().toString();
        String lname = lname_input.getText().toString();
        String email = email_input.getText().toString();
        String password = password_input.getText().toString();
        String confirm_password = confirm_password_input.getText().toString();


        if (password.length() > 4) {
            if (password.equals(confirm_password) && password.length() > 0) {
                System.out.println("1");
                if (email.length() > 0 && fname.length() > 0 && lname.length() > 0) {
                    if (email.contains("@")) {
                        SignUpUser signup = new SignUpUser(this, new String[]{fname, lname}, this);
                        signup.execute(email, password);

                        System.out.println(signup.getStatus());
                    } else {
                        Snackbar.make(view, "Email's not valid.", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    if (email.length() == 0) {
                        Snackbar.make(view, "Email's required.", Snackbar.LENGTH_LONG).show();
                    } else if (fname.length() == 0) {
                        Snackbar.make(view, "First name's required.", Snackbar.LENGTH_LONG).show();
                    } else if (lname.length() == 0) {
                        Snackbar.make(view, "Last name's required.", Snackbar.LENGTH_LONG).show();
                    }
                }
            } else {
                System.out.println(password.toString());
                System.out.println(confirm_password.toString());
                Snackbar.make(view, "Passwords don't match.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view, "Password's too short.", Snackbar.LENGTH_LONG).show();
        }

    }
}
