package mobidev.com.notesfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupActivity extends AppCompatActivity {

    private Group g = null;
    private int ADD_MEMBER_SUCCESS = 200;
    private GroupPager gp;

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
        gp = new GroupPager(getSupportFragmentManager(), 2);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        vp.setAdapter(gp);
        tabs.setupWithViewPager(vp);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ADD_MEMBER_SUCCESS) {
            GroupSettings g = (GroupSettings) gp.getFragmentAtPosition(1);
            g.refreshOptions();
        }
    }

    public Group getGroupData() {
        return g;
    }


}
