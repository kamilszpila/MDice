package com.example.kamil.mdice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ResultsFragment extends Fragment {

    static Context appContext;
    static int typeOfResult;
    SharedPreferences sharedPreferences;

    DataBaseHelper myDb;
    GraphView graphView;
    BarGraphSeries<DataPoint> barSeries;
    PointsGraphSeries lineSeries;
    DataPoint[] dataPoints;

    int numberOfAllDices;
    String nameOfGame;
    String gameID;
    boolean isVirtual;
    int countDices[], sumDices[], ids[], imageIds[], drawables[];
    ArrayList<String> types, dates, names, results, gameIDs;
    ArrayAdapter<String> typeAdapter, nameAdapter, idAdapter;
    Spinner typeSpinner, nameSpinner, idSpinner;
    ListView listView;
    String[] columns;

    public ResultsFragment() {
    }

    public static ResultsFragment newInstance(int position, Context context) {
        ResultsFragment fragment = new ResultsFragment();
        typeOfResult = position;
        appContext = context;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        switch (typeOfResult) {
            case 0:
                view = inflater.inflate(R.layout.results_all, container, false);
                imageIds = new int[]{R.id.diceResultS1, R.id.diceResultS2, R.id.diceResultS3, R.id.diceResultS4, R.id.diceResultS5, R.id.diceResultS6};
                drawables = new int[]{R.drawable.dice_1_small, R.drawable.dice_2_small, R.drawable.dice_3_small, R.drawable.dice_4_small, R.drawable.dice_5_small, R.drawable.dice_6_small};
                columns = new String[]{"DICE_1", "DICE_2", "DICE_3", "DICE_4", "DICE_5", "DICE_6"};
                listView = view.findViewById(R.id.listView);
                break;
            case 1:
                view = inflater.inflate(R.layout.results_graph, container, false);
                graphView = view.findViewById(R.id.graph);
                break;
            case 2:
                view = inflater.inflate(R.layout.results_graph, container, false);
                graphView = view.findViewById(R.id.graph);
                break;
        }

        typeSpinner = view.findViewById(R.id.gameTypeSpinner);
        nameSpinner = view.findViewById(R.id.gameNameSpinner);
        idSpinner = view.findViewById(R.id.gameIDSpinner);

        myDb = MainActivity.myDb;

        sharedPreferences = appContext.getSharedPreferences(String.valueOf(getString(R.string.shPFileName)), MODE_PRIVATE);
        numberOfAllDices = sharedPreferences.getInt(getString(R.string.number_of_dices_text), 2);
        nameOfGame = sharedPreferences.getString(getString(R.string.name_of_game), "CATAN");

        names = myDb.getAllData("MDiceGames", "NAME", "NAME");
        nameAdapter = new ArrayAdapter<>(appContext, R.layout.support_simple_spinner_dropdown_item, names);
        nameSpinner.setAdapter(nameAdapter);

        gameIDs = myDb.getAllGameIDs(nameOfGame, isVirtual);
        gameIDs.add("All");
        idAdapter = new ArrayAdapter<>(appContext, R.layout.support_simple_spinner_dropdown_item, gameIDs);
        idSpinner.setAdapter(idAdapter);

        types = new ArrayList<>();
        types.add("Normal");
        types.add("Virtual");
        typeAdapter = new ArrayAdapter<>(appContext, R.layout.support_simple_spinner_dropdown_item, types);
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    isVirtual = false;
                } else {
                    isVirtual = true;
                }
                gameIDs = myDb.getAllGameIDs(nameOfGame, isVirtual);
                setSpinner(gameIDs);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nameOfGame = names.get(position);
                gameIDs = myDb.getAllGameIDs(nameOfGame, isVirtual);
                numberOfAllDices = myDb.getNumberOfDices(nameOfGame);
                setSpinner(gameIDs);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gameID = gameIDs.get(position);

                switch (typeOfResult) {
                    case 0:
                        dates = myDb.getDataFromGame(nameOfGame, "DATE", gameID, isVirtual);
                        ids = new int[dates.size()];

                        for (int i = 0; i < ids.length; i++) {
                            ids[i] = i + 1;
                        }
                        numberOfAllDices = myDb.getNumberOfDices(nameOfGame);
                        ResultsFragment.CustomAdapter adapter = new ResultsFragment.CustomAdapter(numberOfAllDices);
                        listView.setAdapter(adapter);
                        break;
                    case 1:
                        setGraph();
                        break;
                    case 2:
                        setGraph();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    public void setSpinner(ArrayList<String> gameIds) {
        gameIds.add("All");
        idAdapter = new ArrayAdapter<>(appContext, R.layout.support_simple_spinner_dropdown_item, gameIds);
        idSpinner.setAdapter(idAdapter);
    }

    public DataPoint[] getCountDataPoint() {
        countDices = myDb.countDices(nameOfGame, gameID, isVirtual);
        dataPoints = new DataPoint[countDices.length];
        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(i + 1, countDices[i]);
        }
        return dataPoints;
    }

    public DataPoint[] getSumDataPoint() {
        sumDices = myDb.sumDices(nameOfGame, gameID, isVirtual);
        dataPoints = new DataPoint[sumDices.length];
        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(i + numberOfAllDices, sumDices[i]);
        }
        return dataPoints;
    }

    public DataPoint[] getCountLineDataPoint() {
        countDices = myDb.countDices(nameOfGame, gameID, isVirtual);
        float sum = 0;
        for (int i = 0; i < countDices.length; i++) {
            sum += countDices[i];
        }
        sum /= countDices.length;
        dataPoints = new DataPoint[countDices.length];
        for (int i = 0; i < countDices.length; i++) {
            dataPoints[i] = new DataPoint(i + 1, sum);
        }
        return dataPoints;
    }

    public DataPoint[] getSumLineDataPoint() {
        sumDices = myDb.sumDices(nameOfGame, gameID, isVirtual);
        float sum = 0;
        for (int i = 0; i < sumDices.length; i++) {
            sum += sumDices[i];
        }
        dataPoints = new DataPoint[sumDices.length];
        for (int i = 0; i < sumDices.length; i++) {
            dataPoints[i] = new DataPoint(i + 2, sum * (6 - Math.abs((7 - (i + 2)))) / 36);
        }
        return dataPoints;
    }

    public void setGraph() {

        graphView.removeAllSeries();
        graphView.setTitleColor(Color.BLUE);
        graphView.setTitleTextSize(40);
        graphView.setBackgroundColor(Color.WHITE);

        GridLabelRenderer labelRenderer = graphView.getGridLabelRenderer();
        labelRenderer.setHorizontalAxisTitle("Value");
        labelRenderer.setHorizontalAxisTitleTextSize(30);
        labelRenderer.setVerticalAxisTitle("Frequency");
        labelRenderer.setVerticalAxisTitleTextSize(30);
        labelRenderer.setPadding(20);

        switch (typeOfResult) {
            case 1:
                barSeries = new BarGraphSeries<>(getCountDataPoint());
                lineSeries = new PointsGraphSeries<>(getCountLineDataPoint());
                graphView.setTitle("Frequency of results for dice");
                graphView.addSeries(barSeries);
                graphView.addSeries(lineSeries);
                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMinX(0.5);
                graphView.getViewport().setMaxX(7.5);
                break;
            case 2:
                barSeries = new BarGraphSeries<>(getSumDataPoint());
                graphView.addSeries(barSeries);

                switch (numberOfAllDices) {
                    case 1:
                        lineSeries = new PointsGraphSeries<>(getCountLineDataPoint());
                        graphView.addSeries(lineSeries);
                        graphView.getViewport().setXAxisBoundsManual(true);
                        graphView.getViewport().setMinX(0.5);
                        graphView.getViewport().setMaxX(7.5);
                        break;
                    case 2:
                        lineSeries = new PointsGraphSeries<>(getSumLineDataPoint());
                        graphView.addSeries(lineSeries);
                        graphView.getViewport().setXAxisBoundsManual(true);
                        graphView.getViewport().setMinX(1.5);
                        graphView.getViewport().setMaxX(13.5);
                        break;
                    default:
                        graphView.getViewport().setXAxisBoundsManual(true);
                        graphView.getViewport().setMinX(numberOfAllDices - 0.5);
                        graphView.getViewport().setMaxX(numberOfAllDices + 11.5);
                        break;
                }
                graphView.setTitle("Frequency of sum of dices");
                break;
        }

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(barSeries.getHighestValueY() * 1.5);

        barSeries.setDrawValuesOnTop(true);
        barSeries.setValuesOnTopColor(Color.BLUE);
        barSeries.setValuesOnTopSize(30);
        barSeries.setColor(Color.BLUE);
        barSeries.setSpacing(50);

        lineSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(5);
                canvas.drawLine((float) (x - 20 / typeOfResult), y, (float) (x + 20 / typeOfResult), y, paint);
            }
        });
        lineSeries.setColor(Color.GREEN);

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);

        barSeries.setTitle("Results");
        lineSeries.setTitle("Predicted");
        LegendRenderer legendRenderer = graphView.getLegendRenderer();
        legendRenderer.setVisible(true);
        legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);
        legendRenderer.setBackgroundColor(Color.WHITE);
        legendRenderer.setMargin(30);
    }


    class CustomAdapter extends BaseAdapter {

        TextView idTextView;
        TextView dateTextView;
        ImageView[] diceImages;

        public CustomAdapter(int numberOfDices) {
            this.diceImages = new ImageView[numberOfDices];
        }

        @Override
        public int getCount() {
            return dates.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_row, null);

            idTextView = convertView.findViewById(R.id.idTV);
            dateTextView = convertView.findViewById(R.id.dateTV);

            for (int i = 0; i < numberOfAllDices; i++) {
                diceImages[i] = convertView.findViewById(imageIds[i]);
                diceImages[i].setVisibility(View.VISIBLE);
                results = myDb.getDataFromGame(nameOfGame, columns[i], gameID, isVirtual);
                diceImages[i].setImageResource(drawables[Integer.parseInt(results.get(position)) - 1]);

            }

            idTextView.setText(String.valueOf(ids[position]));
            dateTextView.setText(String.valueOf(dates.get(position)));

            return convertView;
        }
    }

}
