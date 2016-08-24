package mobidev.com.notesfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class MainActivity extends AppCompatActivity {

    public final static int CHANGE_EMAIL_SUCCESS = 500;
    public final static int CHANGE_PASSWORD_SUCCESS = 501;
    public final static int CHANGE_EMAIL_NOTHING = 0;
    public final static int CHANGE_PASSWORD_NOTHING = 1;

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CHANGE_EMAIL_SUCCESS) {
            Toast.makeText(this, "Email updated", Toast.LENGTH_LONG).show();
        } else if (resultCode == CHANGE_PASSWORD_SUCCESS) {
            Toast.makeText(this, "Password updated", Toast.LENGTH_LONG).show();
        } else if (resultCode == RESULT_CANCELED && requestCode == 101) {
            Toast.makeText(this, "Email didn't change", Toast.LENGTH_LONG).show();
        } else if (resultCode == RESULT_CANCELED && requestCode == 100) {
            Toast.makeText(this, "Password didn't change", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), 3);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}