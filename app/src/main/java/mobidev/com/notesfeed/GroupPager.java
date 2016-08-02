package mobidev.com.notesfeed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Shaun on 8/2/2016.
 */
public class GroupPager extends FragmentStatePagerAdapter {

    private int count;

    public GroupPager (FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;

        switch (position) {
            case 0:
                f = new GroupNotes();
                break;
            case 1:
                f = new GroupSettings();
                break;
        }

        return f;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position) {
            case 0:
                title = "Group Notes";
                break;
            case 1:
                title = "Settings";
                break;
        }

        return title;
    }
}
