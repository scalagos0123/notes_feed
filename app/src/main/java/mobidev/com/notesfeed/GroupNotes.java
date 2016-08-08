package mobidev.com.notesfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupNotes = new ArrayList<>();

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
                JSONArray notesId = jsonResponse.getJSONArray("notesId");
                JSONArray notesTitle = jsonResponse.getJSONArray("notesTitle");
                JSONArray notesContent = jsonResponse.getJSONArray("notesContent");
                JSONArray noteOwner = jsonResponse.getJSONArray("noteOwner");

                for (int i = 0; i < notesTitle.length(); i++) {
                    Notes groupNote = new Notes(Integer.parseInt(notesId.getString(i)), notesTitle.getString(i), notesContent.getString(i));
                    groupNote.setNote_owner(noteOwner.getString(i));
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
}
