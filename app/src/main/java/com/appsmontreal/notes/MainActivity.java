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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appsmontreal.notes.Serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "DATA";
    public static final String NAME_NOTE = "NOTE" ;
    public static final String KEY_NOTE = "KEY_NOTE" ;
    public static final String FORMAT_PATTERN = "yy-mm-dd hh:mm";
    private ListView notesListView;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private ObjectSerializer objectSerializer;
    private SharedPreferences.Editor editor;
    private String note;
    private ArrayList<String> notes;
    private ArrayList<String> titles;
    private Date date;
    private String newNameNote;
    private  DateFormat dateFormat;
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesListView = findViewById(R.id.notesListView);
        intent = new Intent(this,CreateNoteActivity.class);
        sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        notes = new ArrayList<>();
        titles = new ArrayList<>();
        date = new Date();
        dateFormat = new SimpleDateFormat(FORMAT_PATTERN);
        reloadList();

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
                newNameNote = dateFormat.format(Calendar.getInstance().getTime());
                Log.i("----------------->", newNameNote);
                notes.add(note);
            }
        }
        saveNotes();

    }

    private void saveNotes() {
        try {
            editor.putString(KEY_NOTE, objectSerializer.serialize(notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
         reloadList();
    }

    private void reloadList() {
        notes.clear();
        titles.clear();
        try {
            notes = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString(KEY_NOTE,objectSerializer.serialize(new ArrayList<String>())));
            getTitles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("------------>Array", notes.toString());
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,titles);
        notesListView.setAdapter(arrayAdapter);

    }

    private void getTitles() {//to handle a list with just one line
        String[] nameTitles;
        for (String s : notes) {
            nameTitles = s.split("\n");
            titles.add(nameTitles[0]);
        }
    }
}
