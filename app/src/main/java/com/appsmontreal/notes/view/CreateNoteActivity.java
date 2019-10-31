package com.appsmontreal.notes.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsmontreal.notes.R;
import com.appsmontreal.notes.controller.NoteController;
import com.appsmontreal.notes.model.Note;
import com.appsmontreal.notes.view.MainActivity;

public class CreateNoteActivity extends AppCompatActivity {

    private final NoteController noteController = NoteController.getInstance("shared_preferences");
    private EditText noteEditText;
    private Intent returnIntent;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteEditText = findViewById(R.id.noteEditText);
                Note note = noteController.createNote(noteEditText.getText().toString());
                saveButton = findViewById(R.id.saveButton);
                noteController.saveNote(note);
                finish();
            }
        });

        setContentView(R.layout.activity_create_note);
    }
}
