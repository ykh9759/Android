package com.example.remi.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.remi.intfc.AddListListener;
import com.example.remi.R;
import com.example.remi.intfc.Menu;

public class DrinkeFragment extends Fragment implements Menu {
    private AddListListener addListListener;
    private Button btncola,btnsider,btnfanta,btnorg,btnpodo,btntomato,btnAA,btnlat,btnlemon,btnjamong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drinke, container, false);
        btncola = view.findViewById(R.id.btnCola);
        btnsider = view.findViewById(R.id.btnSider);
        btnfanta = view.findViewById(R.id.btnFanta);
        btnorg = view.findViewById(R.id.btnOrg);
        btnpodo = view.findViewById(R.id.btnPodo);
        btntomato = view.findViewById(R.id.btnTomato);
        btnAA = view.findViewById(R.id.btnAA);
        btnlat = view.findViewById(R.id.btnlat);
        btnlemon = view.findViewById(R.id.btnlemon);
        btnjamong = view.findViewById(R.id.btnjamong);
        View.OnClickListener ClickMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnCola:
                        addListListener.onAddList(drink[0],money4[0]); break;
                    case R.id.btnSider:
                        addListListener.onAddList(drink[1],money4[1]); break;
                    case R.id.btnFanta:
                        addListListener.onAddList(drink[2],money4[2]); break;
                    case R.id.btnOrg:
                        addListListener.onAddList(drink[3],money4[3]); break;
                    case R.id.btnPodo:
                        addListListener.onAddList(drink[4],money4[4]); break;
                    case R.id.btnTomato:
                        addListListener.onAddList(drink[5],money4[5]); break;
                    case R.id.btnAA:
                        addListListener.onAddList(drink[6],money4[6]); break;
                    case R.id.btnlat:
                        addListListener.onAddList(drink[7],money4[7]); break;
                    case R.id.btnlemon:
                        addListListener.onAddList(drink[8],money4[8]); break;
                    case R.id.btnjamong:
                        addListListener.onAddList(drink[9],money4[9]); break;
                }
            }
        };
        btncola.setOnClickListener(ClickMenu);
        btnsider.setOnClickListener(ClickMenu);
        btnfanta.setOnClickListener(ClickMenu);
        btnorg.setOnClickListener(ClickMenu);
        btnpodo.setOnClickListener(ClickMenu);
        btntomato.setOnClickListener(ClickMenu);
        btnAA.setOnClickListener(ClickMenu);
        btnlat.setOnClickListener(ClickMenu);
        btnlemon.setOnClickListener(ClickMenu);
        btnjamong.setOnClickListener(ClickMenu);

        return view;
    }
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof Activity){
            addListListener = (AddListListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement AddListListener");
        }
    }
    public void onDetach(){
        super.onDetach();
        addListListener = null;
    }
}
