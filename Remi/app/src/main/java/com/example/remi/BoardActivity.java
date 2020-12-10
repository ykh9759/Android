package com.example.remi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BoardActivity extends AppCompatActivity implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener,
        Robot.AsrListener {
    public static final String HOME_BASE_LOCATION = "home base";
    public static final String Table_No_1 = "1";
    public static final String Table_No_2 = "2";
    public static final String url = "http://ci2020remi.dongyangmirae.kr/yeomkyounghoon/jsonTables.php";
    public String SaveLocation;
    private Robot robot;
    private String table1,table2,table3,table4;
    private String button1,button2,button3,button4;
    private Timer timer;
    private TimerTask tt;

    @Override
    protected void onStart() {
        super.onStart();
        robot.getInstance().addOnRobotReadyListener(this);
        Robot.getInstance().addNlpListener(this);
        Robot.getInstance().addOnBeWithMeStatusChangedListener(this);
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

    public void btnMenu(View v){
        Intent it = new Intent(getApplicationContext(),MenuActivity.class);
        it.putExtra("Table",SaveLocation);
        startActivity(it);
    }

    public void btnPay(View v){
        Intent it = new Intent(getApplicationContext(),PayActivity.class);
        it.putExtra("pTable",SaveLocation);
        startActivity(it);
    }

    public void btnGo() {
        Intent pay = new Intent(getApplicationContext(), PayActivity.class);
        if (button1.equals("1")) {
            robot.goTo(Table_No_1);
            SaveLocation = Table_No_1;
            pay.putExtra("pTable", SaveLocation);
            startActivityForResult(pay,103);
        }
        if (button1.equals("2")) {
            robot.goTo(Table_No_2);
            SaveLocation = Table_No_2;
            pay.putExtra("pTable", SaveLocation);
            startActivityForResult(pay,103);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        robot = Robot.getInstance();
        timer = new Timer();
    }

    public TimerTask timerTaskMaker(){
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                DownloadTask task = new DownloadTask();
                task.execute(url);
            }
        };
        return TT;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tt = timerTaskMaker();
        timer.schedule(tt,0,5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tt.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();//타이머 종료
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

    class DownloadTask extends AsyncTask<String, Void, String> {

        public void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonObject json = (JsonObject) JsonParser.parseString(s);
            JsonArray jsonArray2 = json.getAsJsonArray("jsonTables");
            try {
                JsonObject store1 = (JsonObject) jsonArray2.get(0);
                table1 = store1.get("table").getAsString();
                button1 = store1.get("button").getAsString();
                JsonObject store2 = (JsonObject) jsonArray2.get(1);
                table2 = store2.get("table").getAsString();
                button2 = store2.get("button").getAsString();
                JsonObject store3 = (JsonObject) jsonArray2.get(2);
                table3 = store3.get("table").getAsString();
                button3 = store3.get("button").getAsString();
                JsonObject store4 = (JsonObject) jsonArray2.get(3);
                table4 = store4.get("table").getAsString();
                button4 = store4.get("button").getAsString();
            }catch (Exception e){
                e.printStackTrace();
            }
            btnGo();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer sBuf = new StringBuffer();

            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                BufferedInputStream bufStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(bufStream, "utf-8"));

                String line = null;
                while ((line = bufreader.readLine()) != null) {
                    sBuf.append(line);
                }
                bufreader.close();
                bufStream.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sBuf.toString();
        }

    }

    @Override
    public void onPublish(@NotNull ActivityStreamPublishMessage activityStreamPublishMessage) {

    }

    @Override
    public void onAsrResult(@NotNull String asrResult) {
        Log.d("onAsrResult", "asrResult = " + asrResult);
        if(asrResult.contains("한 명") || asrResult.contains("혼자")) {
            robot.speak(TtsRequest.create("1인석으로 자리 안내해드리겠습니다.", true));
        }
        if(asrResult.contains("두 명") || asrResult.contains("둘이")) {
            robot.speak(TtsRequest.create("2인석으로 자리 안내해드리겠습니다.", true));
            if(table1.equals("0")) {
                robot.goTo(Table_No_1);
                SaveLocation = Table_No_1;
            }
            else if (table2.equals("0")){
                robot.goTo(Table_No_2);
                SaveLocation = Table_No_2;
            }
            else
                robot.speak(TtsRequest.create("죄송합니다 손님. 현재 비어있는 2인석이 없어 자리가 마련될 때 까지 기다려주십시오.", true));
        }
        if(asrResult.contains("세 명")) {
            robot.speak(TtsRequest.create("3인석으로 자리 안내해드리겠습니다.", true));
        }
        if(asrResult.contains("네 명")) {
            robot.speak(TtsRequest.create("4인석으로 자리 안내해드리겠습니다.", true));
        }
        if(asrResult.contains("다섯 명")) {
            robot.speak(TtsRequest.create("5인석으로 자리 안내해드리겠습니다.", true));
        }
        if(asrResult.contains("여섯 명")) {
            robot.speak(TtsRequest.create("6인석으로 자리 안내해드리겠습니다.", true));
        }

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
}
