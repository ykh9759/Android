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

public class SaladFragment extends Fragment implements Menu {
    private AddListListener addListListener;
    private Button btnsld1,btnsld2,btnsld3,btnsld4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_salad, container, false);
        btnsld1 = view.findViewById(R.id.btnSld1);
        btnsld2 = view.findViewById(R.id.btnSld2);
        btnsld3 = view.findViewById(R.id.btnSld3);
        btnsld4 = view.findViewById(R.id.btnSld4);
        View.OnClickListener ClickMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnSld1:
                        addListListener.onAddList(sld[0],money3[0]); break;
                    case R.id.btnSld2:
                        addListListener.onAddList(sld[1],money3[1]); break;
                    case R.id.btnSld3:
                        addListListener.onAddList(sld[2],money3[2]); break;
                    case R.id.btnSld4:
                        addListListener.onAddList(sld[3],money3[3]); break;
                }
            }
        };
        btnsld1.setOnClickListener(ClickMenu);
        btnsld2.setOnClickListener(ClickMenu);
        btnsld3.setOnClickListener(ClickMenu);
        btnsld4.setOnClickListener(ClickMenu);

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
