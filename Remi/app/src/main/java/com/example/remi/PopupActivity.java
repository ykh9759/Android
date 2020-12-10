package com.example.remi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;

import com.example.remi.php.NetworkUtil;
import com.example.remi.php.PHPRequest;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.List;

public class PopupActivity extends Activity implements
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
    public static final String HOME_BASE_LOCATION = "home base";
    private RatingBar ratingBar;
    private float score;
    private String table;
    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        ratingBar = findViewById(R.id.ratingBar);
        NetworkUtil.setNetworkPolicy();
        robot= Robot.getInstance();

        Intent it = getIntent();
        table = it.getStringExtra("Table");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    score = rating;
                }
        });
    }


    public void OnClose(View v){
        //팝업 종료
        try {
            PHPRequest request = new PHPRequest("http://ci2020remi.dongyangmirae.kr/yeomkyounghoon/Data_insert5.php");
            request.PhPtest(table, String.valueOf(score));
            robot.speak(TtsRequest.create("결제가 완료 되었습니다. 감사합니다.",true));
            Intent it = new Intent(getApplicationContext(),BoardActivity.class);
            startActivity(it);
            robot.goTo(HOME_BASE_LOCATION);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //바깥 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        //백버튼 막기
        return;
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
    public void onTtsStatusChanged(@NotNull TtsRequest ttsRequest) {

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

    @Override
    public void onRobotReady(boolean b) {

    }
}