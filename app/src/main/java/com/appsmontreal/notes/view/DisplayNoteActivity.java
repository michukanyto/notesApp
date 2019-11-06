package com.appsmontreal.notes.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appsmontreal.notes.R;
import com.appsmontreal.notes.model.Note;
import com.appsmontreal.notes.view.MainActivity;

public class DisplayNoteActivity extends AppCompatActivity {
    private String note;
    private String returnNote;
    private Intent intent;
    private Intent returnIntent;
    private TextView noteTextView;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        intent = getIntent();
        returnIntent = new Intent();
//        note = intent.getSerializableExtra(MainActivity.NAME_NOTE));
        note = intent.getStringExtra(MainActivity.NAME_NOTE);
        Log.i("display --------------------> ", note + "yyyy");
        noteTextView = findViewById(R.id.noteTextView);
        getNoteText();
        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdates();
            }
        });
    }

    private void checkUpdates() {
        if (!noteTextView.getText().toString().equals(note)) {
            returnNote = noteTextView.getText().toString();
            returnIntent.putExtra(MainActivity.NOTE_UPDATED,returnNote);
            setResult(MainActivity.RESULT_OK,returnIntent);
            Log.i("--------------->", "We are here, check updates");
        }

        finish();
    }

    private void getNoteText() {
        noteTextView.setText(note);
    }
}
