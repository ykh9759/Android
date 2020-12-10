package com.example.remi.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.remi.intfc.AddListListener;
import com.example.remi.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BestFragment extends Fragment {
    private AddListListener addListListener;
    private ImageButton btnbest1,btnbest2,btnbest3;
    private TextView textbest1,textbest2,textbest3,textView1,textView2,textView3;
    private String[][] menu;
    private int[] max,b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_best, container, false);
        btnbest1 = view.findViewById(R.id.btnbest1);
        btnbest2 = view.findViewById(R.id.btnbest2);
        btnbest3 = view.findViewById(R.id.btnbest3);
        textbest1 = view.findViewById(R.id.textBest1);
        textbest2 = view.findViewById(R.id.textBest2);
        textbest3 = view.findViewById(R.id.textBest3);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        String url = "http://ci2020remi.dongyangmirae.kr/yeomkyounghoon/jsonBest.php";
        DownloadTask task = new DownloadTask();
        task.execute(url);

        View.OnClickListener ClickMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnbest1:
                        addListListener.onAddList(menu[b[0]][1],menu[b[0]][2]); break;
                    case R.id.btnbest2:
                        addListListener.onAddList(menu[b[1]][1],menu[b[1]][2]); break;
                    case R.id.btnbest3:
                        addListListener.onAddList(menu[b[2]][1],menu[b[2]][2]); break;
                } }
        };
        btnbest1.setOnClickListener(ClickMenu);
        btnbest2.setOnClickListener(ClickMenu);
        btnbest3.setOnClickListener(ClickMenu);

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

    class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected void onPostExecute(String s){
            super.onPostExecute(s);
            JsonObject json = (JsonObject) JsonParser.parseString(s);
            JsonArray jsonArray2 = json.getAsJsonArray("jsonBest");
            menu = new String[jsonArray2.size()][5];
            try {

                for (int i = 0; i < jsonArray2.size(); i++) {
                    JsonObject store = (JsonObject) jsonArray2.get(i);
                    menu[i][0] = store.get("category").getAsString().trim();
                    menu[i][1] = store.get("food").getAsString().trim();
                    menu[i][2] = store.get("price").getAsString().trim();
                    menu[i][3] = store.get("count").getAsString().trim();
                }
                max = new int[3];
                b = new int[3];
                for (int i = 0; i < jsonArray2.size(); i++) {
                    if (menu[i][0].equals("steak")) {
                        int a = Integer.parseInt(menu[i][3]);
                        if (a > max[0]) {
                            max[0] = a;
                            b[0] = i;
                        }
                    }
                    if (menu[i][0].equals("pasta")) {
                        int a = Integer.parseInt(menu[i][3]);
                        if (a > max[1]) {
                            max[1] = a;
                            b[1] = i;
                        }
                    }
                    if (menu[i][0].equals("salad")) {
                        int a = Integer.parseInt(menu[i][3]);
                        if (a > max[2]) {
                            max[2] = a;
                            b[2] = i;
                        }
                    }
                }

                if(menu[b[0]][1].contains("꽃")){
                    btnbest1.setImageResource(R.drawable.steak1);
                }else if(menu[b[0]][1].contains("블랙")) {
                    btnbest1.setImageResource(R.drawable.steak2);
                }else if(menu[b[0]][1].contains("샤토")) {
                    btnbest1.setImageResource(R.drawable.steak3);
                }else {
                    btnbest1.setImageResource(R.drawable.steak4);
                }

                if(menu[b[1]][1].contains("리가")) {
                    btnbest2.setImageResource(R.drawable.pasta1);
                }else if(menu[b[1]][1].contains("알리")) {
                    btnbest2.setImageResource(R.drawable.pasta2);
                }else if(menu[b[1]][1].contains("마레")) {
                    btnbest2.setImageResource(R.drawable.pasta3);
                }else {
                    btnbest2.setImageResource(R.drawable.pasta4);
                }

                if(menu[b[2]][1].contains("시져")) {
                    btnbest3.setImageResource(R.drawable.salad1);
                }else if(menu[b[2]][1].contains("과카")) {
                    btnbest3.setImageResource(R.drawable.salad2);
                }else if(menu[b[2]][1].contains("리코")) {
                    btnbest3.setImageResource(R.drawable.salad3);
                }else{
                    btnbest3.setImageResource(R.drawable.salad4);
                }

                textbest1.setText(menu[b[0]][1]);
                textView1.setText(menu[b[0]][2] + "원");
                textbest2.setText(menu[b[1]][1]);
                textView2.setText(menu[b[1]][2] + "원");
                textbest3.setText(menu[b[2]][1]);
                textView3.setText(menu[b[2]][2] + "원");
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... strings) {
            StringBuffer sBuf = new StringBuffer();

            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();
                BufferedInputStream bufStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(bufStream,"utf-8"));

                String line = null;
                while ((line = bufreader.readLine()) != null){
                    sBuf.append(line);
                }
                bufreader.close();
                bufStream.close();
                connection.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
            return sBuf.toString();
        }
    }
}
