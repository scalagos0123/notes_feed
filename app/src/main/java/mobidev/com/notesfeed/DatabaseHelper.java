package mobidev.com.notesfeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Debbie Co on 8/6/2016.
 */
public class DatabaseHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Notes.db";

    public static final String TABLE_NAME = "my_notes";
    public static final String COL_1 = "notes_id";
    public static final String COL_2 = "notes_title";
    public static final String COL_3 = "notes_content";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("+ COL_1 + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+ COL_2 +" TEXT NOT NULL, "+ COL_3 +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


//    insertData parameters should be Notes class only. Merong methods provided sa Notes class to get the id, title, and content

    public boolean insertData(String my_notes, String notes_id, String notes_title,String notes_content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, notes_id);
        contentValues.put(COL_2, notes_title);
        contentValues.put(COL_3, notes_content);
        long result = db.insert(my_notes,null,contentValues);

        if(result== -1)
            return false;
        else
            return true;
    }
}
