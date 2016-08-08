package mobidev.com.notesfeed;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupActivity extends AppCompatActivity {

    private Group g = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        g = (Group) getIntent().getSerializableExtra("selectedGroup");

        getSupportActionBar().setTitle(g.getGroup_name());
        getSupportActionBar().setElevation(0);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        final ViewPager vp = (ViewPager) findViewById(R.id.fragment_pages);
        GroupPager gp = new GroupPager(getSupportFragmentManager(), 2);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        vp.setAdapter(gp);
        tabs.setupWithViewPager(vp);

    }

    public Group getGroupData() {
        return g;
    }


}
