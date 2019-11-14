package com.appsmontreal.notes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.appsmontreal.notes.model.IModelListener;
import com.appsmontreal.notes.model.Note;

import java.util.ArrayList;

public class SQLiteNote1DAO extends SQLiteOpenHelper implements INoteDAO {
    public static final String DB_NAME = "Notes.db";
    private final String TABLE_NAME = "note";
    public static final String NOTE_ID = "id";
    public static final String NOTE_TEXT = "text";
    SQLiteDatabase myDB;


    public SQLiteNote1DAO(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("==============> ", "Constructor");
        myDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + NOTE_ID +" INTEGER PRIMARY KEY, " + NOTE_TEXT + " TEXT)");
        Log.i("==============> ", "Oncreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    @Override
    public Note getNoteById(int id) {
        try {
            Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE  id = " + id,null);
            if (cursor != null) {
                int indexID = cursor.getColumnIndex(NOTE_ID);
                int indexText = cursor.getColumnIndex(NOTE_TEXT);
                cursor.moveToFirst();

                int idOnNote = cursor.getInt(indexID);
                String textOnNote = cursor.getString(indexText);
                Note note = new Note(idOnNote,textOnNote);
                return note;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void getAllNotes(IModelListener daoListener) {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        int indexID = cursor.getColumnIndex(NOTE_ID);
        int indexText = cursor.getColumnIndex(NOTE_TEXT);

        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            int idOnNote = cursor.getInt(indexID);
            String textOnNote = cursor.getString(indexText);
            notes.add(new Note(idOnNote,textOnNote));
        }

        daoListener.onGetAllNotes(notes);
    }


    @Override
    public boolean deleteNoteById(int id) {
        int reply = myDB.delete(TABLE_NAME, "id = ?", new String[] {Integer.toString(id)});

        if (reply == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateNote(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TEXT,note.getText());
        myDB.update(TABLE_NAME,contentValues," = ?",new String[] {Integer.toString(note.getId())});
        return true;
    }

    @Override
    public boolean insertNote(Note note) {
        ContentValues contentValues = new ContentValues();
        Log.i("==============> ", note.getText() + "  at SQL insert");
        contentValues.put(NOTE_TEXT,note.getText());
        long noteInsertedOK = myDB.insert(TABLE_NAME,NOTE_TEXT,contentValues);//If note is not inserted it returns -1

        if (noteInsertedOK != -1) {
            return true;
        } else {
            return false;
        }

    }
}
