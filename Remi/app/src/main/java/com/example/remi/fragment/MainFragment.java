package com.example.remi.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.remi.intfc.AddListListener;
import com.example.remi.R;
import com.example.remi.intfc.Menu;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainFragment extends Fragment implements Menu{
    private AddListListener addListListener;
    private Button btnstk1,btnstk2,btnstk3,btnstk4;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        btnstk1 = view.findViewById(R.id.btnstk1);
        btnstk2 = view.findViewById(R.id.btnstk2);
        btnstk3 = view.findViewById(R.id.btnstk3);
        btnstk4 = view.findViewById(R.id.btnstk4);
        View.OnClickListener ClickMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnstk1:
                        addListListener.onAddList(stk[0],money1[0]); break;
                    case R.id.btnstk2:
                        addListListener.onAddList(stk[1],money1[1]); break;
                    case R.id.btnstk3:
                        addListListener.onAddList(stk[2],money1[2]); break;
                    case R.id.btnstk4:
                        addListListener.onAddList(stk[3],money1[3]); break;
                }
            }
        };
        btnstk1.setOnClickListener(ClickMenu);
        btnstk2.setOnClickListener(ClickMenu);
        btnstk3.setOnClickListener(ClickMenu);
        btnstk4.setOnClickListener(ClickMenu);

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




