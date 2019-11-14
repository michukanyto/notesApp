package com.appsmontreal.notes.controller;

import android.content.Context;

import com.appsmontreal.notes.dao.DAOFactory;
import com.appsmontreal.notes.model.IModelListener;
import com.appsmontreal.notes.dao.INoteDAO;
import com.appsmontreal.notes.model.Note;

public class NoteController {

    private static NoteController noteController;

    private final INoteDAO noteDAO;
    private int nextID = 0;

    private NoteController(String dataSource,Context context) {

        noteDAO = DAOFactory.getNoteDAO(dataSource,context);
    }

    public static NoteController getInstance(String dataSource, Context context){//Singleton
        if (noteController == null) {
            noteController = new NoteController(dataSource,context);
        }
        return noteController;
    }

    public Note createNote(String text){
        return new Note(nextID++, text);
    }

    public Note readNote(int id) {
        return noteDAO.getNoteById(id);
    }

    public void readAllNotes(IModelListener daoListener) {
         noteDAO.getAllNotes(daoListener);
    }

    public boolean updateNote(int id, String text) {
        Note noteToUpdate = readNote(id);
        noteToUpdate.setText(text);
        return noteDAO.updateNote(noteToUpdate);
    }

    public boolean deleteNote (int id) {
        return noteDAO.deleteNoteById(id);
    }

    public boolean saveNote(Note note) {
        return noteDAO.insertNote(note);
    }
}
