package com.example.kamil.mdice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VirtualDicesFragment extends Fragment {

    int numberOfDices;

    public VirtualDicesFragment() {
        this.numberOfDices = 2;
    }

    @SuppressLint("ValidFragment")
    public VirtualDicesFragment(int numberOfDices) {
        this.numberOfDices = numberOfDices;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        switch (numberOfDices) {
            case 1:
                view = inflater.inflate(R.layout.virtual_dices1, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.virtual_dices2, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.virtual_dices3, container, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.virtual_dices4, container, false);
                break;
            case 5:
                view = inflater.inflate(R.layout.virtual_dices5, container, false);
                break;
            case 6:
                view = inflater.inflate(R.layout.virtual_dices6, container, false);
                break;

        }
        return view;
    }
}
