package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pets.data.PetsContract.PetsEntry;
import com.example.pets.data.PetsDbHelper;

/*
* This class is used to set functionality in the Editor layout
*/
public class EditorActivity extends AppCompatActivity {

    /*
    * we make a global variable of the name field which is entered by the user
     */
    private EditText pet_name;

    /*
     * we make a global variable of the breed field which is entered by the user
     */
    private EditText pet_breed;

    /*
     * we make a global variable of the weight field which is entered by the user
     */
    private EditText pet_weight;
    /*
    * here we make a spinner object reference
     */
    private Spinner pet_gender_spinner;
    /*
    * here we specify the variable which stores the gender of the pet
     */
    private int pet_gender= PetsEntry.GENDER_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        /*
        * we make the object to obtain name entered by the user in the EditText
         */
        pet_name = (EditText) findViewById(R.id.name_field);
        /*
         * we make the object to obtain breed entered by the user in the EditText
         */
        pet_breed = (EditText) findViewById(R.id.breed_field);
        /*
         * we make the object to obtain weight entered by the user in the EditText
         */
        pet_weight = (EditText) findViewById(R.id.weight_field);
        /*
         * we make the spinner object to obtain gender entered by the user in the EditText
         */
        pet_gender_spinner = (Spinner) findViewById(R.id.gender_spinner);

        setupSpinner();

    }


    /*
    * this method is used to add spinner adapter to the spinner
    * this is done so that the spinner can be populated
     */
    public void setupSpinner(){
        /*
        * here we create and adapter object
        * spinner adapter populates the spinner: adds options and add view
        * @param:this - it stores context
        * @param:R.array.array_gender_options - it stores the array which contains the options title
        * @param:android.R.layout.simple_spinner_item - it specifies a default layout of the spinner list of options
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        /*
        * this method is used to set a default layout to the drop down menu
         */
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*
        * here we attach the adapter to the spinner
         */
        pet_gender_spinner.setAdapter(adapter);

        /*
        * here we set the event listener to the spinner
         */
        pet_gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
            overriding the method which specify if any option in the menu is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*
                * we extract string from the adapter view by getting items at each position nad
                * below we set each value on each selection
                 */
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.male))) {
                        pet_gender = PetsEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.female))) {
                        pet_gender = PetsEntry.GENDER_FEMALE; // Female
                    } else {
                        pet_gender = PetsEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }
            /*
            * this specifies if nothing is selected
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pet_gender = PetsEntry.GENDER_UNKNOWN;
            }
        });
    }

    /*
    * this method is sto insert the inputted element by the user in the database
     */
    private void insertData(){

//        creating an object of helper class in order to create a table
        PetsDbHelper dbHelper = new PetsDbHelper(this);

//        obtaining a writable format of the database so that we can insert data into it
        SQLiteDatabase database = dbHelper.getWritableDatabase();

//        obtaining values entered by the user
        String name = pet_name.getText().toString();
        String breed = pet_breed.getText().toString();
        String weight = pet_weight.getText().toString();

//        creating a Content Values object to make key and value pairs
        ContentValues values = new ContentValues();

//        putting values as key value pair in the content provider class
        values.put(PetsEntry.COLUMN_PETS_NAME,name);
        values.put(PetsEntry.COLUMN_PETS_BREED,breed);
        values.put(PetsEntry.COLUMN_PETS_WEIGHT,weight);

//        entering value for gender of the pet
        switch(pet_gender){
            case 0:
                values.put(PetsEntry.COLUMN_PETS_GENDER,PetsEntry.GENDER_UNKNOWN);
                break;
            case 1:
                values.put(PetsEntry.COLUMN_PETS_GENDER,PetsEntry.GENDER_MALE);
                break;
            case 2:
                values.put(PetsEntry.COLUMN_PETS_GENDER,PetsEntry.GENDER_FEMALE);
                break;
        }

        /*
        * here, also we use the content resolver to get insert data into the database using uri
        * same as the query method of content resolver class that we used
        * this also forwards the same insert query to insert method of the content provider
         */
        Uri insertUri = getContentResolver().insert(PetsEntry.CONTENT_URI,values);

//        here we check for all values of uri received
        if(insertUri==null){
            Log.e("EditorActivity","couldn't enter the value");
            Toast.makeText(this, R.string.not_saved, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
        }

    }

    /*
    * This method is used to inflate a menu layout which is defined in the menu resource directory
    * Here, we define an object of MenuInflater class which is used to inflate a menu layout
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        return true;
    }

    /*
    * This method is used add functionality to the option in the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_as_option:
                insertData();
                finish();
                return true;
            case R.id.delete_option:
//                ENTER CODE HERE
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}