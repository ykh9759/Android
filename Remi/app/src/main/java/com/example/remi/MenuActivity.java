package com.example.remi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.remi.fragment.MainFragment;
import com.example.remi.intfc.AddListListener;
import com.example.remi.intfc.Menu;
import com.example.remi.php.NetworkUtil;
import com.example.remi.php.PHPRequest;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.remi.ui.main.SectionsPagerAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class MenuActivity extends AppCompatActivity implements
        AddListListener,
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener,
        Robot.AsrListener, Menu {
    private ListView listView;
    private SimpleAdapter sAdapter;
    private ArrayList<HashMap<String,String>> listData;
    private HashMap<String, String > item;
    private Toolbar toolbar;
    private String Table;
    private Robot robot;
    public static final String HOME_BASE_LOCATION = "home base";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NetworkUtil.setNetworkPolicy();
        robot= Robot.getInstance();
        robot.speak(TtsRequest.create("메뉴를 선택해주세요",true));

        Intent it = getIntent();
        Table = it.getStringExtra("Table");

        listView = findViewById(R.id.listview);
        listData = new ArrayList<>();
        sAdapter = new SimpleAdapter(this,listData,R.layout.simple_list_item_activated_3,new String[] {"menu","price"}, new int[]{R.id.text1,R.id.text2});
        listView.setAdapter(sAdapter);
    }

    //선택된 주문리스트 db에 전송송
    public void onClickOrder(View v){
        String[] menu = new String[listData.size()];
        String[] price = new String[listData.size()];

        for(int i = 0; i<listData.size(); i++) {
            String a = listData.get(i).toString();
            StringTokenizer aStr = new StringTokenizer(a,",{}");
            price[i] = aStr.nextToken().substring(6);
            menu[i] = aStr.nextToken().substring(6);
        }
        try {
            PHPRequest request = null;
            if(Table.equals("1")) {
                request = new PHPRequest("http://ci2020remi.dongyangmirae.kr/yeomkyounghoon/Data_insert1.php");
            }else if(Table.equals("2")) {
                request = new PHPRequest("http://ci2020remi.dongyangmirae.kr/yeomkyounghoon/Data_insert2.php");
            }else if(Table.equals("3")) {
                request = new PHPRequest("http://ci2020remi.dongyangmirae.kr/yeomkyounghoon/Data_insert3.php");
            }else if(Table.equals("4")){
                request = new PHPRequest("http://ci2020remi.dongyangmirae.kr/yeomkyounghoon/Data_insert4.php");
            }
            for (int i = 0; i < listData.size(); i++) {
                request.PhPtest(menu[i], price[i]);
            }
            robot.speak(TtsRequest.create("주문이 완료 되었습니다.", true));
            finish();
            robot.goTo(HOME_BASE_LOCATION);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        listData.clear();
        sAdapter.notifyDataSetInvalidated();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickDel(View v) {
        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        int count = sAdapter.getCount() ;
        for (int i = count-1; i >= 0; i--) {
            if (checkedItems.get(i)) {
                listData.remove(i) ;
            }
        }
        listView.clearChoices() ;
        sAdapter.notifyDataSetChanged();
    }

    //선택된 메뉴 리스트에 추가
    @Override
    public void onAddList(String a,String b) {
        item = new HashMap<>();
        item.put("menu",a);
        item.put("price",b);
        listData.add(item);
        sAdapter.notifyDataSetChanged();
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
    public void onPublish(@NotNull ActivityStreamPublishMessage activityStreamPublishMessage) {

    }

    @Override
    public void onAsrResult(@NotNull String asrResult) {
        Log.d("onAsrResult", "asrResult = " + asrResult);
        String[] a = {"꽃등심","블랙","포터","샤토"};
        String[] b = {"리가토니","알리오","마레비앙코","아마트리"};
        String[] c = {"시저","과카몰리","리코타","치킨"};
        String[] d = {"콜라","사이다","오렌지","레몬"};
        for(int i =0; i<4; i++){
            if (asrResult.contains(a[i])){
                onAddList(stk[i],money1[i]);
            }
            if (asrResult.contains(b[i])){
                onAddList(pst[i],money2[i]);
            }
            if (asrResult.contains(c[i])){
                onAddList(sld[i],money3[i]);
            }
            if (asrResult.contains(d[i])) {
                onAddList(drink[i], money4[i]);
            }
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