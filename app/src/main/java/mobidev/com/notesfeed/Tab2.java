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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Tab2 extends Fragment {

    private ArrayList<Group> groups;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        groups = new ArrayList<Group>();
//        Group g1 = new Group();
//        g1.setGroup_name("Sample Group");
//        g1.addGroup_member(1, "Shaun");
//        g1.addGroup_member(2, "Cassidy");
//
//        groups.add(g1);
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        ListView sampleListView = (ListView) view.findViewById(R.id.listView);

        ListAdapter group_adapter = new ListAdapter(getActivity(), R.layout.group_list, groups);
        sampleListView.setAdapter(group_adapter);

        return view;
    }
}