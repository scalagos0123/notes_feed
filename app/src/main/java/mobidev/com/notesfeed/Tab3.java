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
public class Tab3 extends Fragment {

    ListView list = null;
    String[] myItems = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        populateListView(view);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = (String) parent.getItemAtPosition(position);
                NotesFeedSession n = new NotesFeedSession(getContext());

                /*

                if you want to get the current user's data, initialize kayo ng NotesFeedSession class, tapos parameter nya, call nyo lang getContext() method.
                There are methods in NotesFeedSession to get the user data para maiwasan mag hard code

                */


                if (itemSelected.equals(myItems[0])) {

//                    start the intent here kung change password yung sinelect

                    Intent changePassword = new Intent(getContext(), ChangePassword.class);
                    n = new NotesFeedSession(getContext());
                    startActivity(changePassword);


                } else if (itemSelected.equals(myItems[1])) {

//                    start the intent here kung change email yung sinelect
                    Intent changeEmail = new Intent(getContext(), ChangeEmail.class);
                    n = new NotesFeedSession(getContext());
                    startActivity(changeEmail);

                } else if (itemSelected.equals(myItems[2])) {
//                    Don't mess this part
                    n = new NotesFeedSession(getContext());
                    n.endUserSession();
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    private void populateListView(View v){
        String[] myItems = {"Change Password", "Change Email", "Sign Out"};
        this.myItems = myItems;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myItems);

        list = (ListView) v.findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
    }
}