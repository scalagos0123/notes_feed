package mobidev.com.notesfeed;

import android.content.Context;
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

import org.w3c.dom.Text;

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
        final Notes selectedNote = this.getItem(position);

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
}
