package com.example.quizofking;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Home extends Fragment {

    public Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View setData(View v) throws IOException {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Socket socket = new Socket("192.168.43.248", 7923);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject("get" + MainPage.username);
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();

            if (message.startsWith("u")) {
                TextView tv = v.findViewById(R.id.coin);
                String s1 = "Coin : " + message.substring(1, message.indexOf(','));
                tv.setText(s1);
                tv = v.findViewById(R.id.score);
                String s2 = "Score : " + message.substring(1 + message.indexOf(','));
                tv.setText(s2);
                tv = v.findViewById(R.id.username);
                tv.setText(MainPage.username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Button b = v.findViewById(R.id.newgame);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (MainPage.games.size() < 5) {
                        Socket socket = new Socket("192.168.43.248", 7923);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject("G" + MainPage.username);
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String message = (String) ois.readObject();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }


    public void getList() {
        try {
            Socket socket = new Socket("192.168.43.248", 7923);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("check" + MainPage.username);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            if (!message.equals("")) {
                String[] m = message.split(",");
                MainPage.games.clear();
                for (String i : m) {
                    int x = Integer.parseInt(i);
                    MainPage.games.add(x);
                }
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGames(View v) {
        getList();

        ArrayList rowItems = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            try {
                Integer integer = MainPage.games.get(i);
                if (integer < 0) {
                    rowItems.add("\n" + "                     Waiting for opponent ");
                } else {
                    rowItems.add("\n" + "                                   Ready ");
                }
            } catch (Exception e) {
                rowItems.add("\n" + "                     There is no game yet");
            }
        }

        ListView listView = v.findViewById(R.id.games);
        ListAdapter adapter = new ArrayAdapter(getContext(), R.layout.item, rowItems) {
        };
        listView.setAdapter(adapter);
        AdapterView.OnItemClickListener o = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    Integer ii = MainPage.games.get(i);
                    if (ii < 0) {

                    } else {
                        Intent iii = new Intent(getActivity(), GameMainPage.class);
                        GameMainPage.gid = ii;
                        MainPage.pause = 1;
                        startActivity(iii);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        listView.setOnItemClickListener(o);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.homefragment, container, false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (MainPage.pause == 0) {
                                    updateGames(setData(view));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        thread.start();
        try {
            return setData(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.homefragment, container, false);
    }
}
