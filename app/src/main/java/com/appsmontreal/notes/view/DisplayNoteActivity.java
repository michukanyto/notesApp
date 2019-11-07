package com.appsmontreal.notes.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appsmontreal.notes.R;
import com.appsmontreal.notes.controller.NoteController;
import com.appsmontreal.notes.model.Note;
import com.appsmontreal.notes.view.MainActivity;

public class DisplayNoteActivity extends AppCompatActivity {
    private Note note;
//    private Intent intent;
    private TextView noteTextView;
    private Button closeButton;
    private NoteController noteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        noteController = NoteController.getInstance("sqlite",getApplicationContext());
//        intent = getIntent();
        note = (Note) getIntent().getSerializableExtra(MainActivity.NAME_NOTE);
//        note = intent.getStringExtra(MainActivity.NAME_NOTE);
        Log.i("display --------------------> ",  note.getText());
        noteTextView = findViewById(R.id.noteTextView);
        noteTextView.setText(note.getText());
        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!note.getText().equals(noteTextView.getText())) {
                    String noteText = String.valueOf(noteTextView.getText());
                    boolean isItUpdated = noteController.updateNote(note.getId(), noteText);

                    if(isItUpdated) {
                        Toast.makeText(getApplicationContext(),"Note was updated.",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Note was NOT updated", Toast.LENGTH_LONG).show();
                    }
                }
                finish();
            }
        });
    }


}
