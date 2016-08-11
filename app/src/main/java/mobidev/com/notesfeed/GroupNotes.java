package mobidev.com.notesfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shaun on 8/2/2016.
 */
public class GroupNotes extends Fragment {
    private Group g = null;
    private ArrayList<Notes> groupNotes;
    private ListView notesListView;
    private Notes_ListAdapter groupNotesAdapter;
    private FloatingActionButton fab;
    private int last_noteId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupNotes = new ArrayList<>();
        last_noteId = 0;

        GroupActivity activity = (GroupActivity) getActivity();
        g = activity.getGroupData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println(g.getGroup_name() + " id: " + g.getGroup_id());
        View view = inflater.inflate(R.layout.group_notes_tab,container, false);

        notesListView = (ListView) view.findViewById(R.id.group_notes_list);
        groupNotesAdapter = new Notes_ListAdapter(getContext(), R.layout.note_layout, groupNotes);

        GetGroupNotes g = new GetGroupNotes();
        g.execute(this.g.getGroup_id());

        fab = (FloatingActionButton) view.findViewById(R.id.add_note_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesFeedSession userData = new NotesFeedSession(getContext());
                Notes newNote = new Notes(last_noteId, "", "");
                newNote.setNote_owner(new User(userData.getUserId(), userData.getUserFullName()));
                AddGroupNote a = new AddGroupNote();
                a.execute(newNote);

                groupNotes.add(0, newNote);
                groupNotesAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    protected class GetGroupNotes extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            String groupId = params[0];
            String link = NotesFeedSession.SERVER_ADDRESS + "/notesfeed/getnotes.php?groupId=" + groupId;

            try {
                URL url = new URL(link);
                System.out.println("Sending data");
                HttpURLConnection sendRequest = (HttpURLConnection) url.openConnection();
                System.out.println("Data sent!");

                StringBuffer responseBuilder = new StringBuffer();
                Reader response = new BufferedReader(new InputStreamReader(sendRequest.getInputStream()));

                for (int i; (i = response.read()) >= 0; ) {
                    responseBuilder.append((char) i);
                }

                System.out.println(responseBuilder.toString());

                JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
                JSONArray notesIdMax = jsonResponse.getJSONArray("notesIdMax");
                JSONArray notesId = jsonResponse.getJSONArray("notesId");
                JSONArray notesTitle = jsonResponse.getJSONArray("notesTitle");
                JSONArray notesContent = jsonResponse.getJSONArray("notesContent");
                JSONObject noteOwner = jsonResponse.getJSONObject("noteOwner");
                JSONArray userId = noteOwner.getJSONArray("userId");
                JSONArray user_fullname = noteOwner.getJSONArray("user_fullname");

                int getLastId = Integer.parseInt(notesIdMax.getString(0));
                last_noteId = getLastId + 1;
                System.out.println(last_noteId);

                for (int i = 0; i < notesTitle.length(); i++) {
                    Notes groupNote = new Notes(Integer.parseInt(notesId.getString(i)), notesTitle.getString(i), notesContent.getString(i));
                    User noteowner = new User(userId.getString(i), user_fullname.getString(i));
                    groupNote.setNote_owner(noteowner);
                    groupNotes.add(groupNote);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notesListView.setAdapter(groupNotesAdapter);
        }
    }

    protected class AddGroupNote extends AsyncTask<Notes, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Notes... params) {
            Notes noteToAdd = params[0];
            int flag = 3;
            String link = NotesFeedSession.SERVER_ADDRESS + "notesfeed/note_actions.php";
            String noteData = "note_id=" + noteToAdd.getNotes_id() + "&note_title=" + noteToAdd.getNotes_title() + "&note_content=" + noteToAdd.getNotes_content() + "&flag=" + flag + "&user_id=" + noteToAdd.getNote_owner().getUserId() + "&group_id=" + g.getGroup_id();

            System.out.println("note_id=" + noteToAdd.getNotes_id() + "\n&note_title=" + noteToAdd.getNotes_title() + "\n&note_content=" + noteToAdd.getNotes_content() + "\n&flag=" + flag + "\n&user_id=" + noteToAdd.getNote_owner().getUserId() + "\n&group_id=" + g.getGroup_id());

            byte[] noteDataBytes = noteData.getBytes();

            System.out.println("Adding new note");

            boolean status = false;

            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.getOutputStream().write(noteDataBytes);

                System.out.println("Note sent");

                Reader outputConnection = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer outputConnectionReader = new StringBuffer();

                for (int c; (c = outputConnection.read()) >= 0;) {
                    outputConnectionReader.append((char)c);
                }

                if (outputConnectionReader.toString().equals("added")) {
                    status = true;
                    last_noteId++;
                } else {
                    System.out.println(outputConnectionReader);
                }

            } catch (Exception e) {
                System.out.println("Adding failed");
                e.printStackTrace();
            }

            return status;
        }
    }
}
