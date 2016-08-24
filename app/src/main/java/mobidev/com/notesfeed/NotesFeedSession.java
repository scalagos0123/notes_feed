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
    public final String SESSION_USER_PASSWORD = "user_password";
    public final String SESSION_USER_EMAIL = "user_email";
    public final static String SERVER_ADDRESS = "http://mobidev.azurewebsites.net/";
    public final static String SERVER_ADDRESS_ALTERNATE = "http://SHAUN-G501/";

    public NotesFeedSession(Context context) {
        this.context = context;
        this.editor = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).edit();
    }

    public void endUserSession() {
        editor.clear();
        editor.commit();
    }

    public void startUserSession(String userId, String fullname, String password, String email) {
        editor.putString(SESSION_USER_ID, userId);
        editor.putString(SESSION_USER_NAME, fullname);
        editor.putString(SESSION_USER_EMAIL, email);
        editor.putString(SESSION_USER_PASSWORD, password);
        editor.commit();
    }

    public void editUserSessionName(String fullname) {

        if (!context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_ID, null).equals(null) || !context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_NAME, null).equals(null)) {

            editor.putString(SESSION_USER_NAME, fullname);
            editor.commit();

        } else {
            endUserSession();
        }
    }

    public void editUserSessionPassword(String password) {
        if (!context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_ID, null).equals(null) || !context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_PASSWORD, null).equals(null)) {

            editor.putString(SESSION_USER_PASSWORD, password);
            editor.commit();

        } else {
            endUserSession();
        }
    }

    public void editUserSessionEmail(String email) {
        if (!context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_ID, null).equals(null) || !context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE).getString(SESSION_USER_EMAIL, null).equals(null)) {

            editor.putString(SESSION_USER_EMAIL, email);
            editor.commit();

        } else {
            endUserSession();
        }
    }

    public String getUserFullName() {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(SESSION_USER_NAME, null);
    }

    public String getUserEmail() {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(SESSION_USER_EMAIL, null);
    }

    public String getUserPassword() {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(SESSION_USER_PASSWORD, null);
    }

    public String getUserId() {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(SESSION_USER_ID, null);
    }
}
