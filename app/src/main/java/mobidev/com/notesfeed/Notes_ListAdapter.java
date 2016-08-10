package mobidev.com.notesfeed;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kat on 01/08/2016.
 */
public class Notes_ListAdapter extends ArrayAdapter<Notes> {
    private ArrayList<Notes> notesList;
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private ArrayAdapter<Notes> notes_list = this;

    public Notes_ListAdapter(Context context, int resource, ArrayList<Notes> objects) {
        super(context, resource, objects);
        this.notesList = objects;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    public class ViewHolder {
        EditText notes_title;
        EditText notes_content;
        TextView note_owner;
        CardView note_action;
        CardView group_note_owner;
        Button save;
        Button delete;

        public ViewHolder() {

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(resource, parent, false);

        final ViewHolder viewElements = new ViewHolder();
        final Notes n = this.getItem(position);
        final int item_position = position;

        viewElements.note_owner = (TextView) convertView.findViewById(R.id.note_owner);
        viewElements.note_action = (CardView) convertView.findViewById(R.id.note_actions);
        viewElements.notes_title = (EditText) convertView.findViewById(R.id.notes_title);
        viewElements.notes_content = (EditText) convertView.findViewById(R.id.edit_text_note);
        viewElements.save = (Button) convertView.findViewById(R.id.save_button);
        viewElements.delete = (Button) convertView.findViewById(R.id.delete_button);
        viewElements.group_note_owner = (CardView) convertView.findViewById(R.id.group_note_owner);

        if (notesList.get(position).getNote_owner().equals("")) {
            viewElements.group_note_owner.setVisibility(View.INVISIBLE);
        } else {
            viewElements.note_owner.setText(notesList.get(position).getNote_owner());
        }

        viewElements.notes_title.setText(notesList.get(position).getNotes_title());
        viewElements.notes_content.setText(notesList.get(position).getNotes_content());

        viewElements.notes_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Selected item: "+ n.getNotes_title());
                viewElements.note_action.setVisibility(View.VISIBLE);
            }
        });

        viewElements.notes_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Selected item: "+ n.getNotes_title());
                viewElements.note_action.setVisibility(View.VISIBLE);
            }
        });

        viewElements.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n.getNote_owner().equals("")) {
//                    integrate the SQLite Database part here
                } else {
                    n.setNotes_title(viewElements.notes_title.getText().toString());
                    n.setNotes_content(viewElements.notes_content.getText().toString());
                    UpdateNote thisAction = new UpdateNote(context);
                    thisAction.execute(n);
                    viewElements.note_action.setVisibility(View.GONE);
                }
            }
        });

        viewElements.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesList.remove(item_position);
                notes_list.notifyDataSetChanged();

                for (int i = 0; i < notesList.size(); i++) {
                    System.out.println(notesList.get(i).getNotes_title());
                }
            }
        });

//        convertView.setTag(viewElements);
        return convertView;
    }

    private class UpdateNote extends AsyncTask<Notes, Void, Boolean> {

        private Context c;

        public UpdateNote(Context c) {
            this.c = c;
        }

        @Override
        protected Boolean doInBackground(Notes... params) {

            Notes selectedNote = params[0];
            int flag = 1;
            String link = NotesFeedSession.SERVER_ADDRESS + "notesfeed/note_actions.php";
            String noteData = "note_id=" + selectedNote.getNotes_id() + "&note_title=" + selectedNote.getNotes_title() + "&note_content=" + selectedNote.getNotes_content() + "&flag=" + flag;

            byte[] noteDataBytes = noteData.getBytes();

            System.out.println("Updating note");

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

                if (outputConnectionReader.toString().equals("updated")) {
                    status = true;
                } else {
                    System.out.println(outputConnectionReader);
                }

            } catch (Exception e) {
                System.out.println("Update failed");
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean == true) {
                Toast.makeText(c, "Note's updated!", Toast.LENGTH_SHORT);
            }
        }
    }
}
