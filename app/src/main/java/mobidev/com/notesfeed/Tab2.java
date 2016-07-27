package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Tab2 extends Fragment {

    private List<Group> groups;
    SharedPreferences session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Group g = new Group(getActivity(), "1");
        session = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, getActivity().MODE_PRIVATE);
        g.getGroupsByUser(session.getString("userId", null));
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.tab_fragment_2, container, false);
    }
}