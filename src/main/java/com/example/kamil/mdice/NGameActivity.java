package com.example.kamil.mdice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NGameActivity extends AppCompatActivity {

    int numberOfAllDices;
    int numberOfChosenDices = 0;
    String nameOfGame;
    DataBaseHelper myDb;
    int ids[];
    int drawables[];
    ImageView[] diceImagesS;
    FragmentTransaction transaction;
    LastResultFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shPFileName), MODE_PRIVATE);
        numberOfAllDices = sharedPreferences.getInt(getString(R.string.number_of_dices_text), 2);
        nameOfGame = sharedPreferences.getString(getString(R.string.name_of_game), "Catan");

        fragment = new LastResultFragment(numberOfAllDices);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.lastResultFrame, fragment);
        transaction.hide(fragment);
        transaction.commit();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void click(View view) {

        ids = new int[]{R.id.diceImageS1, R.id.diceImageS2, R.id.diceImageS3, R.id.diceImageS4, R.id.diceImageS5, R.id.diceImageS6};
        drawables = new int[]{R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6};
        diceImagesS = new ImageView[numberOfAllDices];

        for (int i = 0; i < numberOfAllDices; i++) {
            diceImagesS[i] = findViewById(ids[i]);

        }

        final TextView numberDice1 = (TextView) findViewById(R.id.diceTV1);
        final TextView numberDice2 = (TextView) findViewById(R.id.diceTV2);
        final TextView numberDice3 = (TextView) findViewById(R.id.diceTV3);
        final TextView numberDice4 = (TextView) findViewById(R.id.diceTV4);
        final TextView numberDice5 = (TextView) findViewById(R.id.diceTV5);
        final TextView numberDice6 = (TextView) findViewById(R.id.diceTV6);

        final TextView[] numberDice = new TextView[]{numberDice1, numberDice2, numberDice3, numberDice4, numberDice5, numberDice6};


        switch (view.getId()) {
            case R.id.diceButton1:
                if (numberOfChosenDices < numberOfAllDices) {
                    setValue(numberDice1, getValue(numberDice1) + 1);
                    numberOfChosenDices++;
                }
                break;
            case R.id.diceButton2:
                if (numberOfChosenDices < numberOfAllDices) {
                    setValue(numberDice2, getValue(numberDice2) + 1);
                    numberOfChosenDices++;
                }
                break;
            case R.id.diceButton3:
                if (numberOfChosenDices < numberOfAllDices) {
                    setValue(numberDice3, getValue(numberDice3) + 1);
                    numberOfChosenDices++;
                }
                break;
            case R.id.diceButton4:
                if (numberOfChosenDices < numberOfAllDices) {
                    setValue(numberDice4, getValue(numberDice4) + 1);
                    numberOfChosenDices++;
                }
                break;
            case R.id.diceButton5:
                if (numberOfChosenDices < numberOfAllDices) {
                    setValue(numberDice5, getValue(numberDice5) + 1);
                    numberOfChosenDices++;
                }
                break;
            case R.id.diceButton6:
                if (numberOfChosenDices < numberOfAllDices) {
                    setValue(numberDice6, getValue(numberDice6) + 1);
                    numberOfChosenDices++;
                }
                break;

            case R.id.addButton:
                if (numberOfChosenDices == numberOfAllDices) {
                    myDb = MainActivity.myDb;
                    int[] values = getValues(new TextView[]{numberDice1, numberDice2, numberDice3, numberDice4, numberDice5, numberDice6});
                    myDb.insertData(nameOfGame, values, false);
                    for (int i = 0; i < diceImagesS.length; i++) {
                        diceImagesS[i].setImageResource(drawables[(values[i] - 1)]);
                    }

                    for (int i = 0; i < numberDice.length; i++) {
                        setValue(numberDice[i], 0);
                    }

                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(fragment);
                    transaction.commit();


                    numberOfChosenDices = 0;

                    Toast.makeText(this, "Result saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Select all dices", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.discardButton:
                if (numberOfChosenDices > 0) {
                    for (int i = 0; i < numberDice.length; i++) {
                        setValue(numberDice[i], 0);
                    }
                    numberOfChosenDices = 0;
                }
                break;

        }
    }

    public int[] getValues(TextView[] numberDice) {
        int[] values = new int[numberOfAllDices];
        System.out.println(values.length + "55555");
        int c = 0;
        for (int i = 0; i < numberDice.length; i++) {
            while (getValue(numberDice[i]) > 0) {
                values[c] = i + 1;
                c++;
                setValue(numberDice[i], getValue(numberDice[i]) - 1);
            }
        }
        return values;
    }

    public void setValue(TextView numberDice, int value) {
        numberDice.setText(String.valueOf(value));
    }

    public int getValue(TextView numberDice) {
        return Integer.parseInt(numberDice.getText().toString());
    }
}
