package com.example.kamil.mdice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class VGameActivity extends AppCompatActivity {

    DataBaseHelper myDb;
    Random random;
    int numberOfAllDices;
    String nameOfGame;
    int ids[];
    int drawables[];
    ImageView[] diceImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vgame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shPFileName), MODE_PRIVATE);
        numberOfAllDices = sharedPreferences.getInt(getString(R.string.number_of_dices_text), 2);
        nameOfGame = sharedPreferences.getString(getString(R.string.name_of_game), "CATAN");

        VirtualDicesFragment fragment = new VirtualDicesFragment(numberOfAllDices);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.virtualDicesFrame, fragment);
        transaction.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void click(View view) {

        ids = new int[]{R.id.diceImage1, R.id.diceImage2, R.id.diceImage3, R.id.diceImage4, R.id.diceImage5, R.id.diceImage6};
        drawables = new int[]{R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6};
        diceImages = new ImageView[numberOfAllDices];

        for (int i = 0; i < numberOfAllDices; i++) {
            diceImages[i] = findViewById(ids[i]);

        }

        switch (view.getId()) {
            case R.id.rollButton:
                myDb = MainActivity.myDb;
                int[] values = roll(numberOfAllDices);
                myDb.insertData(nameOfGame, values, true);
                for (int i = 0; i < diceImages.length; i++) {
                    diceImages[i].setImageResource(drawables[(values[i] - 1)]);
                }

                Toast.makeText(this, "Result saved", Toast.LENGTH_SHORT).show();


        }
    }


    public int[] roll(int numberOfAllDices) {

        int[] values = new int[numberOfAllDices];
        random = new Random();
        for (int i = 0; i < numberOfAllDices; i++) {
            values[i] = random.nextInt(6) + 1;
        }
        return values;

    }

}
