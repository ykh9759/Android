package com.example.remi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class PayActivity extends AppCompatActivity implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener,
        Robot.AsrListener{
    private Robot robot;
    private Button btncard,btncash,btnhome;
    private String table;

    @Override
    protected void onStart() {
        super.onStart();
        robot.getInstance().addOnRobotReadyListener(this);
        robot.getInstance().addNlpListener(this);
        robot.getInstance().addOnBeWithMeStatusChangedListener(this);
        robot.getInstance().addOnGoToLocationStatusChangedListener(this);
        robot.getInstance().addConversationViewAttachesListenerListener(this);
        robot.getInstance().addWakeupWordListener(this);
        robot.getInstance().addTtsListener(this);
        robot.getInstance().addOnLocationsUpdatedListener(this);
        robot.addAsrListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.getInstance().removeOnRobotReadyListener(this);
        robot.getInstance().removeNlpListener(this);
        robot.getInstance().removeOnBeWithMeStatusChangedListener(this);
        robot.getInstance().removeOnGoToLocationStatusChangedListener(this);
        robot.getInstance().removeConversationViewAttachesListenerListener(this);
        robot.getInstance().removeWakeupWordListener(this);
        robot.getInstance().removeTtsListener(this);
        robot.getInstance().removeOnLocationsUpdateListener(this);
        robot.removeAsrListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        robot = Robot.getInstance();
        btncard = findViewById(R.id.btnCard);
        btncash = findViewById(R.id.btnCash);
        btnhome = findViewById(R.id.btnHome);
        Intent it = getIntent();
        table = it.getStringExtra("pTable");

        btncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),PopupActivity.class);
                it.putExtra("Table",table);
                startActivity(it);
            }
        });

        btncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robot.speak(TtsRequest.create("현금결제는 카운터에서 부탁드립니다", true));
            }
        });

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onTtsStatusChanged(@NotNull TtsRequest ttsRequest) {

    }

    @Override
    public void onPublish(@NotNull ActivityStreamPublishMessage activityStreamPublishMessage) {

    }

    @Override
    public void onAsrResult(@NotNull String s) {

    }

    @Override
    public void onConversationAttaches(boolean b) {

    }

    @Override
    public void onNlpCompleted(@NotNull NlpResult nlpResult) {

    }

    @Override
    public void onWakeupWord(@NotNull String s, int i) {

    }

    @Override
    public void onBeWithMeStatusChanged(@NotNull String s) {

    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String s, @NotNull String s1, int i, @NotNull String s2) {

    }

    @Override
    public void onLocationsUpdated(@NotNull List<String> list) {

    }
}
