package com.corpogas.corpoapp.Corte.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corpogas.corpoapp.R;


public class FajillasBilletesFragment extends Fragment {

   View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fajillas_billetes, container, false);

        return view;
    }
}