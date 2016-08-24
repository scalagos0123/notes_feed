package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    FragmentManager fm;
    private SparseArray<Fragment> fragments;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.fm = fm;
        //Initializing tab count
        this.tabCount= tabCount;
        this.fragments = new SparseArray<>();
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        Fragment f = null;
        switch (position) {
            case 0:
                f = new Tab1();
                break;
            case 1:
                f = new Tab2();
                break;
            case 2:
                f = new Tab3();
                break;
            default:
                return null;
        }

        return f;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    public Fragment getFragmentAtPosition(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabTitle = "";

        switch (position) {
            case 0:
                tabTitle = "My Notes";
                break;
            case 1:
                tabTitle = "Groups";
                break;
            case 2:
                tabTitle = "Settings";
                break;
        }

        return tabTitle;
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
}
