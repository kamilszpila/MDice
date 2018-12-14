package com.example.kamil.mdice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button loadButton;
    static DataBaseHelper myDb;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadButton = findViewById(R.id.loadButton);
        loadButton.setEnabled(false);


        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        }

        myDb = new DataBaseHelper(this);
        myDb.createNameTable();


    }


    public void click(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.playButton:
                intent = new Intent(MainActivity.this, TypeOfDicesActivity.class);
                startActivity(intent);
                break;
            case R.id.loadButton:
                break;


        }
    }


}
