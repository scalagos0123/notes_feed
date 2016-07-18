package mobidev.com.notesfeed;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Shaun on 7/19/2016.
 */
public class SignUpUser extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private String name[];

    public SignUpUser(Context context, String name[]) {
        this.name = name;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return null;
    }
}
