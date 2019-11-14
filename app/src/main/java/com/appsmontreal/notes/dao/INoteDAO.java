package com.appsmontreal.notes.dao;

import com.appsmontreal.notes.model.IModelListener;
import com.appsmontreal.notes.model.Note;

public interface INoteDAO {

    Note getNoteById(int id);

    void getAllNotes(IModelListener noteDAOListener);

    boolean deleteNoteById(int id);

    boolean updateNote(Note note);

    boolean insertNote(Note note);
}
