package mobidev.com.notesfeed;

/**
 * Created by Debbie Co on 7/7/2016.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
    private Notes_ListAdapter notesAdapter;
    private int lastNoteId;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.notes = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());
        GetPersonalNotes getNotes = new GetPersonalNotes();
        getNotes.execute();

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

        //code for notes and stuff

        this.notes_list = (ListView) view.findViewById(R.id.notes_listview);
        notesAdapter = new Notes_ListAdapter(getContext(), R.layout.note_layout, notes);

        FloatingActionButton addNote = (FloatingActionButton) view.findViewById(R.id.add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes n = new Notes(000, "", "");
                notesAdapter.insert(n, 0);
                notesAdapter.notifyDataSetChanged();
            }
        });

        return view;


    }

    private class GetPersonalNotes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

//            The below part gets the last ID of the notes

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor c = db.rawQuery("select max(" + databaseHelper.COL_1 + ") from " + databaseHelper.TABLE_NAME, null);

            c.moveToFirst();
            lastNoteId = c.getInt(0) + 1;

//            End of getting the last id

            /*

            Use this method (doInBackground) to get all of the notes from the database.
            for loop or while loop, make a new note inside it, tapos add mo sa ArrayList<Notes>

            Example:

            while (condition mo) {

                Notes n = new Notes (lastNoteId, title ng note from the database, content ng note from the database);
                ArrayList of Notes mo, tapos use .add(n);

            }

            Kapag may lumabas na notes, this is done

             */

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notes_list.setAdapter(notesAdapter);
        }
    }

    private class AddNote extends AsyncTask<Notes, Void, Void> {

        @Override
        protected Void doInBackground(Notes... params) {

            /*

            Execute the inserting of a new note here.
            To get the note, initialize kayo ng Notes variable, tapos kunin nyo yung note via params

            Ex. params[0];
            params kasi yung tinatanggap ng .execute method ng AsyncTask is an array of a data type or a class na nakalagay (in this case, Notes nilagay ko)

            Use lastNoteId variable para makapag-insert ka ng ID na may blank title and content (para lagyan ng laman yung blanks, edit mo after mag-add, then pressing save will do it)

            No need to implement save and delete in Tab1. See note below:

            * Saving and Deleting a note, nakalagay sa Notes_ListAdapter, in their respective doInBackground methods (Saving is the UpdateNote class, then Deleting is DeleteNote)
            * Wala na kayo gagawin sa ClickListeners. I've set them to adapt to the note that you want to modify.
            * Yung pag-save and pag-delete, ilalagay nyo dun sa may else part (may nilagay akong if(selectedNote.getNote_Owner() != null) condition. Dun nyo ilagay sa else yung updating, and deleting the local database

             */

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            what to do after the adding is complete
//            this is optional
        }
    }
}