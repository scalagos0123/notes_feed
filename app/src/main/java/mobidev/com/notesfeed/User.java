package mobidev.com.notesfeed;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * Created by Shaun on 7/28/2016.
 */
public class User implements Serializable {

    private String name;
    private String userId;
    private Context context;
    private SharedPreferences session;
    public static final String SHARED_PREFERENCES = "Session";
    public static final String SESSION_USER_ID = "userId";
    public static final String SESSION_USER_NAME = "user_fullname";

    public User(Context context, String userId, String name) {
        this.name = name;
        this.userId = userId;
        this.context = context;
    }

    public String getName() {
        session = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE);
        name = session.getString(SESSION_USER_NAME, null);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        session = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE);
        userId = session.getString(SESSION_USER_ID, null);
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void endUserSession() {
        session = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.clear();
        editor.commit();
    }

    public void startUserSession() {
        session = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putString(SESSION_USER_ID, this.userId);
        editor.putString(SESSION_USER_NAME, this.name);
        editor.commit();
    }

}
