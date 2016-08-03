package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class  Tab3 extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        populateListView(view);
        return view;
    }

    private void populateListView(View v){
        String[] myItems = {"Change Password", "Change Email", "Sign Out"};
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.da_item, myItems);
         ListView list = (ListView) v.findViewById(R.id.listViewMain);
         list.setAdapter(adapter);
}
}