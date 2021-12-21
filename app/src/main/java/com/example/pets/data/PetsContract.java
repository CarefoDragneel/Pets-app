package com.example.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

/*
* this class contains the column names and info about the database
* it also stores the URI
* This class is basically a blueprint for the database
 */
public final class PetsContract {

    /*
    * we define a private constructor so that nobody can make an object of this class
     */
    private PetsContract(){}

    /*
    * Here, we are creating the URI which will be used to access the database data
    * we are not hard coding the different parts of the URI so that they can be accessed independently
    * here, these are made global because these are fixed URI contents which should be present in a URI to access
    * data from this database
    * except path which is table name only
     */
    public final static String CONTENT_AUTHORITY = "com.example.pets";

    public final static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_PETS = "pets";

    /*
    * Basically, this inner class is like a blueprint(contract) of the database table
    * an inner class to store values for a table
    * BaseColumns is an interface which has two constants only
    * we use to automate the id of the pets as well as let the content provider know which field
    * is the id field
     */
    public static final class PetsEntry implements BaseColumns {

        /*
        * Here, we create the final URI which will be used to access the table
        * we make a content uri here instead of making it global variable because this uri is used to access this table only
        * table which is specified by this inner class
         */
        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

//        we specify the table name
        public final static String TABLE_NAME = "pets";

//        here, store the id of the pets
        public final static String COLUMN_PETS_ID = BaseColumns._ID;

//        here, store the name of Name column
        public final static String  COLUMN_PETS_NAME = "name";

//        here, store the name of Breed column
        public final static String COLUMN_PETS_BREED = "breed";

//        here, store the name of Gender column
        public final static String COLUMN_PETS_GENDER = "gender";

//        here, store the name of the Weight column
        public final static String COLUMN_PETS_WEIGHT = "weight";

        /*
        * variables for the different gender options
         */
        public final static int GENDER_UNKNOWN = 0;
        public final static int GENDER_MALE = 1;
        public final static int GENDER_FEMALE = 2;
    }
}
