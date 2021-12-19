package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pets.data.PetsContract.PetsEntry;

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
//                ENTER CODE LATER
                return true;
            case R.id.delete_option:
//                ENTER CODE HERE
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}