package mobidev.com.notesfeed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Shaun on 8/2/2016.
 */
public class GroupPager extends FragmentStatePagerAdapter {

    private int count;
    private SparseArray<Fragment> fragments;

    public GroupPager (FragmentManager fm, int count) {
        super(fm);
        this.count = count;
        this.fragments = new SparseArray<>();
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

    public Fragment getFragmentAtPosition(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragments.remove(position);
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
