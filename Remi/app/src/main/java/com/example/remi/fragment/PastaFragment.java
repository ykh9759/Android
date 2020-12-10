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

public class PastaFragment extends Fragment implements Menu {
    AddListListener addListListener;
    Button btnpst1,btnpst2,btnpst3,btnpst4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pasta, container, false);
        btnpst1 = view.findViewById(R.id.btnPst1);
        btnpst2 = view.findViewById(R.id.btnPst2);
        btnpst3 = view.findViewById(R.id.btnPst3);
        btnpst4 = view.findViewById(R.id.btnPst4);
        View.OnClickListener ClickMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnPst1:
                        addListListener.onAddList(pst[0],money2[0]); break;
                    case R.id.btnPst2:
                        addListListener.onAddList(pst[1],money2[1]); break;
                    case R.id.btnPst3:
                        addListListener.onAddList(pst[2],money2[2]); break;
                    case R.id.btnPst4:
                        addListListener.onAddList(pst[3],money2[3]); break;
                }
            }
        };
        btnpst1.setOnClickListener(ClickMenu);
        btnpst2.setOnClickListener(ClickMenu);
        btnpst3.setOnClickListener(ClickMenu);
        btnpst4.setOnClickListener(ClickMenu);

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
