package mobidev.com.notesfeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Shaun on 8/2/2016.
 */
public class GroupNotes extends Fragment {
    private Group g = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GroupActivity activity = (GroupActivity) getActivity();
        g = activity.getGroupData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println(g.getGroup_name() + " id: " + g.getGroup_id());
        View view = inflater.inflate(R.layout.group_notes_tab,container, false);
        return view;
    }
}
