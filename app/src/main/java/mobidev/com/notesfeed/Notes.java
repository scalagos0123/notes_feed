package mobidev.com.notesfeed;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kat on 01/08/2016.
 */
public class Notes implements Serializable {
    private int notes_id;
    private String notes_title;
    private String notes_content;
    private String note_owner;


    public Notes(int notes_id, String notes_title, String notes_content) {
        this.notes_id = notes_id;
        this.notes_title = notes_title;
        this.notes_content = notes_content;
        this.note_owner = "";
    }

    public void setNotes_id(int notes_id) {
        this.notes_id = notes_id;
    }

    public void setNotes_title(String notes_title) {
        this.notes_title = notes_title;
    }

    public void setNotes_content(String notes_content) {
        this.notes_content = notes_content;
    }

    public String getNote_owner() {
        return note_owner;
    }

    public void setNote_owner(String note_owner) {
        this.note_owner = note_owner;
    }

    public int getNotes_id() { return notes_id; }

    public String getNotes_title() {
        return notes_title;
    }

    public String getNotes_content() {
        return notes_content;
    }
}
