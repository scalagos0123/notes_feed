package mobidev.com.notesfeed;

import android.content.Context;
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

        public ViewHolder() {

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(resource, parent, false);

        ViewHolder viewElements = new ViewHolder();

        viewElements.notes_title = (EditText) convertView.findViewById(R.id.notes_title);
        viewElements.notes_content = (EditText) convertView.findViewById(R.id.edit_text_note);

        viewElements.notes_title.setText(notesList.get(position).getNotes_title());
        viewElements.notes_content.setText(notesList.get(position).getNotes_content());

        convertView.setTag(viewElements);
        return convertView;
    }
}
