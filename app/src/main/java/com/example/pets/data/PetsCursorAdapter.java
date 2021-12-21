package com.example.pets.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.pets.data.PetsContract.PetsEntry;

import com.example.pets.R;

/*
* this class is made to extend CursorAdapter class as it is an abstract class
 */
public class PetsCursorAdapter extends CursorAdapter {

    public PetsCursorAdapter (Context context, Cursor cursor){
        super(context, cursor, 0);
    }
    /*
    * this method is overridden to add functionality when a new view is created
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pets, parent,false);
    }

    /*
    * this method adds functionality when the views are recycled
    * basically what needs to happen with the views
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView pet_name = (TextView) view.findViewById(R.id.pet_name_textbox);
        TextView pet_breed = (TextView) view.findViewById(R.id.pet_breed_textbox);

//        this is used to extract the data from the cursor which are queried out using content provider and uri
//        getColumnIndexOrThrow() method takes in the column name as String and returns the column index as int.
//        getString() method of Cursor class extracts data from the column in the form of String
        String name = cursor.getString(cursor.getColumnIndexOrThrow(PetsEntry.COLUMN_PETS_NAME));
        String breed = cursor.getString(cursor.getColumnIndexOrThrow(PetsEntry.COLUMN_PETS_BREED));
//        here we set the data in the textboxes
        pet_name.setText(name);
        pet_breed.setText(breed);
    }
}
