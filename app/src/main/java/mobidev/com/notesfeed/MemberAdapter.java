package mobidev.com.notesfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shaun on 8/20/2016.
 */
public class MemberAdapter extends ArrayAdapter<User> {

    private ArrayList<User> members;
    private LayoutInflater inflater;
    private int resource;

    public MemberAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.inflater = LayoutInflater.from(this.getContext());
        members = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.member_list, parent, false);

        TextView memberName = (TextView) convertView.findViewById(R.id.person_name);
        memberName.setText(members.get(position).getName());

        return convertView;
    }
}
