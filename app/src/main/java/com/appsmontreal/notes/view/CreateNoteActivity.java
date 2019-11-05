package com.appsmontreal.notes.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsmontreal.notes.R;
import com.appsmontreal.notes.controller.NoteController;
import com.appsmontreal.notes.model.Note;
import com.appsmontreal.notes.view.MainActivity;

public class CreateNoteActivity extends AppCompatActivity {

    private final NoteController noteController = NoteController.getInstance("sqlite",this);
    private EditText noteEditText;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteEditText = findViewById(R.id.noteEditText);
                Note note = noteController.createNote(noteEditText.getText().toString());
                Log.i("==============> ", note.toString());
                boolean isItSaved = noteController.saveNote(note);

                if (isItSaved) {
                    Toast.makeText(view.getContext(),"Note saved successfully",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(view.getContext(),"Note was not saved",Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

    }
}
