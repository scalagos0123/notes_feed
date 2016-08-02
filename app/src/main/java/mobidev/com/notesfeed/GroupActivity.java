package mobidev.com.notesfeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Group g = (Group) getIntent().getSerializableExtra("selectedGroup");
        getSupportActionBar().setSubtitle(g.getGroup_name());

    }
}
