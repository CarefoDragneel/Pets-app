package com.example.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.pets.data.PetsContract.PetsEntry;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
* this is a Content provider class; we create this because ContentProvider is an abstract class
* this class is basically used to give access to read as well as manipulate the database
* it takes in the uri to distinguish which table to access or which specific part of the table to access
* it also used the Cursor Loader class to enable multi threading
 */
public class PetsProvider extends ContentProvider {

//    this is used to get simple name of the class
    private static final String LOG_TAG = PetsProvider.class.getSimpleName();

    /*
    * Here, we create an object of UriMatcher class which is used to match the incoming uris and
    * direct them in to the correct source
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//    here we create key to distinguish between different uri paths
    private static final int PETS = 1;
    private static final int PETS_ID = 0;

    /*
    * addURI() - method is used to create options to match different uri
     */
    static {
        uriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, "pets", PETS);
        uriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, "pets/#", PETS_ID);
    }

//    we make it global as this will be used in more than one method
    private PetsDbHelper pets_database_helper;

    @Override
    public boolean onCreate() {
//        we write this object here because we want this to be created as the provider is called
        pets_database_helper = new PetsDbHelper(getContext());
        return true;
    }

    /*
    * query method is used to extract data from the table and retrieve data from the provider
    * this is basically used when we need to read some data from the database
    * it returns a cursor containing the rows and data which is asked for
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

//        we define a readable form of the database
        SQLiteDatabase pets_database = pets_database_helper.getReadableDatabase();

        Cursor cursor = null;

        /*
        * here, functionality of different paths for different forms of URI is created
        * ContentUris is a utility class which is used to perform functions on an uri
         */
        int match = uriMatcher.match(uri);
        switch(match){

            case PETS:
                cursor = pets_database.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs,null,null, sortOrder);
                break;
            case PETS_ID:
                selection = PetsEntry.COLUMN_PETS_ID+"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = pets_database.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs,null,null, sortOrder);
                break;
            default:
                try {
                    throw new IllegalAccessException("Cannot query unknown URI " + uri);
                } catch (IllegalAccessException e) {
                    Log.e("PetsProvider", e.toString());
                }
        }


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /*
    * insert method is used to insert data into the database table
    * it returns the URI of the inserted row in the table which can be null
    * the uri in the parameter is different as this is the uri which is used to access the database where the data
    * needs to be inserted
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = uriMatcher.match(uri);
        switch (match){
            case PETS:
                return insertPets(uri, values);
            default:
                try {
                    throw new IllegalAccessException("Insertion is not supported for" + uri);
                } catch (IllegalAccessException e) {
                    Log.e("PetsProvider", "pet is not inserted correctly");
                    Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                }
        }
        
        return null;
    }

    private Uri insertPets(Uri uri, ContentValues values){

        SQLiteDatabase db = pets_database_helper.getWritableDatabase();

//        below method returns the id of the row which is inserted in the table
        long id = db.insert(PetsEntry.TABLE_NAME, null, values);

        if(id==-1){
            Log.e(LOG_TAG,"Data not inserted in uri: "+uri);
            return null;
        }

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
