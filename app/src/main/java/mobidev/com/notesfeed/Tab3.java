package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Tab3 extends Fragment {

    SharedPreferences sp;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        Button sign_out = (Button) view.findViewById(R.id.sign_out);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
    }

    public void logout() {
        sp = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        System.out.println(sp.getString("userId", null));
        edit.clear();
        edit.commit();
        System.out.println(sp.getString("userId", null));
        getActivity().finish();
    }
}