package mobidev.com.notesfeed;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shaun on 7/30/2016.
 */
public class NotesFeedSession {

    private Context context;
    private SharedPreferences.Editor editor;
    public final String SHARED_PREFERENCES = "Session";
    public final String SESSION_USER_ID = "userId";
    public final String SESSION_USER_NAME = "user_fullname";

    public NotesFeedSession(Context context) {
        this.context = context;
        this.editor = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).edit();
    }

    public void endUserSession() {
        editor.clear();
        editor.commit();
    }

    public void startUserSession(String userId, String fullname) {
        editor.putString(SESSION_USER_ID, userId);
        editor.putString(SESSION_USER_NAME, fullname);
        editor.commit();
    }

    public void editUserSession(String userId, String fullname) {

        if (!context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_ID, null).equals(null) || !context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_NAME, null).equals(null)) {

            editor.putString(SESSION_USER_ID, userId);
            editor.putString(SESSION_USER_NAME, fullname);
            editor.commit();

        } else {
            endUserSession();
        }
    }
}
