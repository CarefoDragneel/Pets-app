package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    }
}