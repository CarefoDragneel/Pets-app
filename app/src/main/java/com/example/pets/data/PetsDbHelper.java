package com.example.pets.data;

import android.content.Context;
import android.database.ContentObservable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.pets.data.PetsContract.PetsEntry;

/*
* this class is to extend SQLiteOpenHelper class
* SQLiteOpenHelper class is used to makes database and makes it easy to update it
* if the data changes
 */
public class PetsDbHelper extends SQLiteOpenHelper {

    public final static int  DATABASE_VERSION = 1;
    public final static String DATABASE_NAME ="Shelter.db";

    public PetsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    * this is the method which is called when the database is created
    * here, we pass the sql command to create a table
    * {@param:db) object of readable form of the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String DATABASE_CREATE_TABLE_COMMAND = "CREATE TABLE " + PetsEntry.TABLE_NAME + "("
                + PetsEntry.COLUMN_PETS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetsEntry.COLUMN_PETS_NAME + " TEXT NOT NULL,"
                + PetsEntry.COLUMN_PETS_BREED + " TEXT,"
                + PetsEntry.COLUMN_PETS_GENDER + " INTEGER,"
                + PetsEntry.COLUMN_PETS_WEIGHT + " INTEGER"
                + " );" ;

//        this method is used to convert the string into a SQL command
//        not used with SELECT command as it does not return anything
//        only used for changing the database
        db.execSQL(DATABASE_CREATE_TABLE_COMMAND);
    }

//    this method is called when the schema of the database is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE FROM "+ PetsEntry.TABLE_NAME);
        onCreate(db);
    }
}
