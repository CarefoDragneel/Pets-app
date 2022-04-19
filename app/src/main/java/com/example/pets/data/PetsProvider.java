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

import com.example.pets.CatalogActivity;
import com.example.pets.data.PetsContract.PetsEntry;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
* this is a Content provider class; we create this because ContentProvider is an abstract class
* this class is basically used to give access to read as well as manipulate the database to current app as well as outside apps
* it takes in the uri to distinguish which table to access or which specific part of the table to access
* it also uses the Cursor Loader class to enable multi threading
* there are many functions of using content providers which otherwise we will have to implement     manually
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

//        Here, the CatalogActivity UI is not being updated when there a change in the database
//        in order to change the UI we are needed to restart the app
//        so in order to make the CatalogActivity reload when there is some kind of change in the database we set notification on the URI which is changed
//        If the data at this URI changes, then we know we need to update the Cursor.
//        here, uri (in the param) is being watched through the content resolver
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
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

        /*
         * here in order to prevent the insertion of wrong data we create validity checks
         * here we use the methods of ContentValue class to get
         */
        String name = values.getAsString(PetsEntry.COLUMN_PETS_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Check that the gender is valid
        Integer gender = values.getAsInteger(PetsEntry.COLUMN_PETS_GENDER);
        if (gender == null || !PetsEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(PetsEntry.COLUMN_PETS_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }


        SQLiteDatabase db = pets_database_helper.getWritableDatabase();

//        below method returns the id of the row which is inserted in the table
        long id = db.insert(PetsEntry.TABLE_NAME, null, values);

        if(id==-1){
            Log.e(LOG_TAG,"Data not inserted in uri: "+uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri,id);
    }

    /*
    * Update method in content provider helps in updating the data in the database
    * it returns the number of rows that were updated
    * it has the same return type when used in SQLiteDatabase class
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, values, selection, selectionArgs);
            case PETS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        /*
        * here we check the entry values
        * containsKey check if the content value has key-data pair of the name specified in the parameter is there or not
         */
//        check that the name value is valid
        if (values.containsKey(PetsEntry.COLUMN_PETS_NAME)) {
            String name = values.getAsString(PetsEntry.COLUMN_PETS_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // check that the gender value is valid.
        if (values.containsKey(PetsEntry.COLUMN_PETS_GENDER)) {
            Integer gender = values.getAsInteger(PetsEntry.COLUMN_PETS_GENDER);
            if (gender == null || !PetsEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        // check that the weight value is valid.
        if (values.containsKey(PetsEntry.COLUMN_PETS_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(PetsEntry.COLUMN_PETS_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = pets_database_helper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(PetsEntry.TABLE_NAME, values, selection, selectionArgs);

        if(rowsUpdated!=0){
            // Notify all listeners that the data has changed for the pet content URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    /*
    * performs the delete operations on the database
    * it returns the number of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = pets_database_helper.getWritableDatabase();

        // get rows deleted
        int rowsDeleted=0;

        final int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PETS_ID:
                // Delete a single row given by the ID in the URI
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if(rowsDeleted != 0){
            // Notify all listeners that the data has changed for the pet content URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}



