package mobidev.com.notesfeed;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kat on 01/08/2016.
 */
public class Notes  implements Serializable {
    private int notes_id;
    private String notes_title;
    private String notes_content;


    public Notes(int notes_id, String notes_title, String notes_content) {
        this.notes_id = notes_id;
        this.notes_title = notes_title;
        this.notes_content = notes_content;
    }

    public int getNotes_id() { return notes_id; }

    public String getNotes_title() {
        return notes_title;
    }

    public String getNotes_content() {
        return notes_content;
    }
}
