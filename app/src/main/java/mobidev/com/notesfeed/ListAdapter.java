package mobidev.com.notesfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun on 7/29/2016.
 */
public class ListAdapter extends ArrayAdapter<Group> {

    private ArrayList<Group> currentList;
    private Context context;
    private LayoutInflater inflater;
    private int resource;

    public ListAdapter(Context context, int resource, ArrayList<Group> objects) {
        super(context, resource, objects);
        this.currentList = objects;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    public class ViewHolder {
        TextView group_title;
        TextView group_size;

        public ViewHolder() {

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Group g = this.getItem(position);
        convertView = inflater.inflate(R.layout.group_list, parent, false);

        ViewHolder h = new ViewHolder();

        h.group_title = (TextView) convertView.findViewById(R.id.group_title);
        h.group_size = (TextView) convertView.findViewById(R.id.group_member_size);

        convertView.setTag(h);

        h.group_title.setText(g.getGroup_name());

        if (g.getGroupTotalMembers() == 0) {

            h.group_size.setText("No members in this group");

        } else if (g.getGroupTotalMembers() == 1) {

            h.group_size.setText(g.getGroupTotalMembers() + " member in this group");

        } else {

            h.group_size.setText(g.getGroupTotalMembers() + " members in this group");
        }

        return convertView;

    }
}
