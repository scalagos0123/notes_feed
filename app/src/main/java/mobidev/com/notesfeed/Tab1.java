package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Tab1 extends Fragment {

    private ArrayList<Notes> notes;
    private ListView notes_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.notes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Notes n = new Notes(i, "Note title #" +i, "Content of #" + i + " here");
            notes.add(n);
        }
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        this.notes_list = (ListView) view.findViewById(R.id.notes_listview);
        Notes_ListAdapter notesAdapter = new Notes_ListAdapter(getContext(), R.layout.note_layout, notes);
        notes_list.setAdapter(notesAdapter);

        notes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notes n = (Notes) parent.getItemAtPosition(position);
                EditText e = (EditText) view.findViewById(R.id.notes_title);
                System.out.println(e.getText().toString());

                System.out.println(n.getNotes_title());

                CardView note_actions = (CardView) view.findViewById(R.id.note_actions);
                note_actions.setVisibility(View.INVISIBLE);
            }
        });

        //code for notes and stuff

        return view;


    }
}