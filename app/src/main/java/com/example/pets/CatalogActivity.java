package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pets.data.PetsContract.PetsEntry;
import com.example.pets.data.PetsCursorAdapter;
import com.example.pets.data.PetsDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class CatalogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        /*
        * here we make an object to access the floating button in the layout
        * then we attach an event listener to it
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                adding an intent to take us to EditorActivity
                Intent fab_intent = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(fab_intent);
            }
        });

        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo(){

//        we define a helper class to access the database
        PetsDbHelper dbHelper = new PetsDbHelper(this);
//        we obtain a readable format of the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

//        here, we define projection in order to know which columns we want returned as query
//        if we don't define it then we get all columns back
        String projection[] = new String[]{
                PetsEntry.COLUMN_PETS_ID,
                PetsEntry.COLUMN_PETS_NAME,
                PetsEntry.COLUMN_PETS_BREED,
                PetsEntry.COLUMN_PETS_GENDER,
                PetsEntry.COLUMN_PETS_WEIGHT
        };

        /*
        * we use content resolver here which returns an object of ContentResolver class
        * then we use query method which returns a cursor object
        * here, this query method then calls the query method of the respective provider and asks for data
        * query method of the provider then returns a cursor which is passed on to this query method
        * selection ans selectionArgs specifies the WHERE clause
        * selection provides the column name and a place holder like "=?"
        * selectionArgs provide the value for the placeholder
        * Cursor is like ArrayList, it is used to store data; ArrayList we stored any type of data but here we specifically store db data
        * Cursor also has many methods like ArrayList to navigate through db data
        * when Cursor is completed me call Cursor.close()
         */
        Cursor cursor = getContentResolver().query(PetsEntry.CONTENT_URI,projection,null,null,null);

//        here we set the adapter object
        PetsCursorAdapter adapter = new PetsCursorAdapter(this,cursor);

//        we define the list view object and attach the adapter to it
        ListView list = (ListView) findViewById(R.id.pets_list);
        list.setAdapter(adapter);
    }

}