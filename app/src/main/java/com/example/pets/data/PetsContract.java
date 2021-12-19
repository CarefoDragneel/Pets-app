package com.example.pets.data;

import android.provider.BaseColumns;

/*
* this class contains the column names and info about the database
* it also stores the URI
 */
public final class PetsContract {

    /*
    * we define a private constructor so that nobody can make an object of this class
     */
    private PetsContract(){}

    /*
    * an inner class to store values for a table
    * BaseColumns is an interface which has two constants only
    * we use to automate the id of the pets as well as let the content provider know which field
    * is the id field
     */
    public final class PetsEntry implements BaseColumns {

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
