package mobidev.com.notesfeed;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Kat on 01/08/2016.
 */
public class Notes_ListAdapter extends ArrayAdapter<Notes> {
    private ArrayList<Notes> notesList;
    private Context context;
    private LayoutInflater inflater;
    private int resource;

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
        CardView note_action;
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
        final ArrayAdapter<Notes> notes_list = this;
        final int item_position = position;

        viewElements.note_action = (CardView) convertView.findViewById(R.id.note_actions);
        viewElements.notes_title = (EditText) convertView.findViewById(R.id.notes_title);
        viewElements.notes_content = (EditText) convertView.findViewById(R.id.edit_text_note);
        viewElements.save = (Button) convertView.findViewById(R.id.save_button);
        viewElements.delete = (Button) convertView.findViewById(R.id.delete_button);

        viewElements.notes_title.setText(notesList.get(position).getNotes_title());
        viewElements.notes_content.setText(notesList.get(position).getNotes_content());

        viewElements.notes_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(n.getNotes_title());
                viewElements.note_action.setVisibility(View.VISIBLE);

                for (int i = 0; i < notes_list.getCount(); i++) {
                    System.out.println(notes_list.getItem(i).getNotes_title());
                }
            }
        });

        viewElements.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes_list.remove(notes_list.getItem(item_position));
            }
        });

        convertView.setTag(viewElements);
        return convertView;
    }
}
