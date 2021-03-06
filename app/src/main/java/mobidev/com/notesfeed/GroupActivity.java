package mobidev.com.notesfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {

    private Group g = null;
    private int ADD_MEMBER_SUCCESS = 200;
    private GroupPager gp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        g = (Group) getIntent().getSerializableExtra("selectedGroup");

        TextView groupName = (TextView) findViewById(R.id.group_name);
        groupName.setText(g.getGroup_name());

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        final ViewPager vp = (ViewPager) findViewById(R.id.fragment_pages);
        gp = new GroupPager(getSupportFragmentManager(), 2);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        vp.setAdapter(gp);
        tabs.setupWithViewPager(vp);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(95);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ADD_MEMBER_SUCCESS) {
            GroupSettings g = (GroupSettings) gp.getFragmentAtPosition(1);
            g.refreshOptions();
        }
    }

    public void changeReject(View v) {
        finish();
    }

    public Group getGroupData() {
        return g;
    }


}
