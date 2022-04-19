package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PetsCursorAdapter adapter;
    private final static int LOADER_ID = 0;


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

//        here we set the adapter object
//        here we set the cursor as null instead of cursor defined in the displayDatabaseInfo method
//        this is so because we no longer need the get content resolver because Cursor loader uses content resolver intrinsically
//        and we do not have to write code for it from our side.
        adapter = new PetsCursorAdapter(this,null);

//        we define the list view object and attach the adapter to it
        ListView list = (ListView) findViewById(R.id.pets_list);
        list.setAdapter(adapter);

        /*
         * Initializes the CursorLoader. The URL_LOADER value is eventually passed
         * to onCreateLoader().
         */
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }

//    private void displayDatabaseInfo(){
//
////        we define a helper class to access the database
//        PetsDbHelper dbHelper = new PetsDbHelper(this);
////        we obtain a readable format of the database
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
////        here, we define projection in order to know which columns we want returned as query
////        if we don't define it then we get all columns back
//        String projection[] = new String[]{
//                PetsEntry.COLUMN_PETS_ID,
//                PetsEntry.COLUMN_PETS_NAME,
//                PetsEntry.COLUMN_PETS_BREED,
//                PetsEntry.COLUMN_PETS_GENDER,
//                PetsEntry.COLUMN_PETS_WEIGHT
//        };
//
//        /*
//        * we use content resolver here which returns an object of ContentResolver class
//        * then we use query method which returns a cursor object
//        * here, this query method then calls the query method of the respective provider and asks for data
//        * query method of the provider then returns a cursor which is passed on to this query method
//        * selection ans selectionArgs specifies the WHERE clause
//        * selection provides the column name and a place holder like "=?"
//        * selectionArgs provide the value for the placeholder
//        * Cursor is like ArrayList, it is used to store data; ArrayList we stored any type of data but here we specifically store db data
//        * Cursor also has many methods like ArrayList to navigate through db data
//        * when Cursor is completed me call Cursor.close()
//         */
//        Cursor cursor = getContentResolver().query(PetsEntry.CONTENT_URI,projection,null,null,null);
//
////        here we set the adapter object
//        adapter = new PetsCursorAdapter(this,cursor);
//
////        we define the list view object and attach the adapter to it
//        ListView list = (ListView) findViewById(R.id.pets_list);
//        list.setAdapter(adapter);
//
//
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        here, we define projection in order to know which columns we want returned as query
//        if we don't define it then we get all columns back
        String projection[] = new String[]{
                PetsEntry.COLUMN_PETS_ID,
                PetsEntry.COLUMN_PETS_NAME,
                PetsEntry.COLUMN_PETS_BREED
        };

        switch (id) {
            case LOADER_ID:
                // Returns a new CursorLoader
                return new CursorLoader(
                        this,   // Parent activity context
                        PetsEntry.CONTENT_URI,        // Table to query
                        projection,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        we update the adapter with new data which we receive after querying the cursor loader
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}