package com.example.kamil.mdice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TypeOfDicesActivity extends AppCompatActivity {

    Button mechButton;
    Button normButton;
    Button virtButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_dices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mechButton = findViewById(R.id.mechButton);
        normButton = findViewById(R.id.normButton);
        virtButton = findViewById(R.id.virtButton);

        mechButton.setEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void click(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.mechButton:
                intent = new Intent(TypeOfDicesActivity.this,MGameOptionsActivity.class);
                startActivity(intent);
                break;
            case R.id.normButton:
                intent = new Intent(TypeOfDicesActivity.this,NGameOptionsActivity.class);
                startActivity(intent);
                break;
            case R.id.virtButton:
                intent = new Intent(TypeOfDicesActivity.this,VGameOptionsActivity.class);
                startActivity(intent);
                break;
        }

    }

}
