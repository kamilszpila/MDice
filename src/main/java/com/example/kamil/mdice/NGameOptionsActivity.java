package com.example.kamil.mdice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class NGameOptionsActivity extends AppCompatActivity {

    int numberOfDices;
    String nameOfGame;
    DataBaseHelper myDb;
    Spinner spinner;
    Button addGame;
    Button plusButton;
    Button minusButton;
    EditText nameOfGameET;
    TextView numberOfDicesTV;

    ArrayList<String> names;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngame_options);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = MainActivity.myDb;
        spinner = findViewById(R.id.spinner);

        names = myDb.getAllData("MDiceGames", "NAME", "NAME");
        names.add("Add Game");
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        spinner.setAdapter(adapter);

        addGame = findViewById(R.id.addGameButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);

        nameOfGameET = findViewById(R.id.nameOfGameET);
        numberOfDicesTV = findViewById(R.id.numberDicesTV);


        setChooseOptions(new View[]{addGame, plusButton, minusButton, nameOfGameET}, new View[]{plusButton, minusButton, addGame});

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == (names.size() - 1)) {
                    setAddOptions(new View[]{addGame, plusButton, minusButton, nameOfGameET}, new View[]{plusButton, minusButton, addGame});
                    nameOfGameET.setText("Name");
                    numberOfDicesTV.setText(String.valueOf(2));
                } else {
                    setChooseOptions(new View[]{addGame, plusButton, minusButton, nameOfGameET}, new View[]{plusButton, minusButton, addGame});
                    ArrayList<String> numbers = myDb.getAllData("MDiceGames", "NUMBER", "NAME");
                    nameOfGame = names.get(position);
                    numberOfDices = Integer.parseInt(numbers.get(position));
                    nameOfGameET.setText(nameOfGame);
                    numberOfDicesTV.setText(String.valueOf(numberOfDices));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void click(View view) {

        numberOfDices = Integer.parseInt(numberOfDicesTV.getText().toString());
        nameOfGame = nameOfGameET.getText().toString().toUpperCase();

        switch (view.getId()) {
            case R.id.plusButton:
                if (numberOfDices < 6) {
                    numberOfDices++;
                    numberOfDicesTV.setText(String.valueOf(numberOfDices));
                }
                break;
            case R.id.minusButton:
                if (numberOfDices > 1) {
                    numberOfDices--;
                    numberOfDicesTV.setText(String.valueOf(numberOfDices));
                }
                break;
            case R.id.addGameButton:
                myDb = MainActivity.myDb;
                myDb.insertGame(nameOfGame, numberOfDices);
                names = myDb.getAllData("MDiceGames", "NAME", "NAME");
                names.add("Add Game");
                adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
                spinner.setAdapter(adapter);
                break;
            case R.id.startButton:
                myDb = MainActivity.myDb;
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shPFileName), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.name_of_game), nameOfGame);
                editor.putInt(getString(R.string.number_of_dices_text), numberOfDices);
                int gameID = myDb.getLastGameID(nameOfGame, false);
                gameID++;
                editor.putInt(nameOfGame, gameID);
                editor.commit();
                myDb.createTable(nameOfGame, numberOfDices, false);

                Intent intent = new Intent(NGameOptionsActivity.this, NGameActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void setAddOptions(View[] views, View[] buttons) {
        for (int i = 0; i < views.length; i++) {
            views[i].setEnabled(true);
        }
        for (int i = 0; i < buttons.length; i++) {
            views[i].setVisibility(View.VISIBLE);
        }
    }

    public void setChooseOptions(View[] views, View[] buttons) {
        for (int i = 0; i < views.length; i++) {
            views[i].setEnabled(false);
        }
        for (int i = 0; i < buttons.length; i++) {
            views[i].setVisibility(View.INVISIBLE);
        }
    }
}
