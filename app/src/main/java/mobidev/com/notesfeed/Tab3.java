package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        final String[] myItems = {"Change Password", "Change Email", "Sign Out"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.da_item, myItems);
        ListView list = (ListView) v.findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = (String) parent.getItemAtPosition(position);

                if (itemSelected.equals(myItems[0])) {

//                    what to do with change password
//                    Intent i = new Intent(getContext(), ChangePassword.class);
//                    getContext().startActivity(i);

                } else if (itemSelected.equals(myItems[1])) {

//                    What to do with change email

                } else if (itemSelected.equals(myItems[2])) {
                    NotesFeedSession n = new NotesFeedSession(getContext());
                    n.endUserSession();
                    getActivity().finish();
                }
            }
        });
}
}