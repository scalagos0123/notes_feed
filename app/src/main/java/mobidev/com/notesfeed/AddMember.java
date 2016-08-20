package mobidev.com.notesfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.lang.reflect.Member;
import java.util.ArrayList;

/**
 * Created by Shaun on 8/20/2016.
 */
public class AddMember extends AppCompatActivity {

    private MemberAdapter memberList_adapter;
    private MemberAdapter results_adapter;
    private ListView memberList;
    private EditText inputBox;
    private ImageButton back;
    private ArrayList<User> members;
    private ArrayList<User> userDatabase;
    private ArrayList<User> results;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);

        userDatabase = new ArrayList<>();
        results = new ArrayList<>();

        Bundle getBundle = getIntent().getExtras().getBundle("usersBundle");
        members = (ArrayList<User>) getBundle.getSerializable("members");

        initializeViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                performSearch(s.toString());
                System.out.println("Searching now: " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initializeViews() {
        inputBox = (EditText) findViewById(R.id.search_input);
        back = (ImageButton) findViewById(R.id.backbtn);
        inputBox.setHint("Type a person's name");
        memberList = (ListView) findViewById(R.id.group_list);
        memberList_adapter = new MemberAdapter(this, R.layout.member_list, userDatabase);
        results_adapter = new MemberAdapter(this, R.layout.member_list, results);
    }

    private void filterMembers() {

    }

    private class GetUsers extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
