package com.example.kamil.mdice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LastResultFragment extends Fragment {

    int numberOfDices;

    public LastResultFragment() {
        this.numberOfDices = 2;
    }

    @SuppressLint("ValidFragment")
    public LastResultFragment(int numberOfDices) {
        this.numberOfDices = numberOfDices;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        switch (numberOfDices) {
            case 1:
                view = inflater.inflate(R.layout.last_result1, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.last_result2, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.last_result3, container, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.last_result4, container, false);
                break;
            case 5:
                view = inflater.inflate(R.layout.last_result5, container, false);
                break;
            case 6:
                view = inflater.inflate(R.layout.last_result6, container, false);
                break;

        }
        return view;
    }
}
