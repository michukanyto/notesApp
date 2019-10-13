package com.appsmontreal.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.appsmontreal.notes.Serializer.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "DATA";
    public static final String NAME_NOTE = "NOTE" ;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private ObjectSerializer objectSerializer;
    private SharedPreferences.Editor editor;
    private String note;
    private ArrayList<String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this,CreateNoteActivity.class);
        sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        notes = new ArrayList<>();
    }

    //To inflate menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.addNote:
                startActivityForResult(intent,911);
                break;

            case R.id.exit:
                finish();
                break;

            default:
                break;
        }

        return true;
    }

    //Handling the note that's coming from CreateNoteActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 911){
            if (resultCode == RESULT_OK) {
                note = data.getStringExtra(NAME_NOTE);
                Log.i("----------------->", note);
                notes.add(note);
            }
        }
        saveNotes();

    }

    private void saveNotes() {
        try {
            editor.putString("notes", objectSerializer.serialize(note)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
         reloadList();
    }

    private void reloadList() {
    }
}
