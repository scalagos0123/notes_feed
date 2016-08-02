package mobidev.com.notesfeed;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Group g = (Group) getIntent().getSerializableExtra("selectedGroup");
        getSupportActionBar().setTitle(g.getGroup_name());
        getSupportActionBar().setElevation(0);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        final ViewPager vp = (ViewPager) findViewById(R.id.fragment_pages);
        GroupPager gp = new GroupPager(getSupportFragmentManager(), 2);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        vp.setAdapter(gp);
        tabs.setupWithViewPager(vp);

//        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                vp.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

    }
}
